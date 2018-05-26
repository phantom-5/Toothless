package com.example.rudra.toothless;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Intro extends Activity {
    static boolean control=true;
    TextToSpeech tts;

    int result;
    TextView you_chose;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_screen);
        tts=new TextToSpeech(Intro.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    result=tts.setLanguage(Locale.US);

                }else{
                    Toast.makeText(getApplicationContext(),"Feature not supported",Toast.LENGTH_LONG).show();


                }
            }
        });
        TextView hello_text = findViewById(R.id.hello_text);
        final TextView iam_text = findViewById(R.id.iam_text);
        hello_text.setText("Hello!");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iam_text.setText("I Am Toothless");
                TTS(iam_text);
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                iam_text.startAnimation(fadeIn);
                iam_text.startAnimation(fadeOut);
                fadeIn.setDuration(500);
                fadeOut.setDuration(1200);
                fadeOut.setStartOffset(1200 + fadeIn.getStartOffset() + 1200);

            }
        }, 2000);
    }
    public void TTS(View view){
        final View v=view;
        if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){}
        else{
            tts.speak("I am Toothless, The pet from the Future. You can tell me if" +
                    "we should go for a walk or let me analyze you",TextToSpeech.QUEUE_FLUSH,null);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   // tts.stop();
                   // tts.shutdown();
                    getSpeechInput(v);
                }
            }, 7000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(you_chose!=null) {
                        tts.speak("You chose " + you_chose.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                        if(you_chose.getText().toString().equals("analyze")){
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i=new Intent(Intro.this,MainActivity.class);
                                    i.putExtra("Pass_from_Intro",false);
                                    startActivityForResult(i,1);
                                }
                            }, 4000);
                        }
                    }else {
                        Intent i = new Intent(Intro.this, Intro.class);
                        startActivity(i);
                    }
                }
            }, 15000);
        }




    }


    public void getSpeechInput(View view) {

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
                    if(x!=null){
                        you_chose=findViewById(R.id.you_chose);
                    }
                    if(x.equals("analyse")){
                        x="analyze";
                    }
                    else if(x.equals("walk")){x="walk";}
                    else if(x.equals("override")){x="override";}
                    else{
                        Random r=new Random();
                        int y=r.nextInt(100);
                        if(y%2==0) {
                            tts.speak("Oh! Captain, My Captain. Got to give me something to start with.", TextToSpeech.QUEUE_FLUSH, null);
                        }else{
                            tts.speak("Come on, I am getting bored, lets do something", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i=new Intent(Intro.this,Intro.class);
                                startActivity(i);
                            }
                        }, 5000);

                    }
                    you_chose.setText(x);

                }
                break;
        }
    }


}