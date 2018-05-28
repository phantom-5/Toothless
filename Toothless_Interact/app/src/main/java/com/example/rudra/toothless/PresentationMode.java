package com.example.rudra.toothless;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import java.util.Random;


public class PresentationMode extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_mode);
        VideoView videoView=findViewById(R.id.VideoView);
        Random r=new Random();
        int x=r.nextInt(10);
        String videoPath;
        //"android.resource://com.example.rudra.toothless/"+R.raw.baymax3;
        if(x%2==0){
            videoPath="android.resource://com.example.rudra.toothless/"+R.raw.baymax3;
        }else{
            videoPath="android.resource://com.example.rudra.toothless/"+R.raw.baymax1;

        }
        Uri uri=Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(PresentationMode.this,PresentationMode.class);
                startActivity(i);
            }
        },10000);

    }
}
