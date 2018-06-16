package com.example.rudra.toothless_bluetooth_override;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    String address =null,name_dev=null;
    BluetoothAdapter blue=null;
    BluetoothSocket socket=null;
    Set<BluetoothDevice> paired;
    Button cd,send,exit,override,back,right,left,up;
    EditText send_text;
    static int override_active=0;
    static final UUID HC_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cd=findViewById(R.id.cd);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bluetooth_connect_device();
                }catch(Exception e){}
            }
        });
        exit=findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                override_active=0;
                //if(blue.isEnabled()){
                   // blue.disable();
                //}
            }
        });
        override=findViewById(R.id.override);
        override.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                override_active=1;
            }
        });
        //override.performClick();
        Log.d("RICKY",Integer.toString(override_active));
    }
    private void bluetooth_connect_device() throws Exception
    {
        try
        {
            blue = BluetoothAdapter.getDefaultAdapter();
            address = blue.getAddress();
            paired = blue.getBondedDevices();
            if (paired.size()>0)
            {
                for(BluetoothDevice bt : paired)
                {

                    address=bt.getAddress().toString();name_dev = bt.getName().toString();
                    Toast.makeText(getApplicationContext(),"Connected to "+name_dev, Toast.LENGTH_LONG).show();

                }
            }

        }
        catch(Exception e){}
        blue = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
        BluetoothDevice dispositivo = blue.getRemoteDevice(address);//connects to the device's address and checks if it's available
        socket = dispositivo.createInsecureRfcommSocketToServiceRecord(HC_UUID);//create a RFCOMM (SPP) connection
        socket.connect();

    }
    private void send_string(String text){
        try{
            if(socket!=null){
                socket.getOutputStream().write(text.getBytes());
            }
        }catch(Exception e){
        }
    }
}
