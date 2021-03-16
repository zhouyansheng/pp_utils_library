package com.yunqipei.utilslibrary.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by jiajie on 2017/12/6.
 */

public class MultiPartUtil {

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static void putRequestBodyMap(Map map, String key, String value) {
        putRequestBodyMap(map, key, createPartFromString(value));
    }

    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        if (descriptionString == null) {
            descriptionString = "";
        }
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    public static void putRequestBodyMap(Map map, String key, RequestBody body) {
        if (!TextUtils.isEmpty(key) && body != null) {
            map.put(key, body);
        }
    }
//
//    public static MultipartBody.Part prepareFilePart(String partName, String fileUri) {
//        //压缩
//        File file = ViewUtils.compressImageToFile(fileUri, new File(FileUtils.getImageCache(BaseApplication.getContext()), partName + ".png"));
//        if (file != null) {
//            // 为file建立RequestBody实例
//            RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
//            // MultipartBody.Part借助文件名完成最终的上传
//            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
//        }
//        return null;
//    }

}
