package com.example.administrator.guiprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 12/4/2017.
 */

public class RegistrationComplete extends AppCompatActivity implements View.OnClickListener{
    Button done;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationcomplete);
        done = (Button) findViewById(R.id.button5);
        done.setOnClickListener(this);
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button5:
                startActivity(new Intent(this,Data.class));
                break;

        }

    }
}
