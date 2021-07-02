package com.gmail.processorcooler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class AmbientTempDialog extends AppCompatDialogFragment {
    private TextView ambient_text;
    private userCommandHandler commandHandler;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.ambient_dialog,null);

        ambient_text = view.findViewById(R.id.ambient_text);
        ambient_text.setText("Getting Ambient Temperature...");
        commandHandler = new userCommandHandler("getTemp","","","",1, ambient_text);

        builder.setView(view)
                .setTitle("Ambient Temperature Sensor")
                .setMessage("The current room temperature of the cooling system is,")
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }
}
