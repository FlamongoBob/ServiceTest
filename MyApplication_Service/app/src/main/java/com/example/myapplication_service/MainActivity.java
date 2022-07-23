package com.example.myapplication_service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;

import com.example.myapplication_service.Message.MessageSystem;
import com.example.myapplication_service.Message.MessageType;

public class MainActivity extends AppCompatActivity {
    LocalService mService;
    boolean mBound = false;
    boolean mServiceConnencted =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(view -> {
            if(mBound) {
                mService.connect2Pepper("ADMIN", "ADMIN", this);
            }
        });

        Button btnSendTest = findViewById(R.id.btnSendTest);
        btnSendTest.setOnClickListener(view -> {
            MessageSystem msgSystem = new MessageSystem("");
            msgSystem.setType(MessageType.Test);

            mService.sendTestMessage(msgSystem);
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        try{
            // Bind to LocalService
            Intent intent = new Intent(this, LocalService.class);
            //this.startService(intent);
            this.bindService(intent, connection, Context.BIND_AUTO_CREATE);

        }catch(Exception ex){
            String err ="";
            err =ex.getMessage();
            err +="";

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

    @Override
    protected void onUserLeaveHint(){

    }

    /** Defines callbacks for service binding, passed to bindService() */
    private  ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };



}