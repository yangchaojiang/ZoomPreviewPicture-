package com.previewlibrary.view;

import android.content.Context;

import com.previewlibrary.R;


/**
 * @author  yangc
 * date 2017/11/22
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public class ImageUtils {
    /**
     * 取状态栏高度
     *@param mApplicationContent mApplicationContent
     * @return int
     */
    public static int getStatusBarHeight(Context mApplicationContent) {
        int result =  mApplicationContent.getResources().getDimensionPixelSize(R.dimen.yms_dimens_50_0_px);
        int resourceId = mApplicationContent.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mApplicationContent.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
