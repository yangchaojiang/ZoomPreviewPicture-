package com.example.previewpicture.nine.entity;

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
    private List<String> mImgUrlList;

    public Post() {
    }

    public Post(String content, List<String> imgUrlList) {
        mContent = content;
        mImgUrlList = imgUrlList;
    }
    public Post(String content,int spanType, List<String> imgUrlList) {
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

    public List<String> getImgUrlList() {
        return mImgUrlList;
    }

    public void setImgUrlList(List<String> imgUrlList) {
        mImgUrlList = imgUrlList;
    }
}
