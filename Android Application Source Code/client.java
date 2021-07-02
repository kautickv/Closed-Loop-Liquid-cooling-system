package com.gmail.processorcooler;

//imports
import android.os.Handler;
import android.widget.TextView;

import java.io.*;
import java.net.*;
import java.util.concurrent.CountDownLatch;


public class client implements Runnable {
    InputStream input;
    OutputStream output;
    Socket clientSocket = null;
    serverMessageHandler myServerMessageHandler;
    userInterface myUI;
    String theCommand;
    private static final char TERMINATOR = 0xFFFD; // UTF-8 encoding of 0xFF
    int portNumber = 80, backlog = 100;
    boolean isConnected = false;
    boolean stopThread = false;
    String ip = "192.168.0.171";
    TextView GUI;

    //    public client(int portNumber, int backlog, userInterface myUI) {
//        this.portNumber = portNumber;
//        this.backlog = backlog;
//        this.myUI = myUI;
//        this.myServerMessageHandler = new serverMessageHandler(this);
//    }
    public client(int portNumber, String cmd, TextView GUI) {
        this.portNumber = portNumber;
        this.theCommand = cmd;
        this.myServerMessageHandler = new serverMessageHandler(this);
       this.GUI = GUI;
    }

    public client(int portNumber, String cmd) {
        this.portNumber = portNumber;
        this.theCommand = cmd;
        this.myServerMessageHandler = new serverMessageHandler(this);
    }

    public void connectToServer () throws Exception
    {
            clientSocket = new Socket(ip, portNumber);
            input = clientSocket.getInputStream();
            output = clientSocket.getOutputStream();
            isConnected = true;
        /*} catch (IOException e) {
            System.out.println("Error connecting to the server.");
            System.exit(1);
        }*/
        isConnected = true;
    }

    public void quitClient()
    {
        disconnectFromServer();
    }

    public void disconnectFromServer()
    {
        // close all objects
        try {
            isConnected = false;
            input = null;
            output = null;
            myServerMessageHandler = null;
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("cannot close stream/client socket; exiting.");
            System.exit(1);
        }
    }

    public void sendMessageToServer(String resp) throws IOException
    {
        // sends message byte by byte to the server. The Message ends with the TERMINATOR
        if (output != null) {
            for (int i = 0; i < theCommand.length(); i++) {
                output.write(theCommand.charAt(i));
            }
            // Finally, terminate it.
            output.write(TERMINATOR);
            output.flush();
        }
        myServerMessageHandler.handleServerMessage(input, resp);
        disconnectFromServer();
        // myUI.update("Message sent and Disconnected!");
    }

    public void sendMessageToServer(int pos) throws IOException
    {
        // sends message byte by byte to the server. The Message ends with the TERMINATOR
        if (output != null) {
            for (int i = 0; i < theCommand.length(); i++) {
                output.write(theCommand.charAt(i));
            }
            // Finally, terminate it.
            output.write(TERMINATOR);
            output.flush();
        }
        myServerMessageHandler.handleServerMessage(input, pos, GUI);
        disconnectFromServer();
        // myUI.update("Message sent and Disconnected!");
    }

    public synchronized boolean isConnected()
    {
        return isConnected;
    }

    public void setPort(int portNumber) {
        this.portNumber = portNumber;
    }

    public int getPort() {
        return this.portNumber;
    }

    public void sendMessageToUI(String theString) {
        myUI.update(theString);
    }


    @Override
    public void run() {


    }
}
