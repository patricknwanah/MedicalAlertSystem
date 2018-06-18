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

public class BloodGlucose extends AppCompatActivity implements View.OnClickListener , PopupMenu.OnMenuItemClickListener{

    ImageButton settingbutton, alertbutton, phonebutton, homebutton;
    GraphView bggraph, ecggraph;
    BarChart bpgraph;
    AzureDatabase database;
    RadioButton todayAverage;
    String graphTitle;
    int graphType;
    Button bp,gl,hr;
    EditText myAge, aveBeforeGl, aveAfterGl, aveG;
    private String phoneNumber, message;
    static int userAge = 0;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    String normal = "";
    double average;
    double total;
    String analysis1 = "";
    String analysis2 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewglucoselevel);

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
        aveBeforeGl = (EditText) findViewById(R.id.aveBeforeGl);
        aveAfterGl = (EditText) findViewById(R.id.aveAfterGl);
        aveG = (EditText) findViewById(R.id.aveG);
        settingbutton.setOnClickListener(this);
        alertbutton.setOnClickListener(this);
        phonebutton.setOnClickListener(this);
        homebutton.setOnClickListener(this);
        todayAverage.setChecked(true);
        bp.setOnClickListener(this);
        gl.setOnClickListener(this);
        hr.setOnClickListener(this);
        graphTitle = "Last 24 Hours";
        userAge = database.getUserAge();
        normal = database.getNormalBp(userAge);
        myAge.setText(userAge+"", TextView.BufferType.EDITABLE);
        graphType = 1;
        populateBloodGlucose();
    }

    public void autoMessage()
    {
        if(database.getLastCall(database.getPrimaryCaregiver()) == 0){
            this.message = "Title: ECG " + graphTitle + "\n" + "My Average: " + average + "\n" + "Analysis(Before and After Meals): " + analysis1 + "\n" + analysis2;
            sendSMSMessage(phoneNumber,message);
        }

    }

    /*After meal*/
    public String analyze2()
    {
        String temp = "";
        if(average <= 89)
            temp = "Possibly too low, get sugar if experiencing symptoms of low blood sugar or see a doctor";
        else if(average >= 90 && average <= 126)
            temp = "Excellent";
        else if(average >= 127 && average <= 180)
            temp = "Good";
        if(average >= 181 && average <= 234)
            temp = "Acceptable";
        if(average >= 235)
            temp = "Too high, work to lower blood sugar levels";
        return temp;
    }

    /*Before meal*/
    public String analyze1()
    {
        String temp = "";
        if(average >= 60 && average <= 71)
            temp = "Poor";
        else if(average >= 72 && average <= 90)
            temp = "Possibly too low, get sugar if experiencing symptoms of low blood sugar or see a doctor";
        else if(average >= 91 && average <= 120)
            temp = "Normal range";
        else if(average >= 121 && average <= 160)
            temp = "Medium, see a doctor";
        else if(average >= 161 && average <= 240)
            temp = "Too high, work to lower blood sugar levels";
        else if(average >= 241 && average <= 300)
            temp = "Too high, a sign of out of control diabetes, see a doctor";
        else if(average >= 301)
            temp = "Very high, seek immediate medical attention";
        else if(average <= 59)
            temp = "Dangerously low, seek medical attention";
        return temp;

    }

    public void populateBloodGlucose()
    {
        AnyChartView anyChartView = findViewById(R.id.bloodglucosegraph);
        Cartesian cartesian = AnyChart.line();
        cartesian.setAnimation(true);
        cartesian.setPadding(10d, 20d, 5d, 20d);
        cartesian.getCrosshair().setEnabled(true);
        cartesian.getCrosshair().setYLabel(true).setYStroke((Stroke) null, null, null, null, null);
        cartesian.getTooltip().setPositionMode(TooltipPositionMode.POINT);
        cartesian.setTitle(graphTitle);
        cartesian.getYAxis().setTitle("Glucose Concentration(mg/dL)");
        cartesian.getXAxis().getLabels().setPadding(5d, 5d, 5d, 5d);
        Set set = new Set(getGlucoseData(graphType));
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        CartesianSeriesLine series1 = cartesian.line(series1Mapping);
        series1.setName("BloodGlucose");
        series1.getHovered().getMarkers().setEnabled(true);
        series1.getHovered().getMarkers().setType(MarkerType.CIRCLE).setSize(4d);
        series1.getTooltip().setPosition("right").setAnchor(EnumsAnchor.LEFT_CENTER).setOffsetX(5d).setOffsetY(5d);
        cartesian.getLegend().setEnabled(true);
        cartesian.getLegend().setFontSize(13d);
        cartesian.getLegend().setPadding(0d, 0d, 10d, 0d);
        anyChartView.setChart(cartesian);
        this.phoneNumber = database.getCarePhoneNumber(database.getPrimaryCaregiver());
        analysis1 = analyze1();
        analysis2 = analyze2();
        aveBeforeGl.setText( analysis1 , TextView.BufferType.EDITABLE);
        aveAfterGl.setText( analysis2, TextView.BufferType.EDITABLE);
        aveG.setText( average+" mg/dL", TextView.BufferType.EDITABLE);
        autoMessage();
    }

    private List<DataEntry> getGlucoseData(int type) {
        average = 0;
        total = 0;
        List<DataEntry> data = new ArrayList<>();
        List<String> todaysAverage = null;
        if (type == 1)
            todaysAverage = database.getGlucoseTodaysAverage();
        else if(type == 2)
            todaysAverage = database.getGlucoseWeeklyAverage();
        else if(type == 3)
            todaysAverage = database.getGlucoseMonthlyAverage();
        Log.e("Size ", todaysAverage.size() +"");
        for(String averages:todaysAverage){
            String[] sqldateString = averages.split(",");
            //Log.e("Values", sqldateString[1] + " = " + sqldateString[0]);
            double gl = Double.parseDouble(sqldateString[0]);//gl
            total = total + gl;
            data.add(new newDataEntry(sqldateString[1],gl));
        }
        average = Math.round(total / todaysAverage.size());
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
                populateBloodGlucose();
                Toast.makeText(this,"Monthly",Toast.LENGTH_SHORT).show();
                break;
            case R.id.todavercheckbox:
                if (checked)
                    graphTitle = "Last 24 Hours";
                graphType = 1;
                populateBloodGlucose();
                Toast.makeText(this,"Today",Toast.LENGTH_SHORT).show();
                break;
            case R.id.weekavecheckbox:
                if (checked)
                    graphTitle = "Last 4 Weeks";
                graphType = 2;
                populateBloodGlucose();
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
                this.message = "Title: ECG " + graphTitle + "\n" + "My Average: " + average + "\n" + "Analysis(Before and After Meals): " + analysis1 + "\n" + analysis2;
                sendSMSMessage(phoneNumber,message);
                Log.e("fds", "fds");
                break;
            case R.id.homebutton:
                startActivity(new Intent(this, BloodPressure.class));
                break;
            case R.id.alerts:
                if(Login.isRunning == 1) {//was running
                    Toast.makeText(this,"Stopped Receiving data",Toast.LENGTH_SHORT).show();
                    Login.cDownTimer.start();
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
