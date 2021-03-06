package com.yunqipei.utilslibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * TITLE
 * Created by shixiaoming on 16/12/27.
 */

public class AnimationsContainer {
    // 单例
    public int FPS = 58;  // 每秒播放帧数，fps = 1/t，t-动画两帧时间间隔
    private List<String> resId; // R.array.loading_anim; //图片资源



    public AnimationsContainer(List<String> drawables, int fps) {
        this.resId = drawables;
        this.FPS = fps;
    }


    /**
     * @param imageView
     * @return progress dialog animation
     */
    public FramesSequenceAnimation createProgressDialogAnim(ImageView imageView) {
        return new FramesSequenceAnimation(imageView, resId, FPS);
    }

    /**
     * 停止播放监听
     */
    public interface OnAnimationStoppedListener {
        void AnimationStopped();
    }

    /**
     * 循环读取帧---循环播放帧
     */
    public class FramesSequenceAnimation {
        private List<String> mFrames; // 帧数组
        private int mIndex; // 当前帧
        private boolean mShouldRun; // 开始/停止播放用
        private boolean mIsRunning; // 动画是否正在播放，防止重复播放
        private SoftReference<ImageView> mSoftReferenceImageView; // 软引用ImageView，以便及时释放掉
        private Handler mHandler;
        private int mDelayMillis;
        private OnAnimationStoppedListener mOnAnimationStoppedListener; //播放停止监听

        private Bitmap mBitmap = null;
        private BitmapFactory.Options mBitmapOptions;//Bitmap管理类，可有效减少Bitmap的OOM问题

        public FramesSequenceAnimation(ImageView imageView, List<String> frames, int fps) {
            mHandler = new Handler();
            mFrames = frames;
            mIndex = -1;
            mSoftReferenceImageView = new SoftReference<ImageView>(imageView);
            mShouldRun = false;
            mIsRunning = false;
            mDelayMillis = 1000 / fps;//帧动画时间间隔，毫秒

            imageView.setImageBitmap(BitmapFactory.decodeFile(mFrames.get(0)));

            // 当图片大小类型相同时进行复用，避免频繁GC
            if (Build.VERSION.SDK_INT >= 11) {
                Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                int width = bmp.getWidth();
                int height = bmp.getHeight();
                Bitmap.Config config = bmp.getConfig();
                mBitmap = Bitmap.createBitmap(width, height, config);
                mBitmapOptions = new BitmapFactory.Options();
                //设置Bitmap内存复用
                mBitmapOptions.inBitmap = mBitmap;//Bitmap复用内存块，类似对象池，避免不必要的内存分配和回收
                mBitmapOptions.inMutable = true;//解码时返回可变Bitmap
                mBitmapOptions.inSampleSize = 1;//缩放比例
            }
        }

        /**
         * 播放动画，同步锁防止多线程读帧时，数据安全问题
         */
        public synchronized void start() {
            mShouldRun = true;
            if (mIsRunning)
                return;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ImageView imageView = mSoftReferenceImageView.get();
                    if (!mShouldRun || imageView == null) {
                        mIsRunning = false;
                        if (mOnAnimationStoppedListener != null) {
                            mOnAnimationStoppedListener.AnimationStopped();
                        }
                        return;
                    }

                    mIsRunning = true;
                    //新开线程去读下一帧
                    mHandler.postDelayed(this, mDelayMillis);

                    if (imageView.isShown()) {
                        String imageRes = getNext();
                        if (TextUtils.isEmpty(imageRes)) {
                            mShouldRun = false;
                            return;
                        }

                        if (mBitmap != null) { // so Build.VERSION.SDK_INT >= 11
                            Bitmap bitmap = BitmapFactory.decodeFile(imageRes, mBitmapOptions);
                            if (bitmap != null) {
                                imageView.setImageBitmap(bitmap);
                            } else {
                                imageView.setImageDrawable(Drawable.createFromPath(imageRes));
                                mBitmap.recycle();
                                mBitmap = null;
                            }
                        } else {
                            imageView.setImageDrawable(Drawable.createFromPath(imageRes));
                        }
                    }

                }
            };

            mHandler.post(runnable);
        }

        //循环读取下一帧
        private String getNext() {
            mIndex++;
            if (mIndex >= mFrames.size()) {
                return null;
            }
            return mFrames.get(mIndex);
        }

        /**
         * 停止播放
         */
        public synchronized void stop() {
            mShouldRun = false;
        }

        /**
         * 设置停止播放监听
         *
         * @param listener
         */
        public void setOnAnimStopListener(OnAnimationStoppedListener listener) {
            this.mOnAnimationStoppedListener = listener;
        }
    }
}