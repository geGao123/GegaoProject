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

    private boolean flag;

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
     * @return 如果sd卡可用，返回的路径为[sd卡路径/relativePath],如果sd卡不可用,返回路径为[下载缓存路径/relativePath
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
     */
    public static final int updateDownloadMeg = 1;

    /**
     * 完成消息提示常量
     */
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

    public static void writeThreadDownLoadInfo(String path, String fileName, int count) {
        File file = null;
        FileWriter fileWriter = null;
        try {
            file = new File(path, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file.getAbsoluteFile(), false);
            fileWriter.write(count + "");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null)
                    fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ;
        }
    }

    public static int readThreadDownLoadInfo(String path, String fileName) {
        File file = null;
        FileReader fileReader = null;
        int valueOf = 0;
        try {
            file = new File(path, fileName);
            if (!file.exists()) {
//                file.createNewFile();
                return 0;
            }
            fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String readLine = br.readLine();
            if (readLine != null && !readLine.equals("null"))
                valueOf = Integer.valueOf(readLine.equals("null") ? 0 + "" : readLine);
        } catch (IOException e) {
            e.printStackTrace();
            return valueOf;
        } finally {
//            try {
//                if (fileReader != null)
//                    fileReader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        return valueOf;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件 
     * @param   sPath 被删除目录的文件路径 
     * @return  目录删除成功返回true，否则返回false 
     */
    public boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件  
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录  
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录  
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String sPath) {
        flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

}
