package com.gmail.processorcooler;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TemperatureTracking extends AppCompatActivity implements TimeDialog.SingleChoiceListener {
    private Button choose_time;
    ArrayList<Entry> x;
    ArrayList<String> y;
    private LineChart mChart;
    public String TAG = "TemperatureTracking";
    private RequestQueue mQueue;
    private TextView mTextResult;
    private String url24 = "https://api.myjson.com/bins/1fb0se";
    private String url48 = "https://api.myjson.com/bins/wbala";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperaturetracking);

        //Top Bar with username
        Toolbar toolbar = findViewById(R.id.tempt_toolbar);
        setSupportActionBar(toolbar);


        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        Intent intentH = new Intent(TemperatureTracking.this, SystemOverview.class);
                        startActivity(intentH);
                        break;
                    case R.id.chart:

                        break;
                    case R.id.more:
                        Intent intentM = new Intent(TemperatureTracking.this, More.class);
                        startActivity(intentM);
                        break;

                }
                return false;
            }
        });//end bottomNavigation

        //Dialog Box for picking time length in the graph
        choose_time = (Button)findViewById(R.id.time_change);
        choose_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeDialog = new TimeDialog();
                timeDialog.setCancelable(false);
                timeDialog.show(getSupportFragmentManager(),"Single Choice Dialog");
            }//end of onClick
        });//end of time button

        //Chart Style
        mQueue = Volley.newRequestQueue(this);
        //Line Graph
        x = new ArrayList<Entry>(); //x-axis
        y = new ArrayList<String>(); //y-axis
        mChart = (LineChart) findViewById(R.id.temperaturePlot);
        mChart.setDrawGridBackground(false);
        //mChart.setDescription("Temperature");
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);

        //Axis style
        XAxis xl = mChart.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setInverted(false);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);

        //default display 24 hour temperature Tracking chart
        drawChart(url24);
    }//end Oncreate

    @Override
    public void onPositiveButtonClicked(String[] list, int position){
        choose_time.setText(list[position]);
        if (position == 0) {
            drawChart(url24);
        }else if (position == 1) {
            drawChart(url48);
        }else if (position == 2) {
            drawChart(url48);
        }
    }
    @Override
    public void onNegativeButtonClicked(){

    }
    //To get data from Json and added to the array 24 hours
    private void drawChart(String url) {

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, "Response: " + response);

                        try {
                            JSONArray jsonArray = response.getJSONArray("measurements");
                            //String sensor = jsonObject.getString("sensor");
                            //JSONArray jsonArray = jsonObject.getJSONArray("measurements");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject temperature = jsonArray.getJSONObject(i);

                                int value =  temperature.getInt("value");
                                String date = temperature.getString("time");
                                x.add(new Entry(i, value));
                                y.add(date);
                            }
                            LineDataSet set1 = new LineDataSet(x, "CPU's Temperature in C");
                            set1.setLineWidth(1.5f);
                            set1.setCircleRadius(4f);
                            //ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                            //dataSets.add(set1);
                            LineData data = new LineData(set1);
                            mChart.setData(data);
                            mChart.invalidate();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                error.printStackTrace();
            }
        });
        mQueue.add(strReq);

    }//end of drawChart

} //end class

