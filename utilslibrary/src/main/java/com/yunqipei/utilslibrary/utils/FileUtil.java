package com.yunqipei.utilslibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import okhttp3.ResponseBody;

/**
 * Created by water_mu on 2018/5/27.
 */

public class FileUtil {
    private static final String TAG = "FileUtil";

    public static boolean isAppFileExists(Context context) {
        File file = getAppFile(context);
        return file.exists() && file.isFile();
    }

    public static File getAppFile(Context context) {
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "llq.apk";
        return new File(filesDir, fileName);
    }

    public static File saveAppFile(ResponseBody body,Context context) {
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        String fileStoreDir = filesDir.getAbsolutePath();
        String fileName = "llq.apk";
        File file = saveFile(body, fileStoreDir, fileName);
        return file;
    }

    /**
     * 保存文件
     *
     * @param body
     * @param destFileDir
     * @param destFileName
     */
    public static File saveFile(ResponseBody body, String destFileDir, String destFileName) {
        File file = null;
        InputStream is = null;
        byte[] buf = new byte[1024];
        int len;
        FileOutputStream fos = null;
        try {
            is = body.byteStream();
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }
            file = new File(dir, destFileName);
            if (file.exists() && file.length() != 0) {
                boolean delete = file.delete();
            }

            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            //取消订阅
            //onCompleted();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                Log.e("saveFile", e.getMessage());
            }
        }
        return file;
    }

    public static File saveImageZipFile(ResponseBody body, String fileName,Context context) {
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        String fileStoreDir = filesDir.getAbsolutePath();
        File file = saveFile(body, fileStoreDir, fileName);
        return file;
    }


    /**
     * 读取json文本文件
     *
     * @param strFilePath 文件的完整路径
     * @return
     */
    public static String readTextFile(String strFilePath) {
        String resultString = "";
        File file = new File(strFilePath);
        if (file.isDirectory()) {
            Log.d(TAG, "The File doesn't not exist.");
        } else {

            try {
                InputStream inputStream = new FileInputStream(file);

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                //逐行读取
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStreamReader.close();
                bufferedReader.close();
                resultString = stringBuilder.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }

    public static Vector<String> getFileName(String fileAbsolutePath) {
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        if (subFile == null || subFile.length == 0) {
            return vecFile;
        }
        List<File> fileList = Arrays.asList(subFile);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });


        for (File aSubFile : fileList) {
            // 判断是否为文件夹
            if (!aSubFile.isDirectory()) {
                String filename = aSubFile.getAbsolutePath();
                vecFile.add(filename);
            }
        }
        return vecFile;
    }

    /**
     * 删除的文件夹所有文件
     *
     * @param file 删除的文件夹的所在位置
     */
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }

    }
}
