package com.example.administrator.guiprototype;

/**
 * Created by Administrator on 5/21/2018.
 */
import android.util.Log;

import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
public class AutomaticDateRetrieval extends TimerTask{


    public void addBp(){
        AzureDatabase database = new AzureDatabase();;
        int highmin = 120;
        int highmax = 140;
        int lowmin = 80;
        int lowmax = 90;
        int high = ThreadLocalRandom.current().nextInt(highmin, highmax + 1);
        int low = ThreadLocalRandom.current().nextInt(lowmin, lowmax + 1);
        database.addbp(Login.thisUserLoginName,high,low);
    }

    public void addBg(){
        AzureDatabase database = new AzureDatabase();;
        int min = 90;
        int max = 130;
        int num = ThreadLocalRandom.current().nextInt(min, max + 1);
        database.addbg(Login.thisUserLoginName,num);
    }

    public void addHr(){
        AzureDatabase database = new AzureDatabase();;
        int min = 65;
        int max = 90;
        int num = ThreadLocalRandom.current().nextInt(min, max + 1);
        database.addHr(Login.thisUserLoginName,num);
    }

    @Override
    public void run() {
        addBp();
        addBg();
        addHr();
    }
}
