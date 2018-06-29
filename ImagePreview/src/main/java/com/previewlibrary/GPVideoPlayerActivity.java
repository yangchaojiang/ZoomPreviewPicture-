package com.previewlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

public class GPVideoPlayerActivity extends Activity {

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpvideoplayer);
        videoView=findViewById(R.id.gpVideo);
        videoView.setVideoPath(getIntent().getStringExtra("url"));
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(GPVideoPlayerActivity.this, R.string.Playback_failed,Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        videoView.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoView.isPlaying()){
            videoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }

    /***
     * 启动播放视频
     * @param   context context
     * @param  url url
     * ***/
    public static  void startActivity(Context context, String url){
        Intent intent=new Intent(context,GPVideoPlayerActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }
}
