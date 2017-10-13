package com.example.testapp;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Created by yangc on 2017/10/10.
 */

public class NIneGridView extends FrameLayout {
    private RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    private MyNineGridAdapter mAdapter;
    public NIneGridView(@NonNull Context context) {
        super(context);
        init();
    }

    public NIneGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NIneGridView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
          recyclerView = new RecyclerView(getContext());
         int size= (int) getResources().getDimension(R.dimen.padding_size);
          recyclerView.setPadding(size,size,size,size);
          layoutManager = new GridLayoutManager(getContext(), 2,GridLayoutManager.HORIZONTAL,false);
          addView(recyclerView);
    }

    public void setAdapter(@NonNull MyNineGridAdapter adapter) {
        this.mAdapter=adapter;
        int count=mAdapter.getItemCount();
        if (count==1){
            layoutManager.setSpanCount(1);
        }else  if (count%2==0&&count<5){
            layoutManager.setSpanCount(2);
        }else  if (count>=3&&count!=4){
            layoutManager.setSpanCount(3);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public int getItemCounts() {
        return  layoutManager.getChildCount();
    }
    public View getItem(int index) {
        return  layoutManager.getChildAt(index);
    }
}
