package com.previewlibrary;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.previewlibrary.enitity.ThumbViewInfo;
import java.util.ArrayList;

/**
 * Created by yangc on 2017/9/12.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public final class GPreviewBuilder {
    private Activity mContext;
    private Intent intent;
   private  Class  className;

    private GPreviewBuilder(Activity activity) {
        mContext = activity;
        intent = new Intent();
    }
    /***
     * 设置开始启动预览
     * @param activity  启动
     * **/
    public static GPreviewBuilder from(Activity activity) {
        return new GPreviewBuilder(activity);
    }
    /***
     * 设置开始启动预览
     * @param fragment  启动
     * **/
    public static GPreviewBuilder from(Fragment fragment) {
        return new GPreviewBuilder(fragment.getActivity());
    }
    /****
     *自定义预览activity 类名
     * @param className   继承GPreviewActivity
     * **/
    public GPreviewBuilder to(Class className) {
        this.className=className;
        intent.setClass(mContext, className);
        return this;
    }

    /***
     * 设置数据源
     * @param imgUrls 数据
     * @return     GPreviewBuilder
     * **/
    public GPreviewBuilder setData(ArrayList<ThumbViewInfo> imgUrls) {
        intent.putParcelableArrayListExtra("imagePaths", imgUrls);
        return this;
    }
    /***
     * 设置默认索引
     * @param currentIndex 数据
     * @return     GPreviewBuilder
     * **/
    public GPreviewBuilder setCurrentIndex(int currentIndex) {
        intent.putExtra("position", currentIndex);
        return this;
    }

    /***
     * 设置指示器类型
     * @param indicatorType 枚举
     * @return     GPreviewBuilder
     * **/
    public GPreviewBuilder setType(IndicatorType indicatorType) {
        intent.putExtra("type", indicatorType);
        return this;
    }
    /***
     * 设置超出内容点击退出（黑色区域）
     * @param isSingleFling  true  可以 false
     * @return     GPreviewBuilder
     * **/
    public GPreviewBuilder setSingleFling(boolean isSingleFling) {
        intent.putExtra("isSingleFling", isSingleFling);
        return this;
    }
    /***
     * 启动
     * **/
    public  void start(){
        if (className==null){
            intent.setClass(mContext,GPreviewActivity.class);
        }
        mContext.startActivity(intent);
        intent=null;
        mContext=null;
    }

  public   enum IndicatorType {
        Dot, Number
    }
}
