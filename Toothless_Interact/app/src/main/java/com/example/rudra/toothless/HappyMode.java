package com.example.rudra.toothless;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class HappyMode extends AppCompatActivity {
    TextToSpeech tts;
    int result;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.happy_mode);
        tts=new TextToSpeech(HappyMode.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    result=tts.setLanguage(Locale.US);

                }else{
                    Toast.makeText(getApplicationContext(),"Feature not supported",Toast.LENGTH_LONG).show();


                }
            }
        });
        String y;
        Random r=new Random();
        int x=r.nextInt(50);
        if(x%8==0||x%7==0||x%13==0){
            y="Wow! It's great to see you happy";
        }else if(x%6==0){
            y="What a beautiful day! Let's have some fun";
        }else{
            y="It's fuuuun O' Clock";
        }
        final String y_inner=y;
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tts.speak(y_inner,TextToSpeech.QUEUE_FLUSH,null);
            }
        },5000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tts.speak("Do you want some music or how about we hangout with friends",TextToSpeech.QUEUE_FLUSH,null);
            }
        },8000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSpeechInput();
            }
        },11000);

    }
    public void getSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String x=result.get(0);
                    
                }
                break;
        }
    }
    public boolean check_this_string(String s,String p){
        if(s.indexOf(p)<0){
            return false;
        }
        return true;
    }
}
