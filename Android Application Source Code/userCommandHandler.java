package com.gmail.processorcooler;

// imports
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class userCommandHandler implements Runnable{

    //    userInterface myUI;
   private client myClient;
    private String theCommand;
    private String val1;
    private String val2;
    private String val3;
    private int pos;
    private TextView GUI;




    //    public userCommandHandler(userInterface myUI, client myClient) {
//        this.myUI = myUI;
//        this.myClient = myClient;
//    }
    public userCommandHandler(String cmd, String val1, String val2, String val3, int pos, TextView GUI){
        this.theCommand = cmd;
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
        this.pos = pos;
        this.GUI = GUI;
        Thread myCommandThread = new Thread(this);
        myCommandThread.start();

    }

    public userCommandHandler(String cmd, String val1, String val2, String val3){
        this.theCommand = cmd;
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
        Thread myCommandThread = new Thread(this);
        myCommandThread.start();

    }

   /* public void handleUserCommand(String theCommand) {

        this.theCommand = theCommand;
        Thread myCommandThread = new Thread(this);
        myCommandThread.start();

    }*/

    @Override
    public void run(){
        theCommand = theCommand.trim();
        // create a client
        // myClient = new client(80,theCommand);
        switch (theCommand) {
            case "pumpStatus":
//                // Toggle the status of the pump.
                try{
                    myClient = new client(80,"PPPPP", GUI);     // return ON/OFF
                    myClient.connectToServer();
                    myClient.sendMessageToServer(1);   // response will be in format OK|OFF  or OK/ON
                    myClient.disconnectFromServer();

                }catch(Exception e){    // send code to Arduino
                        GUI.setText("An error occurred. Cannot get the status of pump.");
                }
                break;

            case "valveStatus":
//                // close the valve
                try{
                    myClient = new client(80,"VVVVV");
                    myClient.connectToServer();
                    myClient.sendMessageToServer("OK");
                    myClient.disconnectFromServer();

                }catch(Exception e){
                    System.out.println("Cannot get Valve status");
                    System.out.println("Error: " + e);
                    //Toast.makeText(mainThread, "A problem occurred. Please check your internet connection and try again later.", Toast.LENGTH_SHORT).show();
                }
                break;

            case "on/offController":
//               // Select the on/off controller as a cooling method.
                try{

                      myClient = new client(80, "CCC|OOO|CCC");
                      myClient.connectToServer();
                      myClient.sendMessageToServer("OK");
                      myClient.disconnectFromServer();


                }catch(Exception e){
                    System.out.println("Cannot use On/Off controller");
                    System.out.println("Error: " + e);
                }
                break;
            case "Start": //Request Sensor values
                try{
                        myClient = new client(80, "GGGGG", GUI);
                        myClient.connectToServer();
                        myClient.sendMessageToServer(pos);
                        myClient.disconnectFromServer();

                }catch(Exception e){
                    System.out.println("Cannot request sensor values");
                    System.out.println("Error: " + e);
                    GUI.setText("An error occurred. Try again later");
                }

                break;
            case "getTemp": //Request Sensor values
                try{
                    myClient = new client(80, "GGGTTTT", GUI);
                    myClient.connectToServer();
                    myClient.sendMessageToServer(pos);
                    myClient.disconnectFromServer();

                }catch(Exception e){
                    System.out.println("Cannot request Temperature sensor values");
                    System.out.println("Error: " + e);
                    GUI.setText("An error occurred. Try again later");
                }

                break;
            case "setTemp": //Set the tempearture value
                try{
                    String code = "ST|" + val1 + "|SSSTTT";
                    myClient = new client(80,code);
                    myClient.connectToServer();
                    myClient.sendMessageToServer("OK");
                    myClient.disconnectFromServer();
                    myClient = null;

                }catch(Exception e){
                    System.out.println("Cannot set temperature.");
                    System.out.println("Error: " + e);
                }

                break;
            case "PIDController":
                // Set the PID controller as the cooling method
                try{
                    // CCC|III|kp|ki|kd|CCC
                    String code = "CCC|III|" + val1+"|"+val2+"|"+val3+"|"+"CCC";
                    myClient = new client(80,code);
                    myClient.connectToServer();
                    myClient.sendMessageToServer("OK");
                    myClient.disconnectFromServer();

                }catch(Exception e){
                    System.out.println("Cannot set PID Controller");
                    System.out.println("Error: " + e);
                }
                break;
            case "ON":                                               // turn the controller ON
                // Set the PID controller as the cooling method
                try{

                    myClient = new client(80,"XXX|ON|XXX");
                    myClient.connectToServer();
                    myClient.sendMessageToServer("OK");
                    myClient.disconnectFromServer();

                }catch(Exception e){
                    System.out.println("Cannot turn the system ON");
                    System.out.println("Error: " + e);
                }
                break;
            case "OFF":
                // Set the PID controller as the cooling method
                try{

                    myClient = new client(80,"XXX|OFF|XXX");    // turn the system OFF
                    myClient.connectToServer();
                    myClient.sendMessageToServer("OK");
                    myClient.disconnectFromServer();

                }catch(Exception e){
                    System.out.println("Cannot turn the system OFF");
                    System.out.println("Error: " + e);
                }
                break;
            case "Database":
                // getDataFromServer();
                break;
            default:
                // myClient.sendMessageToUI(theCommand + " command is not recognized!");
                break;
        }
    }
}
