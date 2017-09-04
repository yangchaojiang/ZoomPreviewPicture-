package com.previewlibrary.loader;


/**
 * Created by yangc on 2017/5/7.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 图片加载回调状态接口
 */

public interface MySimpleTarget<T> {
    /**
     * Callback when an image has been successfully loaded.
     * <p>
     * <strong>Note:</strong> You must not recycle the bitmap.
     */
    void onResourceReady(T bitmap);

    /**
     * Callback indicating the image could not be successfully loaded.
     */
    void onLoadFailed(int errorRes);

    /**
     * Callback invoked right before your request is submitted.
     */
    void onLoadStarted();

}
