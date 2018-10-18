package com.previewlibrary.loader;


import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * @author yangc
 * date 2017/5/7
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 图片加载回调状态接口
 */

public interface MySimpleTarget {
   /**
     * Callback when an image has been successfully loaded.
     * <p>
     * <strong>Note:</strong> You must not recycle the bitmap.
     *
     */
    void onResourceReady();

    /**
     * Callback indicating the image could not be successfully loaded.
     *
     * @param errorRes 内容
     */
    void onLoadFailed(@Nullable Drawable errorRes);


}
