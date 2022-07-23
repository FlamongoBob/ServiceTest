package com.example.myapplication_service.ClientModel;

import android.os.Handler;
import android.os.Looper;

import com.example.myapplication_service.MainActivity;
import com.example.myapplication_service.Message.Message;
import com.example.myapplication_service.Message.MessageLogin;
import com.example.myapplication_service.Message.MessageSystem;
import com.example.myapplication_service.Message.MessageType;
import com.example.myapplication_service.utils.NotificationUtil;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ClientModel {
    public volatile boolean isClientConnected =false;
    //Controller controller;
    public static Logger logger = Logger.getLogger("");
    //Controller controller;

    private Socket socket;
    private String strUserName;
    private String strPassword;
    Thread t;

    String strIpAddress;
    int intPort;
    static OnProcessedListener listener;
    static OnProcessedListener listener2;

    // Create some member variables for the ExecutorService
    // and for the Handler that will update the UI from the main thread
    ExecutorService mExecutor = Executors.newFixedThreadPool(2);
    Handler mHandler = new Handler(Looper.getMainLooper());
    ClientModel clientModel;

    public ClientModel() {
        clientModel = this;
        // mExecutor.execute(sndMessageThread);
    }


    // Create an interface to respond with the result after processing
    public interface OnProcessedListener {
        void onProcessed(Message msg);
    }

    public void StartServer(String strUserName, String strPassword, String strIpAddress, int intPort, MainActivity mainActivity) {

        listener = new OnProcessedListener() {
            @Override
            public void onProcessed(Message msg) {
                // Use the handler so we're not trying to update the UI from the bg thread
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Update the UI here


                         if (msg.getType().equals(MessageType.Test)) {

                            //controller.appendLogServerCon(msg.getType());

                             NotificationUtil.createChannel(mainActivity,"your_notification_channel_id");
                             NotificationUtil.setNotification(mainActivity," TITLE - Notification From CodeSpeedy"," CONTENT - CodeSpeedy App");
                        }


                        // If we're done with the ExecutorService, shut it down.
                        // Shut it down whenever everything is completed and it is not needed anymore.)
                        if (!clientModel.isClientConnected) {
                            mExecutor.shutdown();
                        }
                    }
                });
            }
        };
        /**
         *  Perform background connection to server and receive messages to send them to the Mainthread (UI) to show the  User
         */
        Runnable backgroundRunnable = new Runnable() {
            @Override
            public void run() {

                /** Use the interface to pass along the message to Mainthread / User
                 * @param msgConnect with Boolean false --> Trying to Connect, message to User
                 *                   with Boolean True -->  Connected to Server,  message to User
                 */
                // MessageSystem msgSys = new MessageSystem("Connecting");
                //msgSys.setType(MessageType.Connect);
                //msgSys.setBoolean(false);

                //listener.onProcessed(msgSys);

                try {

                    socket = new Socket(strIpAddress, intPort);

                    if (socket != null) {
                        Login(strUserName, strPassword);
                    }
                    clientModel.isClientConnected = true;

                    while (clientModel.isClientConnected) {

                        Message msg = Message.receive(socket);
                            if(msg != null) {
                                listener.onProcessed(msg);
                            }
                    }
                } catch (Exception e) {
                    // logger.warning(e.toString());

                    clientModel.isClientConnected = false;
                    String err = e.getMessage();
                    err += "";

                    // Controlle Show as Error when Login
                    MessageSystem msgSysError = new MessageSystem(" Could not Connect to the Server! \n Please try again!");
                    msgSysError.setType(MessageType.Error);
                    listener.onProcessed(msgSysError);
                }


            }
        };

        try {
            mExecutor.execute(backgroundRunnable);
        } catch (Exception e) {
            // logger.warning(e.toString());

            clientModel.isClientConnected = false;
            String err = e.getMessage();
            err += "";

            // Controlle Show as Error when Login
            MessageSystem msgSysError = new MessageSystem(" Could not Connect to the Server! \n Please try again!");
            msgSysError.setType(MessageType.Error);
            listener.onProcessed(msgSysError);
        }
    }

    private void Login(String strUserName, String strPassword) {

            MessageLogin msgLogin = new MessageLogin(strUserName, strPassword, MessageType.Login);
            msgLogin.send(socket);
    }

    public void disconnect() {
        MessageSystem msgSysDisconnect = new MessageSystem("Disconnecting");
        msgSysDisconnect.setType(MessageType.Disconnect);
        msgSysDisconnect.send(socket);

        mExecutor.shutdown();

    }

    public void responseTimer() {
        MessageSystem msgSysTimer = new MessageSystem("Response Time");
        msgSysTimer.setType(MessageType.Test);
        msgSysTimer.send(socket);
    }

    public void sendMessage(Message message) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    if (message != null) {
                        message.send(socket);
                    }

                }
            }).start();
            // sndMessageThread.interrupt();

            // mExecutor.execute(newBGSendMessage);
        } catch (Exception e) {
            String err = "";
            err = e.getMessage();
            err = "";
            // logger.warning(e.toString());
        }
    }



}
