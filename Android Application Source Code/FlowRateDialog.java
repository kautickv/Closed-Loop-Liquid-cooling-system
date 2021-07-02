package com.gmail.processorcooler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class FlowRateDialog extends AppCompatDialogFragment {
    private TextView flowrate_constant;
    private userCommandHandler commandHandler;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.flow_rate_dialog,null);

        flowrate_constant = view.findViewById(R.id.flowrate_constant);
        flowrate_constant.setText("Getting Flow Rate..."); // put actual value of the flow rate here
        commandHandler = new userCommandHandler("Start","","","",1, flowrate_constant);
        builder.setView(view)
                .setTitle("Flow Rate")
                .setMessage("Current flow rate of the cooling system.")
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        flowrate_constant= view.findViewById(R.id.flowrate_constant);
        return builder.create();
    }
    //insert code for displaying current flow rate in dialog box
}
