package com.example.rudra.toothless;
import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;


public class PresentationMode extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_mode);
        VideoView videoView=findViewById(R.id.VideoView);
        String videPath="android.resource://com.example.rudra.toothless/"+R.raw.baymax3;
        Uri uri=Uri.parse(videPath);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

    }
}
