package com.example.rudra.toothless;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Detector.ImageListener {
    CameraDetector cameraDetector;
    SurfaceView cameraPreview;
    TextView textView;
    boolean check=false;
    int smile1=0,smile2=0,smile3=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //show intro screen
        Intent intent=getIntent();
        boolean Pass_from_Intro=intent.getBooleanExtra("Pass_from_Intro",true);
        if(Pass_from_Intro) {
            Intent i = new Intent(MainActivity.this, Intro.class);
            startActivity(i);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1888);
        else {
            check=true;

        }
        textView=findViewById(R.id.smile_percent);
        cameraPreview=findViewById(R.id.surfaceView);
        cameraDetector=new CameraDetector(this,CameraDetector.CameraType.CAMERA_FRONT,cameraPreview, 1, Detector.FaceDetectorMode.LARGE_FACES);
        cameraDetector.setImageListener(this);
        cameraDetector.setDetectSmile(true);
        cameraDetector.setMaxProcessRate(20);
        final Button button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check==true){
                   cameraDetector.start();
                }
            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button2.performClick();
            }
        }, 2000);

    }


    @Override
    public void onImageResults(List<Face> faces, Frame frame, float v) {
        TextView textView=findViewById(R.id.smile_percent);
        if(faces.size()==0){
            textView.setText("No face");
        }else{
            Face face=faces.get(0);
            textView.setText(String.format("SMILE: %.2f",face.expressions.getSmile()));
            if(face.expressions.getSmile()>=95){
                smile1++;
            }else if(face.expressions.getSmile()>=20){
                smile2++;
            }else{
                smile3++;
            }
            if(smile1>=70){
                Log.d("RICKY","You are happy");
                cameraDetector.stop();
            }
            if(smile2>=70){
                Log.d("RICKY", "Need some talks");
                cameraDetector.stop();
            }
                if (smile3 >= 70){
                    Log.d("RICKY", "You are sad");
                    cameraDetector.stop();
                    //Intent i=new Intent(MainActivity.this,WalkMode.class);
                    //startActivity(i);
            }
        }
    }
}
