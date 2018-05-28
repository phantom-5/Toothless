package com.example.rudra.toothless;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class SadMode extends AppCompatActivity {
    TextToSpeech tts;
    int result;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sad_mode);
        tts=new TextToSpeech(SadMode.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    result=tts.setLanguage(Locale.US);

                }else{
                    Toast.makeText(getApplicationContext(),"Feature not supported",Toast.LENGTH_LONG).show();


                }
            }
        });
        final Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String y;
                Random r=new Random();
                int x=r.nextInt(50);
                if(x%8==0||x%7==0||x%13==0){
                    y="It's okay to be sad, things happen";
                }else if(x%6==0){
                    y="It will pass, we will get through this, together";
                }else{
                    y="Everything will be fine, don't worry";
                }
                tts.speak(y,TextToSpeech.QUEUE_FLUSH,null);
            }
        },4000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        tts.shutdown();
    }
}
