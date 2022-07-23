package com.example.myapplication_service.Client;

import com.example.myapplication_service.ClientModel.ClientModel;
import com.example.myapplication_service.MainActivity;
import com.example.myapplication_service.Message.MessageSystem;

public class Client {
    ClientModel cModel;
    /**
     * Constructor for the client class.
     * Creates a new clientModel, and sends it the Port, the Name, the Password.
     * @param strIP
     * @param intPort
     * @param strUserName
     * @param strPassword
     */

    public Client(String strIP, int intPort, String strUserName, String strPassword, MainActivity mainActivity) {

            cModel = new ClientModel();
            cModel.StartServer(strUserName, strPassword,strIP,intPort, mainActivity);

            //cModel.connect(strIP, intPort, strName, strPassword);
            //cModel.newestMessage.addListener((o, oldValue, newValue) -> view.textArea.appendText(newValue));



    }

    /**
     * Function to start the disconnect process from the server. hashem
     *
     */
    public void disconnect(){
        cModel.disconnect();
    }



    /**
     * Sending System Messages
     * @param messageSystem
     *
     */
    public void sendSysMessage(MessageSystem messageSystem) {

        cModel.sendMessage(messageSystem);
    }
}