package com.example.rudra.toothless;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class SadMode extends AppCompatActivity {
    TextToSpeech tts;
    int result;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sad_mode);
        Button sad_mode_talk=findViewById(R.id.sad_mode_talk);
        sad_mode_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SadMode.this,SadMode.class);
                startActivity(i);
            }
        });
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String y;
                Random r=new Random();
                int x=r.nextInt(50);
                if(x%8==0||x%7==0||x%13==0){
                    y="We should take a walk or something";
                }else if(x%6==0){
                    y="Do you wanna have a nice and light chat";
                }else{
                    y="I think we should go out";
                }
                tts.speak(y,TextToSpeech.QUEUE_FLUSH,null);
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
                    if(check_this_string(x,"walk")||check_this_string(x,"out")){
                        tts.speak("Great. Let's get some air.", TextToSpeech.QUEUE_FLUSH,null);
                        //switch to presentation mode
                    }else if(check_this_string(x,"talk")||check_this_string(x,"chat")){
                        tts.speak("I am launching google assistant to handle this well",TextToSpeech.QUEUE_FLUSH,null);
                        final Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(Intent.ACTION_VOICE_COMMAND).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        },3000);
                    }else if(check_this_string(x,"stop")||check_this_string(x,"exit")){
                        tts.speak("Okay,Whatever you wish. Take Care",TextToSpeech.QUEUE_FLUSH,null);
                        Intent i=new Intent(SadMode.this,MainActivity.class);
                        startActivity(i);
                    }
                    else{
                        tts.speak("I am not sure, what you want, but remember everything will be fine",TextToSpeech.QUEUE_FLUSH,null);
                        Intent i=new Intent(SadMode.this,SadMode.class);
                        startActivity(i);
                    }
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

    @Override
    protected void onStop() {
        super.onStop();
        tts.shutdown();
    }
}
