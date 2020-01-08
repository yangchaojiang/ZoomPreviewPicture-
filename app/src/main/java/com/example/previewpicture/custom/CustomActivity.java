package com.example.previewpicture.custom;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.previewpicture.R;
import com.previewlibrary.GPreviewActivity;


/**
 * Created by yangc on 2017/9/19.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public class CustomActivity extends GPreviewActivity {
    /***
     * 重复该方法 *使用你的自定义布局
     ***/
    @Override
    public int setContentLayout() {
        return R.layout.activity_custom_preview;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar   toolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.testBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("sss","'toolbar'");
                Toast.makeText(getApplicationContext(),"测试",Toast.LENGTH_SHORT).show();
                //退出时调用，d封装方法的 不然没有动画效果
                transformOut();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("sss","'toolbar'");
                //退出时调用，d封装方法的 不然没有动画效果
                transformOut();
            }
        });
    }
}
