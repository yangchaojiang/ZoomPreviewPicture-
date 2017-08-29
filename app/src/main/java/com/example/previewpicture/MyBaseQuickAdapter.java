package com.example.previewpicture;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.previewlibrary.ThumbViewInfo;


/**
 * Created by yangc on 2017/8/29.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public class MyBaseQuickAdapter extends BaseQuickAdapter<ThumbViewInfo,BaseViewHolder> {
    public static final String TAG = "MyBaseQuickAdapter";
  private  Context context;
    public MyBaseQuickAdapter(Context context) {
        super(R.layout.item_image);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ThumbViewInfo item) {
        final ImageView thumbView = helper.getView(R.id.iv);
        Glide.with(context)
                .load(item.getUrl())
                .error(R.drawable.ic_iamge_zhanwei)
                .into(thumbView);
        helper.getView(R.id.iv).setTag(R.id.iv,item.getUrl());
    }
}
