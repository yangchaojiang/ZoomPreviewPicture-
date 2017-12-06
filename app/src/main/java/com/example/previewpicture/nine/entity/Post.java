package com.example.previewpicture.nine.entity;

import com.example.previewpicture.bean.UserViewInfo;

import java.util.List;

/**
 * Created by Jaeger on 16/2/24.
 *
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public class Post {
    private String mContent;
    private int mSpanType;
    private List<UserViewInfo> mImgUrlList;

    public Post() {
    }

    public Post(String content, List<UserViewInfo> imgUrlList) {
        mContent = content;
        mImgUrlList = imgUrlList;
    }
    public Post(String content,int spanType, List<UserViewInfo> imgUrlList) {
        mContent = content;
        mSpanType = spanType;
        mImgUrlList = imgUrlList;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getmSpanType() {
        return mSpanType;
    }

    public void setmSpanType(int mSpanType) {
        this.mSpanType = mSpanType;
    }

    public List<UserViewInfo> getImgUrlList() {
        return mImgUrlList;
    }

    public void setImgUrlList(List<UserViewInfo> imgUrlList) {
        mImgUrlList = imgUrlList;
    }
}
