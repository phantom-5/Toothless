package com.example.rudra.toothless;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.WrapperListAdapter;

import com.robertsimoes.quoteable.QuotePackage;
import com.robertsimoes.quoteable.Quoteable;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Random;

public class WalkMode extends Activity implements Quoteable.ResponseReadyListener{

    TextToSpeech tts;
    int result_tts;
    String x_tts;
    Button walk_mode_abort;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walk_mode);
        walk_mode_abort=findViewById(R.id.walk_mode_abort);
        walk_mode_abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i=new Intent(WalkMode.this,MainActivity.class);
                        startActivity(i);
                    }
                }, 10000);
            }
        });
        final Quoteable quoteable = new Quoteable(this,"Jim Rohn","Life is like the changing seasons.");
        final Handler handler = new Handler();
        quoteable.request();
        tts=new TextToSpeech(WalkMode.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    result_tts=tts.setLanguage(Locale.US);
                    Log.d("RICKY","configured");

                }else{
                    Toast.makeText(getApplicationContext(),"Feature not supported",Toast.LENGTH_LONG).show();
                }
            }
        });
        final TextView count_down = findViewById(R.id.count_down);
        CountDownTimer cd;
        cd = new CountDownTimer(61000, 1000) {
            @Override
            public void onTick(long l) {
                count_down.setText(Integer.toString((int) (l / 1000)));
            }

            @Override
            public void onFinish() {
                Intent i=new Intent(WalkMode.this,WalkMode.class);
                startActivity(i);
            }
        };
        cd.start();
    }
    @Override
    public void onQuoteResponseReady(QuotePackage response) {
        Log.d("RICKY",response.getQuote());
        Log.d("RICKY",response.getAuthor());
        final String q_author=response.getAuthor();
        final String q_quote=response.getQuote();
        Random r=new Random();
        int y_tts=r.nextInt(50);
        String x_tts;
        if(y_tts%8==0||y_tts%7==0||y_tts%13==0){
            x_tts="Did you know what "+q_author+" once said ";
        }else if(y_tts%6==0){
            x_tts=q_author+" says ";
        }else{
            x_tts="It was said by "+q_author+" that ";
        }
        x_tts+=q_quote;
        Log.d("RICKY",x_tts);
        final String x_tts_inner=x_tts;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!walk_mode_abort.isPressed()){
                tts.speak(x_tts_inner,TextToSpeech.QUEUE_FLUSH,null);}
            }
        }, 5000);

    }

    @Override
    public void onQuoteResponseFailed(QuotePackage defaultResponse) {
        Log.d("RICKY",defaultResponse.getQuote());
        Log.d("RICKY",defaultResponse.getAuthor());
        tts.speak("I am not connected to internet, Good for you, Not so much for me",TextToSpeech.QUEUE_FLUSH,null);
    }


}
