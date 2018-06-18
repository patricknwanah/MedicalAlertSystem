package com.example.administrator.guiprototype;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Timer;
import android.os.CountDownTimer;


/**
 * Created by Administrator on 12/3/2017.
 */

public class Login extends AppCompatActivity implements View.OnClickListener{

    Button blogin;
    EditText username, password;
    TextView registerlink;
    AzureDatabase tc;
    boolean isCorrect = false;
    public static String thisUserLoginName;
    public static int[][] ratesOfHearts;
    public static String heartRateScore = "";
    public static Timer timer;
    public static int isRunning = 0;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    public static final long startTime = 30 * 10000;//30mins
    public static final long interval = 1 * 1000;//1sec
    public static CountDownTimer cDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        tc = new AzureDatabase();
        timer = new Timer();
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        blogin = (Button) findViewById(R.id.login);
        registerlink = (TextView) findViewById(R.id.tvregisterlink);
        blogin.setOnClickListener(this);
        registerlink.setOnClickListener(this);
        //ratesOfHearts = populateRestingHeartRateChart();
        //Log.e("this is my array", "arr: " + Arrays.deepToString(ratesOfHearts));
    }

//    public static String getHeartRateScore(Double average, int age)
//    {
//        for(int i = 0; i < 6; i++) {//row
//            for (int j = 0; j < 7; j++) {//column
//                if(age <= 25) { // from years 1-25
//                    if(average <= 55)
//                        heartRateScore = "Athlete";
//                    else if(average >= 56 && average <= 61)
//                        heartRateScore = "Excellent";
//                    else if(average >= 62 && average <= 65)
//                        heartRateScore = "Good";
//                    else if(average >= 66 && average <= 69)
//                        heartRateScore = "Above Average";
//                    else if(average >= 70 && average <= 73)
//                        heartRateScore = "Average";
//                    else if(average >= 74 && average <= 81)
//                        heartRateScore = "Below Average";
//                    else
//                        heartRateScore = "Poor";
//                }
//                if(age >= 26 && age <= 35) { // from years 26-35
//                    if(average <= 54)
//                        heartRateScore = "Athlete";
//                    else if(average >= 55 && average <= 61)
//                        heartRateScore = "Excellent";
//                    else if(average >= 62 && average <= 65)
//                        heartRateScore = "Good";
//                    else if(average >= 66 && average <= 70)
//                        heartRateScore = "Above Average";
//                    else if(average >= 71 && average <= 74)
//                        heartRateScore = "Average";
//                    else if(average >= 75 && average <= 81)
//                        heartRateScore = "Below Average";
//                    else
//                        heartRateScore = "Poor";
//                }
//                if(age >= 36 && age <= 45) { // from years 36-45
//                    if(average <= 56)
//                        heartRateScore = "Athlete";
//                    else if(average >= 57 && average <= 62)
//                        heartRateScore = "Excellent";
//                    else if(average >= 63 && average <= 66)
//                        heartRateScore = "Good";
//                    else if(average >= 67 && average <= 70)
//                        heartRateScore = "Above Average";
//                    else if(average >= 71 && average <= 75)
//                        heartRateScore = "Average";
//                    else if(average >= 76 && average <= 82)
//                        heartRateScore = "Below Average";
//                    else
//                        heartRateScore = "Poor";
//                }
//                if(age >= 46 && age <= 55) { // from years 46-55
//                    if(average <= 57)
//                        heartRateScore = "Athlete";
//                    else if(average >= 58 && average <= 63)
//                        heartRateScore = "Excellent";
//                    else if(average >= 64 && average <= 67)
//                        heartRateScore = "Good";
//                    else if(average >= 68 && average <= 71)
//                        heartRateScore = "Above Average";
//                    else if(average >= 72 && average <= 76)
//                        heartRateScore = "Average";
//                    else if(average >= 77 && average <= 83)
//                        heartRateScore = "Below Average";
//                    else
//                        heartRateScore = "Poor";
//                }
//                if(age >= 56 && age <= 65) { // from years 56-65
//                    if(average <= 56)
//                        heartRateScore = "Athlete";
//                    else if(average >= 57 && average <= 61)
//                        heartRateScore = "Excellent";
//                    else if(average >= 62 && average <= 67)
//                        heartRateScore = "Good";
//                    else if(average >= 68 && average <= 71)
//                        heartRateScore = "Above Average";
//                    else if(average >= 72 && average <= 75)
//                        heartRateScore = "Average";
//                    else if(average >= 76 && average <= 81)
//                        heartRateScore = "Below Average";
//                    else
//                        heartRateScore = "Poor";
//                }
//                if(age >= 65) { // 65+
//                    if(average <= 55)
//                        heartRateScore = "Athlete";
//                    else if(average >= 56 && average <= 61)
//                        heartRateScore = "Excellent";
//                    else if(average >= 62 && average <= 65)
//                        heartRateScore = "Good";
//                    else if(average >= 66 && average <= 69)
//                        heartRateScore = "Above Average";
//                    else if(average >= 70 && average <= 73)
//                        heartRateScore = "Average";
//                    else if(average >= 74 && average <= 79)
//                        heartRateScore = "Below Average";
//                    else
//                        heartRateScore = "Poor";
//                }
//            }
//        }
//        return heartRateScore;
//    }

    @Override
    public void onClick(View view) {
        //while(isCorrect == false) {
            switch (view.getId()) {
                case R.id.login:
                    isCorrect = tc.login(username.getText().toString(), password.getText().toString());
                    if(isCorrect) {
                        thisUserLoginName = username.getText().toString();
                        cDownTimer = new CountDownTimer(startTime,interval) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                sendSMSMessage("","");
                            }
                        };
                        timer.schedule(new AutomaticDateRetrieval(), 0, 30000);
                        isRunning = 1;
                        Toast.makeText(this,"Started Receiving data",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, BloodPressure.class));
                    }
                    break;
                case R.id.tvregisterlink:
                    startActivity(new Intent(this, Register.class));
                    break;
            }
        //}
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
                    smsManager.sendTextMessage("2674692604", null, thisUserLoginName + " needs to connect device", null, null);
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

