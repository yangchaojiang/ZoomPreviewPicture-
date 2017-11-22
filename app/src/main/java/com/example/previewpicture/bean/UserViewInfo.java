package com.example.previewpicture.bean;

import android.graphics.Rect;
import android.os.Parcel;

import com.previewlibrary.enitity.IThumbViewInfo;

/**
 *
 * @author yangc
 * @date 2017/4/26
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 图片预览实体类
 **/
public class UserViewInfo implements IThumbViewInfo {

    private String url;  //图片地址
    private Rect mBounds; // 记录坐标
    private String user = "用户字段";

    public UserViewInfo(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String getUrl() {//将你的图片地址字段返回
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Rect getBounds() {//将你的图片显示坐标字段返回
        return mBounds;
    }

    public void setBounds(Rect bounds) {
        mBounds = bounds;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeParcelable(this.mBounds, 0);
    }

    protected UserViewInfo(Parcel in) {
        this.url = in.readString();
        this.mBounds = in.readParcelable(Rect.class.getClassLoader());
    }

    public static final Creator<UserViewInfo> CREATOR = new Creator<UserViewInfo>() {
        public UserViewInfo createFromParcel(Parcel source) {
            return new UserViewInfo(source);
        }

        public UserViewInfo[] newArray(int size) {
            return new UserViewInfo[size];
        }
    };
}
