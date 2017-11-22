package com.previewlibrary.loader;


import android.graphics.drawable.Drawable;

/**
 *
 * @author yangc
 * date 2017/5/7
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 图片加载回调状态接口
 */

public interface MySimpleTarget<T> {
    /**
     * Callback when an image has been successfully loaded.
     * <p>
     * <strong>Note:</strong> You must not recycle the bitmap.
     * @param bitmap  内容
     */
    void onResourceReady(T bitmap);

    /**
     * Callback indicating the image could not be successfully loaded.
     * @param errorRes 内容
     */
    void onLoadFailed(Drawable errorRes);

    /**
     * Callback invoked right before your request is submitted.
     */
    void onLoadStarted();

}