//    public int[][] populateRestingHeartRateChart()
//    {
//        int[][] rates = new int[6][7];//[row][column]
//        for(int i = 0; i < 6; i++){//row
//            for(int j = 0; j < 7; j++){//column
//                //1-25
//                if(i == 0 && j == 0)
//                    rates[i][j] = 55;//athlete
//                else if(i == 0 && j == 1)
//                    rates[i][j] = 61;//excellent
//                else if(i == 0 && j == 2)
//                    rates[i][j] = 65;//good
//                else if(i == 0 && j == 3)
//                    rates[i][j] = 69;//above average
//                else if(i == 0 && j == 4)
//                    rates[i][j] = 73;//average
//                else if(i == 0 && j == 5)
//                    rates[i][j] = 81;//below average
//                else if(i == 0 && j == 6)
//                    rates[i][j] = 82;//poor
//
//                    //26-35
//                else if(i == 1 && j == 0)
//                    rates[i][j] = 54;//athlete
//                else if(i == 1 && j == 1)
//                    rates[i][j] = 61;//excellent
//                else if(i == 1 && j == 2)
//                    rates[i][j] = 65;//good
//                else if(i == 1 && j == 3)
//                    rates[i][j] = 70;//above average
//                else if(i == 1 && j == 4)
//                    rates[i][j] = 74;//average
//                else if(i == 1 && j == 5)
//                    rates[i][j] = 81;//below average
//                else if(i == 1 && j == 6)
//                    rates[i][j] = 82;//poor
//
//
//                    //36-45
//                else if(i == 2 && j == 0)
//                    rates[i][j] = 56;//athlete
//                else if(i == 2 && j == 1)
//                    rates[i][j] = 62;//excellent
//                else if(i == 2 && j == 2)
//                    rates[i][j] = 66;//good
//                else if(i == 2 && j == 3)
//                    rates[i][j] = 70;//above average
//                else if(i == 2 && j == 4)
//                    rates[i][j] = 75;//average
//                else if(i == 2 && j == 5)
//                    rates[i][j] = 82;//below average
//                else if(i == 2 && j == 6)
//                    rates[i][j] = 83;//poor
//
//                    //46-55
//                else if(i == 3 && j == 0)
//                    rates[i][j] = 57;//athlete
//                else if(i == 3 && j == 1)
//                    rates[i][j] = 63;//excellent
//                else if(i == 3 && j == 2)
//                    rates[i][j] = 67;//good
//                else if(i == 3 && j == 3)
//                    rates[i][j] = 71;//above average
//                else if(i == 3 && j == 4)
//                    rates[i][j] = 76;//average
//                else if(i == 3 && j == 5)
//                    rates[i][j] = 83;//below average
//                else if(i == 3 && j == 6)
//                    rates[i][j] = 84;//poor
//
//
//                    //56-65
//                else if(i == 4 && j == 0)
//                    rates[i][j] = 56;//athlete
//                else if(i == 4 && j == 1)
//                    rates[i][j] = 61;//excellent
//                else if(i == 4 && j == 2)
//                    rates[i][j] = 67;//good
//                else if(i == 4 && j == 3)
//                    rates[i][j] = 71;//above average
//                else if(i == 4 && j == 4)
//                    rates[i][j] = 75;//average
//                else if(i == 4 && j == 5)
//                    rates[i][j] = 81;//below average
//                else if(i == 4 && j == 6)
//                    rates[i][j] = 82;//poor
//
//
//                    //65+
//                else if(i == 5 && j == 0)
//                    rates[i][j] = 55;//athlete
//                else if(i == 5 && j == 1)
//                    rates[i][j] = 61;//excellent
//                else if(i == 5 && j == 2)
//                    rates[i][j] = 65;//good
//                else if(i == 5 && j == 3)
//                    rates[i][j] = 69;//above average
//                else if(i == 5 && j == 4)
//                    rates[i][j] = 73;//average
//                else if(i == 5 && j == 5)
//                    rates[i][j] = 79;//below average
//                else if(i == 5 && j == 6)
//                    rates[i][j] = 80;//poor
//            }
//        }
//        return rates;
//    }
}
