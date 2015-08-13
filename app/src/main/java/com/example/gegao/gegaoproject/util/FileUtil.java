package com.example.gegao.gegaoproject.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FileUtil {

	public static String getString(InputStream inputStream) {
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, "gbk");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuffer sb = new StringBuffer("");
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static void createFile(Context c, String filePath) {
		// 检查手机上是否有外部存储卡
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (!sdCardExist) {// 如果不存在SD卡，进行提示
			Toast.makeText(c, "请插入外部SD存储卡", Toast.LENGTH_SHORT).show();
		} else {
			File dirFirstFile = new File(filePath);// 新建一级主目录
			if (!dirFirstFile.exists()) {// 判断文件夹目录是否存在
				dirFirstFile.mkdir();// 如果不存在则创建
			}
		}
	}

	/**
	 * 获取一个可存储的路径
	 * 
	 * @param relativePath 需要保存的文件夹目录
	 * @return 
	 *         如果sd卡可用，返回的路径为[sd卡路径/relativePath],如果sd卡不可用,返回路径为[下载缓存路径/relativePath
	 */
	public static String getStoragePath(String relativePath) {
		if (hasSdcard()) {
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + relativePath);
			if (!file.exists())
				file.mkdirs();
			return file.getPath();
		} else {
			File file = new File(Environment.getDownloadCacheDirectory().getAbsolutePath() + relativePath);
			if (!file.exists())
				file.mkdirs();
			return file.getPath();
		}
	}

	/**
	 * 检查是否存在SDCard
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param directory
	 */
	public static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				File temp = new File(item.getAbsolutePath() + System.currentTimeMillis());
				item.renameTo(temp);
				temp.delete();
				// item.delete();
			}
			File temp = new File(directory.getAbsolutePath() + System.currentTimeMillis());
			directory.renameTo(temp);
			temp.delete();
		}
	}

	/**
	 * 开始消息提示常量
	 * */
	// public static final int startDownloadMeg = 1;

	/**
	 * 更新消息提示常量
	 * */
	public static final int updateDownloadMeg = 1;

	/**
	 * 完成消息提示常量
	 * */
	public static final int endDownloadMeg = 2;
	/**
	 * 异常消息提示常量
	 */
	public static final int exceptionDownloadMeg = -1;

	/**
	 * 检验SDcard状态
	 * 
	 * @return boolean
	 */
	public static boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 保存文件文件到目录
	 * 
	 * @param context
	 * @return 文件保存的目录
	 */
	public static String setMkdir(Context context) {
		String filePath;
		if (checkSDCard()) {
			filePath = Environment.getExternalStorageDirectory() + File.separator + "myfile";
		} else {
			filePath = context.getCacheDir().getAbsolutePath() + File.separator + "myfile";
		}
		File file = new File(filePath);
		if (!file.exists()) {
			boolean b = file.mkdirs();
//			LogUtils.e("文件不存在  创建文件    " + b);
		} else {
//			LogUtils.e("文件存在");
		}
		return filePath;
	}

	/**
	 * 得到文件的名称
	 * 
	 * @return
	 * @throws IOException
	 */
	// public static String getFileName(String url) {
	// String name = null;
	// try {
	// name = url.substring(url.lastIndexOf("/") + 1);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return name;
	// }

	public static String getFileName(String pathandname) {

		int start = pathandname.lastIndexOf("/");
		if (start != -1) {
			File file = new File(FileConstants.WORK_FOLDER_SONGS);
			if (!file.exists()) {
				file.mkdirs();
			}
			return FileConstants.WORK_FOLDER_SONGS + "/" + File.separator + pathandname.substring(start + 1, pathandname.length());
		} else {
			return null;
		}

	}

	public void writeThreadDownLoadInfo(String fileName,int count) throws IOException {
		File file = new File("1.txt");
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(count+"");
		fileWriter.flush();
		fileWriter.close();
	}

	public int readThreadDownLoadInfo(String fileName) throws IOException {
		File file = new File(fileName);
		FileReader fileReader = new FileReader(file);
		BufferedReader br = new BufferedReader(fileReader);
		String readLine = br.readLine();
		int valueOf = Integer.valueOf(readLine);
		return valueOf;
	}

}
