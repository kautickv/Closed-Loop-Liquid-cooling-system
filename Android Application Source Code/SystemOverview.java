package com.gmail.processorcooler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.concurrent.TimeUnit;

public class SystemOverview extends AppCompatActivity {
    //private EditText tempCPU; variable for displaying the microprocessor's internal temperature
    private ImageButton flowRateButton;
    private ImageButton pumpButton;
    private ImageButton tankButton;
    private ImageButton valveButton;
    private ImageButton ambientButton;
    private Button cpuTemperature;
    private static ToggleButton powerButton; //changed it to static variable so that the state of the button does not change between activities
    private SetTemperatureDialog setTemperatureDialog;
    private  TankDialog tankDialog;
    private PumpDialog pumpDialog;
    private  ValveDialog valveDialog;
    private  AmbientTempDialog ambientTempDialog;
    private userCommandHandler commandHandler;
    controller_dialog controllerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_overview); //set what string to display in the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*TabLayout tabLayout = TabLayout findViewById(R.id.tabs);
        setSupportActionBar();
*/
        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        // Create Dialog classes
        setTemperatureDialog = new SetTemperatureDialog();  // temperature dialog
        tankDialog = new TankDialog();
        pumpDialog = new PumpDialog();
        valveDialog = new ValveDialog();
        ambientTempDialog = new AmbientTempDialog();
        controllerDialog = new controller_dialog();


        TextView lay = findViewById(R.id.layout).findViewById(R.id.temptDisplay);   // try to get the textview to dsplay temp


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        break;
                    case R.id.chart:
                        Intent intentC =  new Intent(SystemOverview.this,TemperatureTracking.class);
                                startActivity(intentC);
                        break;
                    case R.id.more:
                        Intent intentM =  new Intent(SystemOverview.this,More.class);
                        startActivity(intentM);
                        break;

                }
                return false;
            }
        });

        //Set the Temperature button
        cpuTemperature = (Button) findViewById(R.id.set_Pc_tempt_button);
        cpuTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTemperatureDialog.show(getSupportFragmentManager(),"set temperature dialog");
            }
        });

        //Power button of the system
        //cpuTemperature.setEnabled(false);
        powerButton = (ToggleButton) findViewById(R.id.power_Button);
        //Save switch state in shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        powerButton.setChecked(sharedPreferences.getBoolean("value",false));
        cpuTemperature.setEnabled(sharedPreferences.getBoolean("value",false));

        powerButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //The toggle is enable
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    cpuTemperature.setEnabled(true);
                    commandHandler = new userCommandHandler("ON","","","");
                } else{
                    //The toggle is disable
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    cpuTemperature.setEnabled(false);
                    commandHandler = new userCommandHandler("OFF","","","");

                }
            }
        });//end onClickListener

        //Dialog box for displaying the flow rate of the water
        flowRateButton = (ImageButton) findViewById(R.id.flowRate);
        flowRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlowRateDialog flowRateDialog = new FlowRateDialog();
                flowRateDialog.show(getSupportFragmentManager(),"flow rate dialog");
            }
        });

        //Dialog box for displaying the pump
        pumpButton = (ImageButton) findViewById(R.id.pump_button);
        pumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pumpDialog.show(getSupportFragmentManager(),"pump dialog");
            }
        });

        //Dialog box for displaying the tank level
        tankButton = (ImageButton) findViewById(R.id.tank);
        tankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    tankDialog.show(getSupportFragmentManager(), "tank dialog");

            }
        });

        //Dialog box for displaying the electric valve
        valveButton = (ImageButton) findViewById(R.id.electric_valve);
        valveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valveDialog.show(getSupportFragmentManager(),"valve dialog");
            }
        });

        //Dialog box for displaying the ambient temperature sensor
        ambientButton = (ImageButton) findViewById(R.id.ambientSensor);
        ambientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambientTempDialog.show(getSupportFragmentManager(),"ambient temperature dialog");
            }
        });

        Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();
            System.out.println("System over view screen created.");
        }


        // Insert code for connection here

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.control_settings1:
                commandHandler = new userCommandHandler("on/offController","","","");
                if(controllerDialog != null){
                    controllerDialog.reset();
                }
                Toast.makeText(this, "The On/Off option has been selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.control_settings2:
                controllerDialog.show(getSupportFragmentManager(),"ambient temperature dialog");
                Toast.makeText(this, "The controlled PID has been selected", Toast.LENGTH_SHORT).show();
                return true;
            //case R.id.pid_1:
              //  Toast.makeText(this, "The Kp option has been selected", Toast.LENGTH_SHORT).show();
                //return true;
            //case R.id.pid_2:
              //  Toast.makeText(this, "The Ki option has been selected", Toast.LENGTH_SHORT).show();
                //return true;
            //case R.id.pid_3:
              //  Toast.makeText(this, "The Kd option has been selected", Toast.LENGTH_SHORT).show();
                //return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}



