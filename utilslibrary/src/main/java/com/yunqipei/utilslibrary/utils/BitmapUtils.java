package com.yunqipei.utilslibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xsw on 2017/3/22 0022.
 */
public class BitmapUtils {
    /**
     * 压缩图片
     * 只压缩图片质量
     *
     * @param imagePath
     */
    public static byte[] yasuoPicOnlyQuality(Context context, String imagePath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = context.getContentResolver().openInputStream(Uri.parse(imagePath));
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }


    /**
     * 保存图片
     *
     * @param bmp
     * @param dir
     * @param fileName
     */
    public static void saveToSD(Bitmap bmp, File dir, String fileName) {
        // 判断文件夹是否存在，不存在则创建
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if (fos != null) {
                // 第一参数是图片格式，第二个是图片质量，第三个是输出流
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                // 用完关闭
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
