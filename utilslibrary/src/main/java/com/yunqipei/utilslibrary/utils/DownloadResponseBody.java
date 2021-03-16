package com.yunqipei.utilslibrary.utils;


import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * edie create on 2018/10/12
 */
public class DownloadResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private HttpUrl mUrl;
    private BufferedSource bufferedSource;

    public DownloadResponseBody(ResponseBody responseBody, HttpUrl url) {
        this.responseBody = responseBody;
        mUrl = url;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;   //不断统计当前下载好的数据
                if (bytesRead == -1) {
                    DownloadManager.getInstance().removeUrl(mUrl.toString());
                }
                return bytesRead;
            }
        };
    }

//    //回调接口
//    interface ProgressListener {
//        /**
//         * @param bytesRead     已经读取的字节数
//         * @param contentLength 响应总长度
//         * @param done          是否读取完毕
//         */
//        void update(long bytesRead, long contentLength, boolean done);
//    }
}