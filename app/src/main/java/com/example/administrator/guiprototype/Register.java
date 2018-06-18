package com.example.administrator.guiprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 12/3/2017.
 */

public class Register extends AppCompatActivity implements View.OnClickListener{
    Button register, cancel;
    AzureDatabase tc;
    EditText firstname, lastname, middlename, emailaddress, username, password, phonenumber;
    Spinner medicalconditions, gender;
    int day,month,year;
    DatePicker datePicker;
    String[] itemsgenders;
    List<String> listofmc = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tc = new AzureDatabase();
        gender = findViewById(R.id.gender);
        itemsgenders = new String[]{"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsgenders);
        gender.setAdapter(genderAdapter);
        datePicker = (DatePicker) findViewById(R.id.dateofbirth);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        middlename = (EditText) findViewById(R.id.middlename);
        emailaddress = (EditText) findViewById(R.id.emailaddress);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        medicalconditions = (Spinner) findViewById(R.id.medicalconditions);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        listofmc = tc.getThreadMedicalCondition();
        populateSpinnerMedicalCondition();
    }

    public String checkDigit(int number)
    {
        return number<=9?"0"+number:String.valueOf(number);
    }
    public void populateSpinnerMedicalCondition()
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listofmc);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicalconditions.setAdapter(dataAdapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                day = month = datePicker.getDayOfMonth();
                month = datePicker.getMonth();
                year = datePicker.getYear();
                String date = checkDigit(day)+"/"+checkDigit(month+1)+"/"+year;
                if(tc.getThreadCreateUser(firstname.getText().toString(),lastname.getText().toString(),middlename.getText().toString(),username.getText().toString(),password.getText().toString(),emailaddress.getText().toString(),medicalconditions, date, phonenumber.getText().toString(), itemsgenders[gender.getSelectedItemPosition()])) {
                //if(true){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Registration Successful");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                    startActivity(new Intent(this, Login.class));
                }
                break;
            case R.id.cancel:
                startActivity(new Intent(this,Login.class));
                break;

        }


    }
}
