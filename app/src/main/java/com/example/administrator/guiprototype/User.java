package com.example.administrator.guiprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 12/4/2017.
 */

public class User extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    ImageButton settingbutton, alertbutton, phonebutton, homebutton;
    Button send;
    ListView listView;
    ListView listViewPending;
    AzureDatabase database;
    String careGiverLoginName = "";
    List<String>listofcg;
    List<String>listofpendingusers;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addotherusers);
        listView = (ListView) findViewById(R.id.listofUsers);
        listViewPending = (ListView) findViewById(R.id.listofpendingrequests);
        database = new AzureDatabase();
        settingbutton = (ImageButton) findViewById(R.id.settings);
        alertbutton = (ImageButton) findViewById(R.id.alerts);
        phonebutton = (ImageButton) findViewById(R.id.phone);
        homebutton = (ImageButton) findViewById(R.id.homebutton);
        send = (Button) findViewById(R.id.send);
        settingbutton.setOnClickListener(this);
        alertbutton.setOnClickListener(this);
        phonebutton.setOnClickListener(this);
        homebutton.setOnClickListener(this);
        send.setOnClickListener(this);
        populateCaregivers();
        populatePendingRequests();

        // ListView on item selected listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                careGiverLoginName = listofcg.get(position);
                Log.e("Connection",careGiverLoginName);

            }
        });

    }

    public void showPopup(View view)
    {
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }

    private void populatePendingRequests()
    {
        listofpendingusers = database.getPendingRequests();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listofpendingusers);
        listViewPending.setAdapter(adapter);
    }


    private void populateCaregivers()
    {
        listofcg = database.getAllCareGivers();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listofcg);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send:
                if(careGiverLoginName == null || careGiverLoginName.length() == 0 || careGiverLoginName.isEmpty())
                    Toast.makeText(this, "Must select a caregiver", Toast.LENGTH_SHORT).show();
                else {
                    if (database.sendCareGiverRequest(careGiverLoginName)) {
                        Toast.makeText(this, "Request sent", Toast.LENGTH_SHORT).show();
                        populatePendingRequests();

                    } else
                        Toast.makeText(this, "Request not sent. User is either already added or pending response", Toast.LENGTH_SHORT).show();
                }
                careGiverLoginName = "";
                break;
            case R.id.homebutton:
                startActivity(new Intent(this, BloodPressure.class));
                break;
            case R.id.settings:
                showPopup(view);
                break;
            case R.id.phone:
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


        }

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


}
