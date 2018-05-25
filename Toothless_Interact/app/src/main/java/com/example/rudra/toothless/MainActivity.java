package com.example.rudra.toothless;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //show intro screen


        Intent i=new Intent(MainActivity.this,Intro.class);
        startActivity(i);

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
        Button button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check==true){
                   cameraDetector.start();
                }
            }
        });

    }

    @Override
    public void onImageResults(List<Face> faces, Frame frame, float v) {
        TextView textView=findViewById(R.id.smile_percent);
        if(faces.size()==0){
            textView.setText("No face");
        }else{
            Face face=faces.get(0);
            textView.setText(String.format("SMILE: %.2f",face.expressions.getSmile()));
        }
    }
}
