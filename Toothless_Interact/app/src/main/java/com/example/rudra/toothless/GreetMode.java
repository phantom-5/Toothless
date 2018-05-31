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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.List;

public class GreetMode extends AppCompatActivity implements Detector.ImageListener {
    CameraDetector cameraDetector;
    SurfaceView cameraPreview;
    boolean check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.greet_mode);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1888);
        else {
            check=true;

        }
        cameraPreview=findViewById(R.id.greet_mode_sview);
        cameraDetector=new CameraDetector(this,CameraDetector.CameraType.CAMERA_FRONT,cameraPreview, 1, Detector.FaceDetectorMode.LARGE_FACES);
        cameraDetector.setImageListener(this);
        cameraDetector.setDetectSmile(true);
        cameraDetector.setMaxProcessRate(20);
    }


    @Override
    public void onImageResults(List<Face> faces, Frame frame, float v) {
        if(faces.size()==0){}
        else{
            Face face=faces.get(0);
            int faceId=face.getId();
            Log.d("RUDRA",Integer.toString(faceId));
        }
    }
}
