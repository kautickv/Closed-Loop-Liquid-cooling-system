package com.gmail.processorcooler;

//imports
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

public class serverMessageHandler {

    client myClient;
    private static final char TERMINATOR = '*'; // UTF-8 encoding of 0xFF

    public serverMessageHandler(client client) {
        this.myClient = client;
    }

    public void handleServerMessage(InputStream input, String expectedResp) throws IOException{

        StringBuilder message = new StringBuilder();
        boolean terminatorFound = false;
        // get byte by byte until the terminator is found.
        while (false == terminatorFound) {
            if (input != null) {
                // Get a byte from the server
                char serverByte = (char) input.read();
                if (serverByte == TERMINATOR) {
                    terminatorFound = true;
                } else {
                    message.append(serverByte);
                }
            }
        }

        System.out.println("The message is(NOT GUI): " + message.toString());


    }

    public void handleServerMessage(InputStream input, int pos, TextView GUI) throws IOException{

        StringBuilder message = new StringBuilder();
        boolean terminatorFound = false;
        // get byte by byte until the terminator is found.
        while (false == terminatorFound) {
            if (input != null) {
                // Get a byte from the server
                char serverByte = (char) input.read();
                if (serverByte == TERMINATOR) {
                    terminatorFound = true;
                } else {
                    message.append(serverByte);
                }
            }
        }
        if(GUI != null) {
            String[] result = message.toString().split(Pattern.quote("|"));     // result will be in the form of XXX|YYY|ZZZ
                System.out.println("The message is: " + message.toString());
                GUI.setText(result[pos]);

        }


    }
}
