package com.previewlibrary.loader;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

/**
 * Created by yangc on 2017/9/4.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */


public interface IZoomMediaLoader {

       /***
        * @param  context 容器
        * @param   path  图片你的路径
        * @param   simpleTarget   图片加载状态回调
        * ***/
       void displayImage(Fragment context, String path,MySimpleTarget<Bitmap> simpleTarget);
       /**
        * 停止
        * @param context 容器
        * **/
         void  onStop(Fragment context);
}
