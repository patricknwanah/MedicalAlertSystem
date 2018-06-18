package com.example.administrator.guiprototype;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by Administrator on 12/4/2017.
 */

public class ViewCaregiver extends AppCompatActivity implements View.OnClickListener , PopupMenu.OnMenuItemClickListener{
    ImageButton settingbutton, alertbutton, phonebutton, homebutton;
    Button delete, setprimary;
    ListView listView;
    TextView primeCareTxt;
    AzureDatabase database;
    String careGiverLoginName = "";
    List<String>listofcg;
    ArrayAdapter adapter;
    int selectedItemPosition;
    private View rowView;
    private PopupWindow POPUP_WINDOW_SCORE = null;
    PopupWindow popupWindow;
    CheckBox cBp, cBg, cHr;
    int vBp;
    int vBG;
    int vHr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcaregiver);
        listView = (ListView) findViewById(R.id.listofUsers);
        database = new AzureDatabase();
        settingbutton = (ImageButton) findViewById(R.id.settings);
        alertbutton = (ImageButton) findViewById(R.id.alerts);
        phonebutton = (ImageButton) findViewById(R.id.phone);
        homebutton = (ImageButton) findViewById(R.id.homebutton);
        delete = (Button) findViewById(R.id.delete);
        setprimary = (Button) findViewById(R.id.setprimary);
        primeCareTxt = (TextView) findViewById(R.id.primeCareTxt);
        settingbutton.setOnClickListener(this);
        alertbutton.setOnClickListener(this);
        phonebutton.setOnClickListener(this);
        homebutton.setOnClickListener(this);
        delete.setOnClickListener(this);
        setprimary.setOnClickListener(this);
        populateUserCaregivers();
        setPrimary();
        vBp = 0;
        vBG = 0;
        vHr = 0;

        listView.setAdapter(new MyListAdaper(this, R.layout.list_item, listofcg));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                careGiverLoginName = listofcg.get(position);
                selectedItemPosition = position;
                Log.e("CareGiverName",careGiverLoginName);
                rowView = view;
            }
        });
    }

    private void setPrimary()
    {
        primeCareTxt.setText("Primary Caregiver " + database.getPrimaryCaregiver());
    }




    private void setPrimarytonull()
    {
        primeCareTxt.setText("Primary Caregiver ");
    }


    private void populateUserCaregivers()
    {
        listofcg = database.getUserCareGivers();
        adapter = new MyListAdaper(this, android.R.layout.simple_list_item_1, listofcg);
        listView.setAdapter(adapter);

    }


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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete:
                if (careGiverLoginName == null || careGiverLoginName.length() == 0 || careGiverLoginName.isEmpty())
                    Toast.makeText(this, "Must select a caregiver", Toast.LENGTH_SHORT).show();
                else {
                    if (database.deleteCaregiver(careGiverLoginName)) {
                        Toast.makeText(this, "Deletion Complete", Toast.LENGTH_SHORT).show();
                        populateUserCaregivers();
                        setPrimary();
                    } else {
                        Toast.makeText(this, "Error deleting caregiver", Toast.LENGTH_SHORT).show();
                    }
                }
                careGiverLoginName = "";
                break;
            case R.id.setprimary:
                if (careGiverLoginName == null || careGiverLoginName.length() == 0 || careGiverLoginName.isEmpty())
                    Toast.makeText(this, "Must select a caregiver", Toast.LENGTH_SHORT).show();
                else {
                    if (database.setPrimary(careGiverLoginName)) {
                        setPrimary();
                        Toast.makeText(this, careGiverLoginName + " is now your primary caregiver", Toast.LENGTH_SHORT).show();
                    } else {
                        setPrimarytonull();
                        Toast.makeText(this, careGiverLoginName + " is no longer your primary caregiver", Toast.LENGTH_SHORT).show();

                    }
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
                    Login.cDownTimer.start();
                    Toast.makeText(this,"Stopped Receiving data",Toast.LENGTH_SHORT).show();
                    Login.isRunning = 0;
                }else if(Login.isRunning == 0){
                    Login.cDownTimer.cancel();
                    Toast.makeText(this,"Started Receiving data",Toast.LENGTH_SHORT).show();
                    Login.isRunning = 1;
                }
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
                Toast.makeText(this,"Baby",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ViewCaregiver.class));
                return true;
            case R.id.sign_out:
                Toast.makeText(this,"Signout Successful",Toast.LENGTH_SHORT).show();
                Login.timer.cancel();
                Login.timer.cancel();
                Login.timer.purge();
                startActivity(new Intent(this, Login.class));
                return true;
            default:
                return false;

        }
    }

    private class MyListAdaper extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;
        private MyListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Button was clicked for list item " + position, Toast.LENGTH_SHORT).show();
                    careGiverLoginName = listofcg.get(position);
                    selectedItemPosition = position;
                    Log.e("CareGiverName",careGiverLoginName);
                    callPopup(database.getCareGiverPermissions(careGiverLoginName));
                }
            });
            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }



    public class ViewHolder {

        TextView title;
        Button button;
    }

    private void callPopup(String perm) {

        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.popup, null);

        popupWindow=new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        //Name = (EditText) popupView.findViewById(R.id.edtimageName);
        String[] perms = perm.split(",");
        vBp = Integer.parseInt(perms[0]);
        vBG = Integer.parseInt(perms[1]);
        vHr = Integer.parseInt(perms[2]);

        cBp = (CheckBox) popupView.findViewById(R.id.viewBpC);
        cBg = (CheckBox) popupView.findViewById(R.id.viewBGLC);
        cHr = (CheckBox) popupView.findViewById(R.id.viewHrC);

        if (vBp == 1)
            cBp.setChecked(true);
        if (vBG == 1)
            cBg.setChecked(true);
        if (vHr == 1)
            cHr.setChecked(true);

        ((Button) popupView.findViewById(R.id.saveBtn))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        //Toast.makeText(getApplicationContext(), Name.getText().toString(),Toast.LENGTH_LONG).show();
                        if(cBp.isChecked())
                            vBp = 1;
                        if(cBg.isChecked())
                            vBG = 1;
                        if(cHr.isChecked())
                            vHr = 1;
                        if(!cBp.isChecked())
                            vBp = 0;
                        if(!cBg.isChecked())
                            vBG = 0;
                        if(!cHr.isChecked())
                            vHr = 0;

                        database.savePermissions( careGiverLoginName ,vBp+","+vBG+","+vHr);

                        popupWindow.dismiss();

                    }

                });

        ((Button) popupView.findViewById(R.id.cancelBtn))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {

                        popupWindow.dismiss();
                    }
                });

    }
}
