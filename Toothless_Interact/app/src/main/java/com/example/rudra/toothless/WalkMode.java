package com.example.rudra.toothless;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.robertsimoes.quoteable.QuotePackage;
import com.robertsimoes.quoteable.Quoteable;
import java.util.Locale;
import java.util.Random;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class WalkMode extends Activity implements Quoteable.ResponseReadyListener,ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {

    TextToSpeech tts;
    int result_tts;
    String x_tts;
    Button walk_mode_abort;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    TextView walk_mode_dist,walk_mode_lat,walk_mode_long;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walk_mode);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        /**
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in millisecond **/

        walk_mode_abort=findViewById(R.id.walk_mode_abort);
        walk_mode_dist=findViewById(R.id.walk_mode_dist);
        walk_mode_lat=findViewById(R.id.walk_mode_lat);
        walk_mode_long=findViewById(R.id.walk_mode_long);

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
        cd = new CountDownTimer(3601000, 1000) {
            @Override
            public void onTick(long l) {
                count_down.setText(Integer.toString((int) (l / 1000)));
                if((int)(l/1000)%5==0){
                    mGoogleApiClient.connect();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mGoogleApiClient.disconnect();
                        }
                    }, 2000);

                }

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
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(WalkMode.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(WalkMode.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(WalkMode.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        111);

            }
        } else {
            // Permission has already been granted
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            } else {
                //If everything went fine lets get latitude and longitude
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                walk_mode_lat.setText(Double.toString(currentLatitude));
                walk_mode_long.setText(Double.toString(currentLongitude));
                Log.d("RICKY","lat= "+currentLatitude);
                Log.d("RICKY","long="+currentLongitude);

            }
        }

    }
    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
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
    }


}
