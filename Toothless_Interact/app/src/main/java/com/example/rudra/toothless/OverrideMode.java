package com.example.rudra.toothless;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class OverrideMode extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.override_mode);
        Button override_exit=findViewById(R.id.override_exit);
        override_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(OverrideMode.this,MainActivity.class);
                startActivity(i);
            }
        });


    }
}
