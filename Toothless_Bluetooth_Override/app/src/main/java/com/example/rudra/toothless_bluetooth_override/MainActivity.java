package com.example.rudra.toothless_bluetooth_override;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    String address =null,name_dev=null;
    BluetoothAdapter blue=null;
    BluetoothSocket socket=null;
    Set<BluetoothDevice> paired;
    static final UUID HC_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            bluetooth_connect_device();
        }catch(Exception e){}

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
                    Toast.makeText(getApplicationContext(),"Connected to Toothless", Toast.LENGTH_LONG).show();

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
