package com.gmail.processorcooler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.sql.Time;

import static android.widget.Toast.LENGTH_SHORT;

public class TimeDialog extends AppCompatDialogFragment {
    int position = 0;
    public interface SingleChoiceListener{
        void onPositiveButtonClicked(String[] list,int position);
        void onNegativeButtonClicked();
    }

    SingleChoiceListener mListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            mListener = (SingleChoiceListener) context;
            System.out.println("On Attached called");
        }catch (Exception e){
            throw new ClassCastException(getActivity().toString()+"SingleChoiceListener must be implemented");
        }
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] list = getActivity().getResources().getStringArray(R.array.choose_time);

        System.out.println("Oncreate called");
        builder.setTitle("Select Period")
                .setSingleChoiceItems(list, position, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    position = i;
                    System.out.println("position is: " + i);
                }
                })//end singleChoiceItems
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mListener.onPositiveButtonClicked(list,position);
                        for(int j=0;j<list.length;j++) {
                            System.out.println("Pos" + j + "= " + list[j]);
                        }
                    }
                })// end positive button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mListener.onNegativeButtonClicked();
                        System.out.println("Cancel listener called");
                    }
                }); //end Negative Button
        return builder.create();
    }
}
