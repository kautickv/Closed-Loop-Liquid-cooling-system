package com.gmail.processorcooler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.concurrent.TimeUnit;

public class SetTemperatureDialog extends AppCompatDialogFragment {
    private TextView previous_temperature;
    private EditText setTemp;
    private userCommandHandler commandHandler;
    private String Prev_temp = "";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.set_temperature_dialog,null);

        setTemp = view.findViewById(R.id.set_Pc_tempt);
        previous_temperature = view.findViewById(R.id.previous_tempt);
        previous_temperature.setText(Prev_temp);

        builder.setView(view)
                .setTitle("CPU's Temperature")
                .setMessage("Write the desired temperature in Celsius")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String temp =  setTemp.getText().toString();
                        previous_temperature.setText("Getting Values...");
                        commandHandler = new userCommandHandler("setTemp", temp, "","");
                        System.out.println("Temperature of " + temp+" sent to Arduino.");
                        Prev_temp = temp;
                        try {
                            TimeUnit.SECONDS.sleep(1);
                            System.out.println("Command handler created.");
                            //commandHandler = new userCommandHandler("Start", "", "", "");
                        }catch (InterruptedException e){
                            System.out.println("Cannot Get data from Arduino");
                        }
                    }
                });


        return builder.create();
    }

}
