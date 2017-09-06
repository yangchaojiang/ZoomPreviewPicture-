package com.previewlibrary.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by yangc on 2017/9/4.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */


public interface IZoomMediaLoader     {

       /***
        * @param  context 容器
        * @param   path  图片你的路径
        * @param   simpleTarget   图片加载状态回调
        * ***/
       void displayImage(@NonNull Fragment context,@NonNull String path,@NonNull MySimpleTarget<Bitmap> simpleTarget);
       /**
        * 停止
        * @param context 容器
        * **/
         void  onStop(@NonNull Fragment context);
      /**
     * 停止
     * @param c 容器
     * **/
       void  clearMemory(@NonNull Context c);
}
