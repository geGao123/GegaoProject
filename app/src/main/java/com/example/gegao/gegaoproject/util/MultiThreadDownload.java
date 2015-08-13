package com.example.gegao.gegaoproject.util;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;

public class MultiThreadDownload extends Thread {
	private static final String TAG = "MultiThreadDownload";
	/** 每一个线程需要下载的大小 */
	private int blockSize;
	/***
	 * 线程数量<br>
	 * 默认为4个线程下载
	 */
	private int threadNum = 4;
	/*** 文件大小 */
	private int fileSize;
	/** * 已经下载多少 */
	private int downloadSize;
	/** 文件的url,线程编号，文件名称 */
	private String UrlStr, ThreadNo, fileName;
	/*** 保存的路径 */
	private String savePath;
	/** 下载的百分比 */
	private int downloadPercent = 0;
	/** 下载的 平均速度 */
	private int downloadSpeed = 0;
	/** 下载用的时间 */
	private int usedTime = 0;
	/** 当前时间 */
	private long curTime;
	/** 是否已经下载完成 */
	private boolean completed = false;
	private Handler handler;

	private final File downloadCacheDirectory;//续传时  上次下载的信息
	private URL url;//conn的连接
	private URLConnection conn;
	private File saveFile;

	/**
	 * 下载的构造函数
	 * 
	 * @param url
	 *            请求下载的URL
	 * @param handler
	 *            UI更新使用
	 * @param savePath
	 *            保存文件的路径
	 */
	public MultiThreadDownload(Handler handler, String url, String savePath) {
		downloadCacheDirectory = Environment.getDownloadCacheDirectory();
		this.handler = handler;
		this.UrlStr = url;
		this.savePath = savePath;
	}

	@Override
	public void run() {

		FileDownloadThread[] fds = new FileDownloadThread[threadNum];// 设置线程数量
//==========================文件未下载完毕==========================
		String recordFolderName = MD5Utils.getMD5String(UrlStr);
		File recordFolder = new File(downloadCacheDirectory,recordFolderName);

		if (recordFolder.exists() && recordFolder.isDirectory()){
			for (int i = 0; i < threadNum; i++) {
				int curThreadEndPosition = (i + 1) != threadNum ? ((i + 1) * blockSize - 1) : fileSize;
				FileDownloadThread fdt = new FileDownloadThread(url, saveFile, i * blockSize, curThreadEndPosition);
				fdt.setName("thread" + i);
				fdt.start();
				fds[i] = fdt;
			}
		}
//==========================文件未下载完毕==========================

//==========================文件第一次下载==========================

		try {
			url = new URL(UrlStr);
			conn = url.openConnection();
			fileSize = conn.getContentLength();
			this.fileName = FileUtil.getFileName(UrlStr);
			// 只创建一个文件，saveFile下载内容
			// File saveFile = new File(savePath + "/" + fileName);
			saveFile = new File(fileName);
			RandomAccessFile accessFile = new RandomAccessFile(saveFile, "rwd");
			// 设置本地文件的长度和下载文件相同
			accessFile.setLength(fileSize);
			accessFile.close();

			// Handler更新UI，发送消息
			// sendMsg(FileUtil.startDownloadMeg);
			// 每块线程下载数据
			blockSize = ((fileSize % threadNum) == 0) ? (fileSize / threadNum) : (fileSize / threadNum + 1);
			for (int i = 0; i < threadNum; i++) {
				int curThreadEndPosition = (i + 1) != threadNum ? ((i + 1) * blockSize - 1) : fileSize;
				FileDownloadThread fdt = new FileDownloadThread(url, saveFile, i * blockSize, curThreadEndPosition);
				fdt.setName("thread" + i);
				fdt.start();
				fds[i] = fdt;
			}
			/**
			 * 获取数据，更新UI，直到所有下载线程都下载完成。
			 */
			boolean finished = false;
			// 开始时间，放在循环外，求解的usedTime就是总时间
			long startTime = System.currentTimeMillis();
			while (!finished) {
				downloadSize = 0;
				finished = true;
				for (int i = 0; i < fds.length; i++) {
					downloadSize += fds[i].getDownloadSize();
					if (fds[i].isDownloadException()) {
						downloadSize -= fds[i].getDownloadSize();
						fds[i].start();
						throw new SocketException("线程 Connection timed out");
					}
					if (!fds[i].isFinished()) {
						finished = false;
					}
				}
				downloadPercent = (downloadSize * 100) / fileSize;
				curTime = System.currentTimeMillis();
				System.out.println("curTime = " + curTime + " downloadSize = " + downloadSize + " usedTime " + (int) ((curTime - startTime) / 1000));
				usedTime = (int) ((curTime - startTime) / 1000);
				if (usedTime == 0)
					usedTime = 1;
				downloadSpeed = (downloadSize / usedTime) / 1024;
				sleep(100);/* 1秒钟刷新一次界面 */
				sendMsg(FileUtil.updateDownloadMeg, downloadPercent);
			}
			completed = true;
			sendMsg(FileUtil.endDownloadMeg, downloadPercent);
		} catch (Exception e) {
//			LogUtils.e(e.toString());
			for (int i = 0; i < fds.length; i++) {
				if (null != fds[i])
					fds[i].interrupt();
			}
			sendMsg(FileUtil.exceptionDownloadMeg, 0);
		}
		super.run();
	}

	/**
	 * 得到文件的大小
	 * 
	 * @return
	 */
	public int getFileSize() {
		return this.fileSize;
	}

	/**
	 * 得到已经下载的数量
	 * 
	 * @return
	 */
	public int getDownloadSize() {
		return this.downloadSize;
	}

	/**
	 * 获取下载百分比
	 * 
	 * @return
	 */
	public int getDownloadPercent() {
		return this.downloadPercent;
	}

	/**
	 * 获取下载速度
	 * 
	 * @return
	 */
	public int getDownloadSpeed() {
		return this.downloadSpeed;
	}

	/**
	 * 修改默认线程数
	 * 
	 * @param threadNum
	 */
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	/**
	 * 分块下载完成的标志
	 * 
	 * @return
	 */
	public boolean isCompleted() {
		return this.completed;
	}

	@Override
	public String toString() {
		return "MultiThreadDownload [threadNum=" + threadNum + ", fileSize=" + fileSize + ", UrlStr=" + UrlStr + ", ThreadNo=" + ThreadNo + ", savePath=" + savePath + "]";
	}

	/**
	 * 发送消息，用户提示
	 * */
	private void sendMsg(int what, int progress) {
		Message msg = new Message();
		msg.what = what;
		msg.arg1 = progress;
		handler.sendMessage(msg);
	}

	public void interruptAll() {

	}



}
