package com.example.administrator.guiprototype;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.jjoe64.graphview.GraphView;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesRangeColumn;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Mapping;
import com.anychart.anychart.Set;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.telephony.SmsManager;

/**
 * Created by Administrator on 4/29/2018.
 */

public class BloodPressure extends AppCompatActivity implements View.OnClickListener , PopupMenu.OnMenuItemClickListener{

    ImageButton settingbutton, alertbutton, phonebutton, homebutton;
    AzureDatabase database;
    RadioButton todayAverage;
    String graphTitle;
    int graphType;
    Button bp,gl,hr;
    EditText myAge,aveBp, normalBp, aveAnal;
    double highAve;
    double highTotal;
    double lowAve;
    double lowTotal;
    static int userAge = 0;
    String analysis = "";
    private String phoneNumber, message;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    String normal = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewbloodpressure);

        highAve = 0;
        highTotal = 0;
        lowAve = 0;
        lowTotal = 0;
        database = new AzureDatabase();
        todayAverage = (RadioButton)findViewById(R.id.todavercheckbox);
        settingbutton = (ImageButton) findViewById(R.id.settings);
        alertbutton = (ImageButton) findViewById(R.id.alerts);
        phonebutton = (ImageButton) findViewById(R.id.phone);
        homebutton = (ImageButton) findViewById(R.id.homebutton);
        bp = (Button) findViewById(R.id.bpviewbutton);
        gl = (Button) findViewById(R.id.glviewbutton);
        hr = (Button) findViewById(R.id.hrviewbutton);
        myAge = (EditText) findViewById(R.id.ageofuser);
        aveBp = (EditText) findViewById(R.id.aveBp);
        normalBp = (EditText) findViewById(R.id.normalBp);
        aveAnal = (EditText) findViewById(R.id.aveAnal);
        settingbutton.setOnClickListener(this);
        alertbutton.setOnClickListener(this);
        phonebutton.setOnClickListener(this);
        homebutton.setOnClickListener(this);
        bp.setOnClickListener(this);
        gl.setOnClickListener(this);
        hr.setOnClickListener(this);
        todayAverage.setChecked(true);
        graphTitle = "Last 24 Hours";
        userAge = database.getUserAge();
        normal = database.getNormalBp(userAge);
        myAge.setText(userAge+"", TextView.BufferType.EDITABLE);
        normalBp.setText(normal, TextView.BufferType.EDITABLE);

        graphType = 1;
        populateBloodPressure();

    }

    public void autoMessage()
    {
        if(database.getLastCall(database.getPrimaryCaregiver()) == 0){
            this.message = "Title: BP " + graphTitle + "\n" + "My Average: " + highAve + "/" + lowAve + "\n" + "Normal: " + normal + "\n" + "Analysis: " + analysis;
            sendSMSMessage(phoneNumber,message);
        }

    }

    public void populateBloodPressure()
    {
        AnyChartView anyChartView = findViewById(R.id.bloodpressuregraph);
        Cartesian cartesian = AnyChart.cartesian();
        cartesian.setTitle(graphTitle);
        List<DataEntry> data = getData(graphType);
        Set set = new Set(data);
        Mapping londonData = set.mapAs("{ x: 'x', high: 'High', low: 'Low' }");
        CartesianSeriesRangeColumn columnLondon = cartesian.rangeColumn(londonData);
        columnLondon.setName("BloodPressure(mm Hg)");
        cartesian.setXAxis(true);
        cartesian.setYAxis(true);
        cartesian.setLegend(true);
        cartesian.setYGrid(true).setYMinorGrid(true);
        cartesian.getTooltip().setTitleFormat("{%SeriesName} ({%x})");
        anyChartView.setChart(cartesian);
        aveBp.setText(highAve + "\n" + lowAve + " mm Hg", TextView.BufferType.EDITABLE);
        aveAnal.setText(analysis,TextView.BufferType.EDITABLE);
        this.phoneNumber = database.getCarePhoneNumber(database.getPrimaryCaregiver());
        autoMessage();
    }



    private List<DataEntry> getData(int type) {
        List<DataEntry> data = new ArrayList<>();
        List<String> todaysAverage = null;
        if (type == 1)
            todaysAverage = database.getTodaysAverage();
        else if(type == 2)
            todaysAverage = database.getWeeklyAverage();
        else if(type == 3)
            todaysAverage = database.getMonthlyAverage();
        highAve = 0;
        highTotal = 0;
        lowAve = 0;
        lowTotal = 0;
        for(String averages:todaysAverage){
            String[] sqldateString = averages.split(",");
            //Log.e("Values", averages + " = " + sqldateString[2] + " : " + sqldateString[0] + " / " + sqldateString[1]);
            double high = Double.parseDouble(sqldateString[0]);//high
            double low = Double.parseDouble(sqldateString[1]);//low
            highTotal = highTotal + high;
            lowTotal = lowTotal + low;
            data.add(new CustomDataEntry(sqldateString[2],high,low));
        }
        highAve = Math.round(highTotal / todaysAverage.size());
        lowAve = Math.round(lowTotal / todaysAverage.size());
        analysis = database.getBpAnalysis(highAve,lowAve);
        return data;
    }



    private class CustomDataEntry extends DataEntry {
        public CustomDataEntry(String x, Number high, Number low) {
            setValue("x", x);
            setValue("high", high);
            setValue("low", low);
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.monvcheckbox:
                if (checked)
                    graphTitle = "Last 12 Months";
                graphType = 3;
                populateBloodPressure();
                Toast.makeText(this,"Monthly",Toast.LENGTH_SHORT).show();
                break;
            case R.id.todavercheckbox:
                if (checked)
                    graphTitle = "Last 24 Hours";
                graphType = 1;
                populateBloodPressure();
                Toast.makeText(this,"Today",Toast.LENGTH_SHORT).show();
                break;
            case R.id.weekavecheckbox:
                if (checked)
                    graphTitle = "Last 4 Weeks";
                graphType = 2;
                populateBloodPressure();
                Toast.makeText(this,"Weekly",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.settings:
                showPopup(view);
                break;
            case R.id.phone:

                this.message = "Title: BP " + graphTitle + "\n" + "My Average: " + highAve + "/" + lowAve + "\n" + "Normal: " + normal + "\n" + "Analysis: " + analysis;
                sendSMSMessage(phoneNumber,message);
                Log.e("fds", "fds");
                break;
            case R.id.homebutton:
                startActivity(new Intent(this, BloodPressure.class));
                break;
            case R.id.alerts:
                if(Login.isRunning == 1) {//was running
                    Login.cDownTimer.start();
                    Toast.makeText(this,"Stopped Receiving data",Toast.LENGTH_SHORT).show();
                    Login.isRunning = 0;
                }else if(Login.isRunning == 0){
                    Login.cDownTimer.cancel();
                    Toast.makeText(this,"Started Receiving data",Toast.LENGTH_SHORT).show();
                    Login.isRunning = 1;
                }
                break;
            case R.id.bpviewbutton:
                startActivity(new Intent(this, BloodPressure.class));

                break;
            case R.id.glviewbutton:
                startActivity(new Intent(this, BloodGlucose.class));
                break;
            case R.id.hrviewbutton:
                startActivity(new Intent(this, HeartRate.class));
                break;



        }
    }


    public void showPopup(View view)
    {
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.add_user:
                startActivity(new Intent(this, User.class));
                return true;
            case R.id.add_device:
                startActivity(new Intent(this, AddDevice.class));
                return true;
            case R.id.view_caregiver:
                startActivity(new Intent(this, ViewCaregiver.class));
                return true;
            case R.id.sign_out:
                Toast.makeText(this,"Signout Successful",Toast.LENGTH_SHORT).show();
                Login.timer.cancel();
                Login.timer.purge();
                startActivity(new Intent(this, Login.class));
                return true;
            default:
                return false;

        }
    }

    public void sendSMSMessage(String phoneNumber, String message)
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }








}
