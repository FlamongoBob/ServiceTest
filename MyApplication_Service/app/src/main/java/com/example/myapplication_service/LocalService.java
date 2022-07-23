package com.example.myapplication_service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.myapplication_service.Client.Client;
import com.example.myapplication_service.Message.MessageSystem;
import com.example.myapplication_service.crypto.Encryption;

import java.util.Random;

public class LocalService extends Service {
    Encryption e = new Encryption();
    Client client;
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    /**
     * Class used for the client Binder.
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * method for clients
     */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }

    public void connect2Pepper(String strUsername, String strPassword, MainActivity mainActivity) {

        if (strUsername != null) {
            if (!strUsername.isEmpty()) {
                if (strPassword != null) {
                    if (!strPassword.isEmpty()) {

                        try {
                            client = new Client("10.0.2.2"
                                    , 8888
                                    , e.encrypt(strUsername)
                                    , e.encrypt(strPassword)
                                    ,mainActivity);

                        } catch (Exception ex) {
                            String err = "";
                            err = ex.getMessage();

                            strUsername = "";
                        }
                    }
                }
            }
        }
    }

    public void sendTestMessage(MessageSystem messageSystem) {

        client.sendSysMessage(messageSystem);
    }

}

