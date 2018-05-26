package com.example.rudra.toothless;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import org.w3c.dom.Text;

public class WalkMode extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walk_mode);
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
}
