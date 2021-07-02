package com.gmail.processorcooler;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private EditText username;
    private EditText password;
    private TextView loginInfo;
    private TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        login  = (Button)findViewById(R.id.button);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.Password);
        loginInfo = (TextView) findViewById(R.id.loginInfo);
        signUp = (TextView)findViewById(R.id.SignUpTextView);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(username.getText().toString(), password.getText().toString());
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });

    }// onCreate

    private void validate(String username, String Pass){
            if ((username.equals("Admin")) && (Pass.equals("123456"))){
                Intent intent = new Intent(MainActivity.this, SystemOverview.class);
                loginInfo.setText("Login Successful.");
                startActivity(intent);
            }else{
                 loginInfo.setText("Login failed. Please try again");

            }

    }
}
