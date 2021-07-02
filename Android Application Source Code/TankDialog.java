package com.gmail.processorcooler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class TankDialog extends AppCompatDialogFragment {
    private TextView water_level;
    private userCommandHandler commandHandler;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.tank_dialog,null);

        water_level = view.findViewById(R.id.water_level);
        water_level.setText("Getting Water Level...");
        commandHandler = new userCommandHandler("Start","","","",0, water_level);

        builder.setView(view)
                .setTitle("Tank Water Level")
                .setMessage("The current water level is,")
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

}
