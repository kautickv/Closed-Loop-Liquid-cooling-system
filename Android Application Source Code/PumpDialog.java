package com.gmail.processorcooler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class PumpDialog extends AppCompatDialogFragment {
    private TextView pump_state;
    private userCommandHandler commandHandler;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.pump_dialog,null);

        pump_state = view.findViewById(R.id.pump_state);
        pump_state.setText("Getting Pump Status...");
        commandHandler = new userCommandHandler("pumpStatus","","","",1, pump_state);

        builder.setView(view)
                .setTitle("Pump Capacity")
                .setMessage("The current state of the pump is,")
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }

}
