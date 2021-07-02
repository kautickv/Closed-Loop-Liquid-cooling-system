package com.gmail.processorcooler;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class More extends AppCompatActivity {

//    private EditText cName; //change name
//    private Button changeNameDialog; //change name button
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        //Top Bar with username
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Navigation Bar

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        Intent intentH =  new Intent(More.this,SystemOverview.class);
                        startActivity(intentH);
                        break;
                    case R.id.chart:
                        Intent intentC =  new Intent(More.this,TemperatureTracking.class);
                        startActivity(intentC);
                        break;
                    case R.id.more:

                        break;

                }
                return false;
            }
        });
/*
        // Dialog Box - changing name
        changeNameDialog = (Button) findViewById(R.id.change_name);
        changeNameDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();}
                });
*/
            }//end of on create
/*
            public void openDialog(){
                NameDialog nameDialog = new NameDialog();
                nameDialog.show(getSupportFragmentManager(),"name dialog");
            }

 */
        }//end of class




