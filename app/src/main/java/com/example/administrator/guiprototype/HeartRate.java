package com.example.administrator.guiprototype;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.anychart.anychart.CartesianSeriesLine;
import com.anychart.anychart.EnumsAnchor;
import com.anychart.anychart.MarkerType;
import com.anychart.anychart.Stroke;
import com.anychart.anychart.TooltipPositionMode;
import com.anychart.anychart.ValueDataEntry;
import com.github.mikephil.charting.charts.BarChart;
import com.jjoe64.graphview.GraphView;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Mapping;
import com.anychart.anychart.Set;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by Administrator on 4/29/2018.
 */

public class HeartRate extends AppCompatActivity implements View.OnClickListener , PopupMenu.OnMenuItemClickListener{

    ImageButton settingbutton, alertbutton, phonebutton, homebutton;
    AzureDatabase database;
    RadioButton todayAverage;
    String graphTitle;
    int graphType;
    Button bp,gl,hr;
    EditText myAge,aveHr, aveAnal;
    double average;
    double total;
    static int userAge = 0;
    String analysis = "";
    String heartRateScore = "";
    private String phoneNumber, message;
    String normal = "";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewheartrate);

        database = new AzureDatabase();
        average = 0;
        total = 0;
        todayAverage = (RadioButton)findViewById(R.id.todavercheckbox);
        settingbutton = (ImageButton) findViewById(R.id.settings);
        alertbutton = (ImageButton) findViewById(R.id.alerts);
        phonebutton = (ImageButton) findViewById(R.id.phone);
        homebutton = (ImageButton) findViewById(R.id.homebutton);
        myAge = (EditText) findViewById(R.id.ageofuser);
        aveHr = (EditText) findViewById(R.id.aveHr);
        aveAnal = (EditText) findViewById(R.id.aveAnal);
        settingbutton.setOnClickListener(this);
        alertbutton.setOnClickListener(this);
        phonebutton.setOnClickListener(this);
        homebutton.setOnClickListener(this);
        bp = (Button) findViewById(R.id.bpviewbutton);
        gl = (Button) findViewById(R.id.glviewbutton);
        hr = (Button) findViewById(R.id.hrviewbutton);
        todayAverage.setChecked(true);
        bp.setOnClickListener(this);
        gl.setOnClickListener(this);
        hr.setOnClickListener(this);
        graphTitle = "Last 24 Hours";

        userAge = database.getUserAge();
        myAge.setText(userAge+"", TextView.BufferType.EDITABLE);
        graphType = 1;
        populateECG();
    }

    public void autoMessage()
    {
        if(database.getLastCall(database.getPrimaryCaregiver()) == 0){
            this.message = "Title: ECG " + graphTitle + "\n" + "My Average: " + average + "\n" + "Analysis: " + analysis;
            sendSMSMessage(phoneNumber,message);
        }

    }


    public void populateECG()
    {
        AnyChartView anyChartView = findViewById(R.id.ECGgraph);
        Cartesian cartesian = AnyChart.line();
        cartesian.setAnimation(true);
        cartesian.setPadding(10d, 20d, 5d, 20d);
        cartesian.getCrosshair().setEnabled(true);
        cartesian.getCrosshair().setYLabel(true).setYStroke((Stroke) null, null, null, null, null);
        cartesian.getTooltip().setPositionMode(TooltipPositionMode.POINT);
        cartesian.setTitle(graphTitle);
        cartesian.getYAxis().setTitle("Heart rate(BPM)");
        cartesian.getXAxis().getLabels().setPadding(5d, 5d, 5d, 5d);
        Set set = new Set(getHeartRateData(graphType));
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        CartesianSeriesLine series1 = cartesian.line(series1Mapping);
        series1.setName("ECG");
        series1.getHovered().getMarkers().setEnabled(true);
        series1.getHovered().getMarkers().setType(MarkerType.CIRCLE).setSize(4d);
        series1.getTooltip().setPosition("right").setAnchor(EnumsAnchor.LEFT_CENTER).setOffsetX(5d).setOffsetY(5d);
        cartesian.getLegend().setEnabled(true);
        cartesian.getLegend().setFontSize(13d);
        cartesian.getLegend().setPadding(0d, 0d, 10d, 0d);
        anyChartView.setChart(cartesian);
        aveHr.setText(average+" BPM", TextView.BufferType.EDITABLE);
        aveAnal.setText(analysis,TextView.BufferType.EDITABLE);
        this.phoneNumber = database.getCarePhoneNumber(database.getPrimaryCaregiver());
        autoMessage();
    }



    private List<DataEntry> getHeartRateData(int type) {
        average = 0;
        total = 0;
        List<DataEntry> data = new ArrayList<>();
        List<String> todaysAverage = null;
        if (type == 1)
            todaysAverage = database.getHeartRateTodaysAverage();
        else if(type == 2)
            todaysAverage = database.getHeartRateWeeklyAverage();
        else if(type == 3)
            todaysAverage = database.getHeartRateMonthlyAverage();
        Log.e("Size ", todaysAverage.size() +"");
        for(String averages:todaysAverage){
            String[] sqldateString = averages.split(",");
            //Log.e("Values", sqldateString[1] + " = " + sqldateString[0]);
            double hr = Double.parseDouble(sqldateString[0]);//hr
            total = total + hr;
            data.add(new newDataEntry(sqldateString[1],hr));
        }
        average = Math.round(total / todaysAverage.size());
        analysis = getHeartRateScore(average,userAge);
        Log.e("************** ", average + " : " +analysis);
        return data;
    }



    private class newDataEntry extends ValueDataEntry {

        newDataEntry(String x, Number value) {
            super(x, value);
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
                    populateECG();
                    Toast.makeText(this,"Monthly",Toast.LENGTH_SHORT).show();
                    break;
            case R.id.todavercheckbox:
                if (checked)
                    graphTitle = "Last 24 Hours";
                    graphType = 1;
                    populateECG();
                    Toast.makeText(this,"Today",Toast.LENGTH_SHORT).show();
                    break;
            case R.id.weekavecheckbox:
                if (checked)
                    graphTitle = "Last 4 Weeks";
                    graphType = 2;
                    populateECG();
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
                this.message = "Title: ECG " + graphTitle + "\n" + "My Average: " + average + "\n" + "Analysis: " + analysis;
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

    public String getHeartRateScore(Double average, int age)
    {
        for(int i = 0; i < 6; i++) {//row
            for (int j = 0; j < 7; j++) {//column
                if(age <= 25) { // from years 1-25
                    if(average <= 55)
                        heartRateScore = "Athlete";
                    else if(average >= 56 && average <= 61)
                        heartRateScore = "Excellent";
                    else if(average >= 62 && average <= 65)
                        heartRateScore = "Good";
                    else if(average >= 66 && average <= 69)
                        heartRateScore = "Above Average";
                    else if(average >= 70 && average <= 73)
                        heartRateScore = "Average";
                    else if(average >= 74 && average <= 81)
                        heartRateScore = "Below Average";
                    else
                        heartRateScore = "Poor";
                }
                if(age >= 26 && age <= 35) { // from years 26-35
                    if(average <= 54)
                        heartRateScore = "Athlete";
                    else if(average >= 55 && average <= 61)
                        heartRateScore = "Excellent";
                    else if(average >= 62 && average <= 65)
                        heartRateScore = "Good";
                    else if(average >= 66 && average <= 70)
                        heartRateScore = "Above Average";
                    else if(average >= 71 && average <= 74)
                        heartRateScore = "Average";
                    else if(average >= 75 && average <= 81)
                        heartRateScore = "Below Average";
                    else
                        heartRateScore = "Poor";
                }
                if(age >= 36 && age <= 45) { // from years 36-45
                    if(average <= 56)
                        heartRateScore = "Athlete";
                    else if(average >= 57 && average <= 62)
                        heartRateScore = "Excellent";
                    else if(average >= 63 && average <= 66)
                        heartRateScore = "Good";
                    else if(average >= 67 && average <= 70)
                        heartRateScore = "Above Average";
                    else if(average >= 71 && average <= 75)
                        heartRateScore = "Average";
                    else if(average >= 76 && average <= 82)
                        heartRateScore = "Below Average";
                    else
                        heartRateScore = "Poor";
                }
                if(age >= 46 && age <= 55) { // from years 46-55
                    if(average <= 57)
                        heartRateScore = "Athlete";
                    else if(average >= 58 && average <= 63)
                        heartRateScore = "Excellent";
                    else if(average >= 64 && average <= 67)
                        heartRateScore = "Good";
                    else if(average >= 68 && average <= 71)
                        heartRateScore = "Above Average";
                    else if(average >= 72 && average <= 76)
                        heartRateScore = "Average";
                    else if(average >= 77 && average <= 83)
                        heartRateScore = "Below Average";
                    else
                        heartRateScore = "Poor";
                }
                if(age >= 56 && age <= 65) { // from years 56-65
                    if(average <= 56)
                        heartRateScore = "Athlete";
                    else if(average >= 57 && average <= 61)
                        heartRateScore = "Excellent";
                    else if(average >= 62 && average <= 67)
                        heartRateScore = "Good";
                    else if(average >= 68 && average <= 71)
                        heartRateScore = "Above Average";
                    else if(average >= 72 && average <= 75)
                        heartRateScore = "Average";
                    else if(average >= 76 && average <= 81)
                        heartRateScore = "Below Average";
                    else
                        heartRateScore = "Poor";
                }
                if(age >= 65) { // 65+
                    if(average <= 55)
                        heartRateScore = "Athlete";
                    else if(average >= 56 && average <= 61)
                        heartRateScore = "Excellent";
                    else if(average >= 62 && average <= 65)
                        heartRateScore = "Good";
                    else if(average >= 66 && average <= 69)
                        heartRateScore = "Above Average";
                    else if(average >= 70 && average <= 73)
                        heartRateScore = "Average";
                    else if(average >= 74 && average <= 79)
                        heartRateScore = "Below Average";
                    else
                        heartRateScore = "Poor";
                }
            }
        }
        return heartRateScore;
    }



}
