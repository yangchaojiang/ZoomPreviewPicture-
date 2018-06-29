package com.example.previewpicture.video;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.previewpicture.R;

import chuangyuan.ycj.videolibrary.video.ExoUserPlayer;
import chuangyuan.ycj.videolibrary.widget.VideoPlayerView;

public class VideoPlayerDetailedActivity extends Activity {

    private ExoUserPlayer exoPlayerManager;
    private VideoPlayerView videoPlayerView;
    private static final String TAG = "OfficeDetailedActivity";
    String[] test;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);
        videoPlayerView = findViewById(R.id.exo_play_context_id);
        exoPlayerManager = new ExoUserPlayer(this, videoPlayerView);
        //设置视频标题
        videoPlayerView.setTitle("视频标题");
        exoPlayerManager.setPlayUri(getIntent().getStringExtra("url"));
        exoPlayerManager.startPlayer();


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        exoPlayerManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        exoPlayerManager.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayerManager.onDestroy();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      //  exoPlayerManager.onConfigurationChanged(newConfig);//横竖屏切换
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (exoPlayerManager.onBackPressed()) {
            ActivityCompat.finishAfterTransition(this);
            exoPlayerManager.onDestroy();
        }
    }
}
