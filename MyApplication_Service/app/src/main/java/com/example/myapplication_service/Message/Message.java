package com.example.myapplication_service.Message;

import static com.example.myapplication_service.ClientModel.ClientModel.logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public abstract class Message {
    protected MessageType type;
    private boolean value;

    private static BufferedReader bfr;
    private static OutputStreamWriter out;

    public Message(MessageType type) {
        this.type = type;
    }

    /**
     * Creates and outputstream to send the message from the sender to the receiver
     * @param socket
     */
    public void send(Socket socket) {
        try {
            //String strEncryptedMessage;
            if(out == null){

                out = new OutputStreamWriter(socket.getOutputStream());
            }

            logger.info("Sending message Plain message: " + this.toString());
            out.write(this.toString() + "\n");
            out.flush();
        } catch (IOException e) {
            logger.warning(e.toString());
        }
    }

    /**
     * The reader get all incoming messages and splits them into parts, to figure out what message types was sent to
     * the receiver, and splits the other parts of the message, for further usage of the receiving program
     * @param socket
     * @return
     */
    public static Message receive(Socket socket) {
        Message message = null;
        try {
            if(bfr == null) {
                bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }

            String msgText = bfr.readLine(); // Will wait here for complete line
            if (msgText != null) {
                logger.info("Receiving  Encrypted message: " + msgText);

                //  String strDecryptedMessage = decryption.decrypt(msgText);
                logger.info("Receiving  Decrypted message: " + msgText);

                // Parse message
                String[] parts = msgText.split("\\|");

                if (parts[0].equals(MessageType.Disconnect.toString())) {
                    message = new MessageSystem(
                            parts[1]
                    );
                    message.setType(MessageType.Disconnect);

                } else if (parts[0].equals(MessageType.Unsuccessful_LogIn.toString())) {
                    message = new MessageSystem(
                            parts[1]
                    );
                    message.setType(MessageType.Unsuccessful_LogIn);

                } else if (parts[0].equals(MessageType.Successful_LogIn.toString())) {
                    message = new MessageSystem(
                            parts[1]
                    );
                    message.setType(MessageType.Successful_LogIn);

                }else if (parts[0].equals(MessageType.LogOut.toString())) {
                    message.setType(MessageType.LogOut);

                } else if (parts[0].equals(MessageType.Patient.toString())) {
                    message = new MessageSystem(
                            parts[1]
                    );
                    message.setType(MessageType.Patient);

                } else if (parts[0].equals(MessageType.System.toString())) {
                    message = new MessageSystem(
                            parts[1]
                    );
                    message.setType(MessageType.System);

                } else if (parts[0].equals(MessageType.Test.toString())) {
                    message = new MessageSystem(
                            parts[1]
                    );
                    message.setType(MessageType.Test);

                }
            }
        } catch (IOException e) {
            logger.warning(e.toString());
            //Controller.ClientIsConnected.set(false);
            // Controller.ServerIsStarted.set(false);
        }
        return message;
    }

    /**
     * Returns the type of the Message
     */
    public MessageType getType() {
        return this.type;
    }
    public void setType(MessageType msgType){
        this.type = msgType;
    }


    public boolean getBoolean() {
        return value;
    }

    public void setBoolean(boolean value) {
        this.value = value;
    }
}

