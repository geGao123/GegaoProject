package com.example.gegao.gegaoproject.util;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloadThread extends Thread {
	private static final String TAG = "FileDownloadThread";
	/** 缓冲区 */
	private static final int BUFF_SIZE = 1024;
	/** 需要下载的URL */
	private URL url;
	/** 缓存的FIle */
	private File file;
	/** 开始位置 */
	private int startPosition;
	/** 结束位置 */
	private int endPosition;
	/** 当前位置 */
	private int curPosition;
	/** 完成 */
	private boolean finished = false;

	private boolean downloadException = false;
	/** 已经下载多少 */
	private int downloadSize = 0;
	private final String downloadCacheDirectory;


	/***
	 * 分块文件下载，可以创建多线程模式
	 * 
	 * @param url
	 *            下载的URL
	 * @param file
	 *            下载的文件
	 * @param startPosition
	 *            开始位置
	 * @param endPosition
	 *            结束位置
	 */
	public FileDownloadThread(URL url, File file, int startPosition, int endPosition,int hasDownloadSize,String path) {
		this.url = url;
		this.file = file;
		this.startPosition = startPosition;
		this.curPosition = startPosition;
		this.endPosition = endPosition;
		this.downloadSize = hasDownloadSize;
		downloadCacheDirectory = path;
	}

	@Override
	public void run() {
		BufferedInputStream bis = null;
		RandomAccessFile rAccessFile = null;
		byte[] buf = new byte[BUFF_SIZE];
		URLConnection conn = null;
		try {
			FileReader fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = url.openConnection();
			conn.setConnectTimeout(150000);// 设置超时
			conn.setReadTimeout(150000);
			conn.setAllowUserInteraction(true);
			System.out.println(this.getName() + " startPosition " + startPosition + " endPosition " + endPosition);
			conn.setRequestProperty("Range", "bytes=" + (startPosition) + "-" + endPosition); // 取剩余未下载的
			rAccessFile = new RandomAccessFile(file, "rwd");// 读写
			// 设置从什么位置开始写入数据
			rAccessFile.seek(startPosition);
			bis = new BufferedInputStream(conn.getInputStream(), BUFF_SIZE);
			while (curPosition < endPosition) // 当前位置小于结束位置 继续下载
			{
				int len = bis.read(buf, 0, BUFF_SIZE);
				if (len == -1) // 下载完成
				{
					break;
				}
				rAccessFile.write(buf, 0, len);
				curPosition = curPosition + len;
				FileUtil.writeThreadDownLoadInfo(downloadCacheDirectory,this.getName(),curPosition);
				if (curPosition > endPosition) { // 如果下载多了，则减去多余部分
					System.out.println("  curPosition > endPosition  !!!!");
					int extraLen = curPosition - endPosition;
					downloadSize += (len - extraLen + 1);
				} else {
					downloadSize += len;
				}
			}
			this.finished = true; // 当前阶段下载完成
//			LogUtils.e("当前" + this.getName() + "下载完成");
		} catch (Exception e) {
			downloadException = true;
			e.printStackTrace();

		} finally {
			// 关闭流
			try {
				bis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				rAccessFile.close();
			} catch (IOException e) {
			}
		}
		super.run();
	}

	public boolean isDownloadException() {
		return downloadException;
	}

	/**
	 * 是否完成当前段下载完成
	 * 
	 * @return
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * 已经下载多少
	 * 
	 * @return
	 */
	public int getDownloadSize() {
		return downloadSize;
	}

	@Override
	public String toString() {
		return "FileDownloadThread [url=" + url + ", file=" + file + ", startPosition=" + startPosition + ", endPosition=" + endPosition + ", curPosition=" + curPosition + ", finished=" + finished
				+ ", downloadSize=" + downloadSize + "]";
	}

}
