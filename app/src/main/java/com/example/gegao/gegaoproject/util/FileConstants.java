package com.example.gegao.gegaoproject.util;

import android.os.Environment;

/**
 * @author luopeng (luopeng@news.cn)
 * @date 2014年9月15日
 */
public class FileConstants {

	public static final String WORK_FOLDER;
	public static final String PATH_TEMP;
	public static final String PATH_USER;
	public static final String PATH_MP4;
	public static final String PATH_MP3;
	public static final String PATH_REC;
	public static final String PATH_REC_TEMP;
	public static final String PATH_BANZOU;
	public static final String PATH_MERGE;

	public static final String PATH_REC_VIDEO;
	public static final String PATH_TEMP_ACC;
	public static final int OFFSET_REC = 500;// 毫秒
	public static final String PATH_CACHE_IMG;

	static {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			WORK_FOLDER = Environment.getExternalStorageDirectory().toString() + "/iSing";
		} else {
			WORK_FOLDER = Environment.getDownloadCacheDirectory().getPath();
		}

		PATH_USER = FileUtil.getStoragePath("/iSing/user");
		PATH_TEMP = FileUtil.getStoragePath("/iSing/temp");
		PATH_MP3 = FileUtil.getStoragePath("/iSing/user/mp3");
		PATH_MP4 = FileUtil.getStoragePath("/iSing/user/mp4");

		PATH_TEMP_ACC = PATH_TEMP + "/rec.aac"; // 编码成AAC格式后临时路径
		PATH_REC_VIDEO = PATH_TEMP + "/rec_video.mp4"; // 录制mv时视频输出路径
		PATH_REC = PATH_TEMP + "/rec.pcm"; // 录制pcm音频时输出的路径
		PATH_REC_TEMP = PATH_TEMP + "/rec_temp.pcm"; // 人声pcm再次处理后的输出路径
		PATH_BANZOU = PATH_TEMP + "/banzou.pcm"; // mp3伴奏转换成pcm路径
		PATH_MERGE = PATH_TEMP + "/out.pcm"; // 伴奏和人生合成时输出路径
		PATH_CACHE_IMG = FileUtil.getStoragePath("/iSing/cache");
	}

	// 歌曲文件存放目录
	public static final String WORK_FOLDER_SONGS = WORK_FOLDER + "/songs";
	// 拍照存放路径
	public static final String WORK_FOLDER_PHOTO_TEMP = WORK_FOLDER + "/phototemp";
	// 升级下载的apk文件夹
	public static final String WORK_FOLODER_APK_FOLDER = WORK_FOLDER + "/apk";
	// 升级下载的apk文件
	public static final String WORK_FOLODER_APK = WORK_FOLODER_APK_FOLDER + "/ising.apk";

	/**
	 * 拍照 在本地设置的一个存放路径。
	 */
	public static final String BACKGROUND_IMG_PATH = FileConstants.WORK_FOLDER_PHOTO_TEMP + "/temp_background.jpg";
}
