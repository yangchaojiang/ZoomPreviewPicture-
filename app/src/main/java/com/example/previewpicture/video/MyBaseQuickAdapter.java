package com.example.previewpicture.video;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.previewpicture.R;
import com.example.previewpicture.bean.UserViewInfo;


/**
 * Created by yangc on 2017/8/29.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public class MyBaseQuickAdapter extends BaseQuickAdapter<UserViewInfo,BaseViewHolder> {
    public static final String TAG = "MyBaseQuickAdapter";
  private  Context context;
    public MyBaseQuickAdapter(Context context) {
        super(R.layout.item_image);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserViewInfo item) {
        final ImageView thumbView = helper.getView(R.id.iv);
       if (item.getVideoUrl()==null){
           helper.getView(R.id.btnVideo).setVisibility(View.GONE);
       }else {
           helper.getView(R.id.btnVideo).setVisibility(View.VISIBLE);
       }

        Glide.with(context)
                .load(item.getUrl())
                .error(R.mipmap.ic_iamge_zhanwei)
                .into(thumbView);
        thumbView.setTag(R.id.iv,item.getUrl());
    }
}
