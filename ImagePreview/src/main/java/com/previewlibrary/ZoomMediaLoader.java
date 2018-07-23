package com.previewlibrary;

import com.previewlibrary.loader.IZoomMediaLoader;

/**
 *
 * @author yangc
 * date 2017/9/4
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 图片加载管理器
 */

public class ZoomMediaLoader {
    private volatile IZoomMediaLoader loader;
    public  static ZoomMediaLoader getInstance(){
        return  Holder.holder;
    }
    private ZoomMediaLoader(){

    }
    private  static  class  Holder{
           static ZoomMediaLoader holder=new ZoomMediaLoader();
    }
    /****
     * 初始化加载图片类
     * @param  loader 自定义
     * **/
    public  void init(IZoomMediaLoader loader){
        this.loader=loader;
    }

    public IZoomMediaLoader getLoader() {
        if (loader==null){
            throw  new  NullPointerException("ZoomMediaLoader loader  no init");
        }
        return loader;
    }
}
