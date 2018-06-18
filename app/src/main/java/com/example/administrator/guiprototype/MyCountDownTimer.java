package com.example.administrator.guiprototype;

import android.os.CountDownTimer;

/**
 * Created by Administrator on 5/21/2018.
 */

public class MyCountDownTimer extends CountDownTimer {
    public MyCountDownTimer(long startTime, long interval) {
        super(startTime, interval);
    }

    @Override
    public void onFinish() {
        //send sms
    }

    @Override
    public void onTick(long millisUntilFinished) {
        long time = millisUntilFinished / 1000;
    }
}
