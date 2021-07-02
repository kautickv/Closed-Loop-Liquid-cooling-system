package com.gmail.processorcooler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class controller_dialog extends AppCompatDialogFragment {
    private EditText kp_value;
    private EditText ki_value;
    private EditText kd_value;
    private String kp = "";
    private String ki = "";
    private String kd = "";
    private userCommandHandler commandHandler;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.pid_dialog,null);

        kp_value = view.findViewById(R.id.kp_value);
        ki_value = view.findViewById(R.id.ki_value);
        kd_value = view.findViewById(R.id.kd_value);

        kp_value.setText(kp);
        ki_value.setText(ki);
        kd_value.setText(kd);

        builder.setView(view)
                .setMessage("Enter desired values.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dismiss();
                    }
                })
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        kp = kp_value.getText().toString();
                        ki = ki_value.getText().toString();
                        kd = kd_value.getText().toString();
                        commandHandler = new userCommandHandler("PIDController",kp,ki,kd);
                    }
                });
        return builder.create();
    }

    public void reset(){

        kp = "";
        ki = "";
        kd = "";
    }
}
