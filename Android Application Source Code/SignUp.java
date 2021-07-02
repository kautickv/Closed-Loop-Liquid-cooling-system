package com.gmail.processorcooler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private Button signUpButton;
    private EditText name;
    private EditText newPass;
    private EditText confirmpass;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setVariables();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if field is empty
                if((newPass.getText().toString().isEmpty()) || (confirmpass.getText().toString().isEmpty()) || (name.getText().toString().isEmpty())){

                    //Toast.makeText(this,"Please fill in all the information", Toast.LENGTH_SHORT).show();

                }else if(checkPass(newPass.getText().toString(),confirmpass.getText().toString())){   // password is the same

                    // save credentials in database
                    ////
                }else{
                    info.setText("Password does not match. Please try again");      // password is not same

                }
            }
        });
    }

    private void setVariables(){
        // sets up the variable names for the activity
        signUpButton = (Button)findViewById(R.id.signUpButton);
        name = (EditText)findViewById(R.id.nameEditText);
        newPass = (EditText)findViewById(R.id.newPassEditText);
        confirmpass = (EditText)findViewById(R.id.confirmPassEditText);
        info = (TextView)findViewById(R.id.infoTextview);
    }

    private boolean checkPass(String pass1, String pass2){
        boolean flag = false;

        if (pass1.equals(pass2)){

            flag = true;
        }else{

            flag = false;
        }
        return flag;
    }
}
