package com.previewlibrary.enitity;

import android.graphics.Rect;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * @author yangc
 *         date 2017/4/26
 *         E-Mail:yangchaojiang@outlook.com
 *         Deprecated: 图片预览接口
 */
public interface IThumbViewInfo extends Parcelable {
    /****
     * 图片地址
     * @return String
     * ****/
    String getUrl();

    /**
     * 记录坐标
     * @return Rect
     ***/
    Rect getBounds();


    /**
     * 获取视频链接
     * ***/
    @Nullable
     String getVideoUrl();


}
