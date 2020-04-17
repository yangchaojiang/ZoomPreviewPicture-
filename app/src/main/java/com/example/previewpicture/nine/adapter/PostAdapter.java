package com.example.previewpicture.nine.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.previewpicture.R;
import com.example.previewpicture.bean.UserViewInfo;
import com.example.previewpicture.nine.entity.Post;
import com.jaeger.ninegridimageview.ItemImageClickListener;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.previewlibrary.GPreviewBuilder;
import java.util.List;

/**
 * Created by Jaeger on 16/2/24.
 *
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private LayoutInflater mInflater;
    private List<Post> mPostList;
    private int mShowStyle;

    public PostAdapter(Context context, List<Post> postList, int showStyle) {
        super();
        mPostList = postList;
        mInflater = LayoutInflater.from(context);
        mShowStyle = showStyle;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bind(mPostList.get(position));
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mShowStyle == NineGridImageView.STYLE_FILL) {
            return new PostViewHolder(mInflater.inflate(R.layout.item_post_fill_style, parent, false));
        } else {
            return new PostViewHolder(mInflater.inflate(R.layout.item_post_grid_style, parent, false));
        }
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private NineGridImageView<UserViewInfo> mNglContent;
        private TextView mTvContent;
        private NineGridImageViewAdapter<UserViewInfo> mAdapter = new NineGridImageViewAdapter<UserViewInfo>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, UserViewInfo s) {
                Glide.with(context).load(s.getUrl()).placeholder(R.drawable.ic_default_image).into(imageView);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }

            @Override
            protected void onItemImageClick(Context context, ImageView imageView, int index, List<UserViewInfo> list) {
              //  Toast.makeText(context, "image position is " + index, Toast.LENGTH_SHORT).show();
            }
        };

        public PostViewHolder(View itemView) {
            super(itemView);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
            mNglContent = (NineGridImageView<UserViewInfo>) itemView.findViewById(R.id.ngl_images);
            mNglContent.setAdapter(mAdapter);
            mNglContent.setItemImageClickListener(new ItemImageClickListener<UserViewInfo>() {
                @Override
                public void onItemImageClick(Context context, ImageView imageView, int index, List<UserViewInfo> list) {
                    Log.d("onItemImageClick", list.get(index).getUrl());
                    computeBoundsBackward(list);//组成数据
                    GPreviewBuilder.from((Activity) context)
                            .setData(list)
                            .setIsScale(true)
                            .setCurrentIndex(index)
                            .setType(GPreviewBuilder.IndicatorType.Dot)
                            .start();//启动
                }
            });
        }
        /**
         * 查找信息
         * @param list 图片集合
         */
        private void computeBoundsBackward(List<UserViewInfo> list) {
            for (int i = 0;i < mNglContent.getChildCount(); i++) {
                View itemView = mNglContent.getChildAt(i);
                Rect bounds = new Rect();
                if (itemView != null) {
                    ImageView thumbView = (ImageView) itemView;
                    thumbView.getGlobalVisibleRect(bounds);
                }
                list.get(i).setBounds(bounds);
                list.get(i).setUrl(list.get(i).getUrl());
            }

        }

        public void bind(Post post) {
            mNglContent.setImagesData(post.getImgUrlList());
            mTvContent.setText(post.getContent());
            Log.d("jaeger", "九宫格高度: " + mNglContent.getMeasuredHeight());
            Log.d("jaeger", "item 高度: " + itemView.getMeasuredHeight());
        }
    }

}
