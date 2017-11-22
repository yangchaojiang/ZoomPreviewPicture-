package com.example.previewpicture.custom;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.previewpicture.ImageUrlConfig;
import com.example.previewpicture.R;
import com.example.previewpicture.bean.UserViewInfo;
import com.previewlibrary.GPreviewBuilder;

import java.util.ArrayList;
import java.util.List;

public class GridViewCustomActivity extends AppCompatActivity {
    private ArrayList<UserViewInfo> mThumbViewInfoList = new ArrayList<>();
    GridView listView;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        type = getIntent().getIntExtra("type", 0);
        listView = (GridView) findViewById(R.id.listView);
        //准备数据
        List<String> urls = ImageUrlConfig.getUrls();
        for (int i = 0; i < urls.size(); i++) {
            mThumbViewInfoList.add(new UserViewInfo(urls.get(i)));
        }
        MyListAdapter adapter = new MyListAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                computeBoundsBackward(listView.getFirstVisiblePosition());
                if (type == 0) {//自定义activity
                    GPreviewBuilder.from(GridViewCustomActivity.this)
                            .to(CustomActivity.class)
                            .setData(mThumbViewInfoList)
                            .setCurrentIndex(position)
                            .setType(GPreviewBuilder.IndicatorType.Dot)
                            .start();
                } else if (type == 1) {//自定义Fragment
                    GPreviewBuilder.from(GridViewCustomActivity.this)
                            .setData(mThumbViewInfoList)
                            .setCurrentIndex(position)
                            .setUserFragment(UserFragment.class)
                            .setType(GPreviewBuilder.IndicatorType.Dot)
                            .start();
                } else {
                    //自定义activity和Fragment

                    GPreviewBuilder.from(GridViewCustomActivity.this)
                            .to(CustomActivity.class)
                            .setData(mThumbViewInfoList)
                            .setUserFragment(UserFragment.class)
                            .setCurrentIndex(position)
                            .setSingleFling(false)
                            .setDrag(false)
                            .setType(GPreviewBuilder.IndicatorType.Dot)
                            .start();
                }


            }
        });
    }

    /**
     * 查找信息
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos; i < mThumbViewInfoList.size(); i++) {
            View itemView = listView.getChildAt(i - firstCompletelyVisiblePos);
            Rect bounds = new Rect();
            if (itemView != null) {
                //需要显示过度控件
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.iv);
                //拿到在控件屏幕可见中控件显示为矩形Rect信息
                thumbView.getGlobalVisibleRect(bounds);
            }
            mThumbViewInfoList.get(i).setBounds(bounds);
        }
    }

    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mThumbViewInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mThumbViewInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.item_image, null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv);
            Glide.with(GridViewCustomActivity.this)
                    .load(mThumbViewInfoList.get(position).getUrl())
                    .error(R.mipmap.ic_iamge_zhanwei)
                    .into(iv);
            iv.setTag(R.id.iv, mThumbViewInfoList.get(position));
            return view;
        }
    }

}
