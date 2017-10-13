package com.example.testapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Jaeger on 16/2/24.
 * <p>
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public   class MyNineGridAdapter extends RecyclerView.Adapter<MyNineGridAdapter.PostViewHolder> {
    private LayoutInflater mInflater;
    private List<String> mPostList;

    public MyNineGridAdapter(Context context, List<String> postList) {
        super();
        mPostList = postList;
        mInflater = LayoutInflater.from(context);
    }

    public MyNineGridAdapter(Context context) {
        super();
        mInflater = LayoutInflater.from(context);
    }

    public void setImagesData(List<String> list) {
        mPostList = list;
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
        return new PostViewHolder(mInflater.inflate(getItemLayImage(), parent, false));
    }

  class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PostViewHolder(View itemView) {
            super(itemView);
            if (itemView instanceof ImageView) {
                imageView = (ImageView) itemView;
            } else {
                throw new NullPointerException("ImageView not");
            }
        }

        public void bind(String post) {
            onDisplayImage(imageView.getContext(), imageView, post);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemImageClick(v.getContext(), imageView, getAdapterPosition(), mPostList);
                }
            });
        }
    }

    protected int getItemLayImage() {
        return 0;
    }

    protected   void onDisplayImage(Context context, ImageView imageView, String t){};

    protected   void onItemImageClick(Context context, ImageView imageView, int index, List<String> list){};

}
