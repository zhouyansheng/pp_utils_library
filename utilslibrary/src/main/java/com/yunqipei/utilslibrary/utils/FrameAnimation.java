package com.yunqipei.utilslibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Ansen on 2015/5/14 23:30.
 *
 * @E-mail: ansen360@126.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: FrameAnimation
 * @PACKAGE_NAME: com.ansen.frameanimation.sample
 * @Description: TODO
 */
public class FrameAnimation {

    private final OnAnimationStoppedListener mOnAnimationStoppedListener;
    private ImageView mImageView;
    private List<String> mFrameRess;
    /**
     * 每帧动画的播放间隔
     */
    private int mDuration;
    private int mLastFrame;


    /**
     * @param iv        播放动画的控件
     * @param drawables 播放的图片数组
     * @param duration  每帧动画的播放间隔(毫秒)
     */
    public FrameAnimation(ImageView iv, List<String> drawables, int duration, OnAnimationStoppedListener listener) {
        this.mImageView = iv;
        this.mFrameRess = drawables;
        this.mDuration = duration;
        this.mLastFrame = drawables.size() - 1;
        mOnAnimationStoppedListener = listener;
        play(0);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private void play(final int i) {
        mImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = decodeSampledBitmap(mFrameRess.get(i));
                mImageView.setImageBitmap(bitmap);
                if (i < mLastFrame) {
                    play(i + 1);
                } else {
                    if (mOnAnimationStoppedListener != null) {
                        mOnAnimationStoppedListener.AnimationStopped();
                    }
                }
            }
        }, mDuration);
    }

    public Bitmap decodeSampledBitmap(String frameRess) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(frameRess, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, mImageView.getWidth(), mImageView.getHeight());
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(frameRess, options);
    }

    public interface OnAnimationStoppedListener {
        void AnimationStopped();
    }
}
