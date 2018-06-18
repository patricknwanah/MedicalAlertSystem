package com.example.administrator.guiprototype;

import android.util.Log;
import android.widget.Spinner;

import com.anychart.anychart.HighLowDataEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/12/2018.
 */

public class AzureDatabase {
    private AzureConnect database;
    private List<String> listofmc;
    private boolean canLogin;
    boolean didRegister;
    private List<String> bloodPressures;
    private List<String> bloodGlucose;
    private List<String> heartRate;
    private List<String> careGivers;
    private List<String> userCareGivers;
    private List<String> pendingRequests;
    private boolean isRequestSuccessful = false;
    private boolean isSuccessful = false;
    private String primaryCaregiver = "";
    List<String> todaysAverage;
    List<String> weeklyAverage;
    List<String> monthlyAverage;
    List<String> GlucoseTodaysAverage;
    List<String> GlucoseWeeklyAverage;
    List<String> GlucoseMonthlyAverage;
    List<String> HeartRateTodaysAverage;
    List<String> HeartRateWeeklyAverage;
    List<String> HeartRateMonthlyAverage;
    private int userAge = 0;
    private String normalBp;
    private String bpAnalysis;
    private String carePhoneNumber;
    private int lastcall = 11;
    private String permissions = "";

    public AzureDatabase()
    {
        database = new AzureConnect();
        listofmc = new ArrayList<>();
        canLogin = false;
        didRegister = false;
    }

    public String getBpAnalysis(final double high, final double low)
    {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Your code goes here
                    if (database.connect()) {
                        //Log.d("Connection",username.getText().toString());
                        bpAnalysis = database.getBpAnalysis(high,low);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return bpAnalysis;
    }

    public String getCareGiverPermissions(final String caregiver)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Your code goes here
                    if (database.connect()) {
                        //Log.d("Connection",username.getText().toString());
                        permissions = database.getCareGiverPermissions(caregiver);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return permissions;
    }


    public int getLastCall(final String caregiver)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Your code goes here
                    if (database.connect()) {
                        //Log.d("Connection",username.getText().toString());
                        lastcall = database.getLastCall(caregiver);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return lastcall;
    }

    public String getCarePhoneNumber(final String name){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Your code goes here
                    if (database.connect()) {
                        //Log.d("Connection",username.getText().toString());
                        carePhoneNumber = database.getCarePhoneNumber(name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return carePhoneNumber;

    }


    public String getNormalBp(final int age)
    {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Your code goes here
                    if (database.connect()) {
                        //Log.d("Connection",username.getText().toString());
                        normalBp = database.getNormalBp(age);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return normalBp;
    }

    public void savePermissions(final String caregiver,final String s) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Your code goes here
                    if (database.connect()) {
                        //Log.d("Connection",username.getText().toString());
                        database.savePermissions(caregiver,s);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
    }

    public int getUserAge()
    {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Your code goes here
                    if (database.connect()) {
                        //Log.d("Connection",username.getText().toString());
                        userAge = database.getAge();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return userAge;
    }

    public boolean login(final String username, final String password)
    {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Your code goes here
                    if (database.connect()) {
                        //Log.d("Connection",username.getText().toString());
                        canLogin = database.login(username, password);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return canLogin;
    }

    public List<String> getThreadMedicalCondition()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect())
                        listofmc = database.getMedicationConditions();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return listofmc;
    }

    public boolean getThreadCreateUser(final String firstname, final String lastname, final String middlename, final String loginName, final String loginPassword, final String emailAddress, final Spinner medicalconditions, final String date, final String phonenumber, final String gender)
    {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(database.connect())
                        didRegister = database.createUser(firstname,lastname,middlename,loginName,loginPassword,emailAddress,medicalconditions.getSelectedItemPosition()+1,date, phonenumber, gender);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return didRegister;
    }

    public List<String> getBloodPressures()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect())
                        bloodPressures = database.getBloodPressures();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.bloodPressures;
    }

    public List<String> getHeartRates()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect())
                        heartRate = database.getHeartRates();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.heartRate;
    }
    public List<String> getBloodGlucoses()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect())
                        bloodGlucose = database.getBloodGlucoses();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.bloodGlucose;
    }

    public List<String> getAllCareGivers()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect())
                        careGivers = database.getAllCareGivers();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.careGivers;
    }

    public boolean sendCareGiverRequest(final String careGiverLoginID)
    {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Your code goes here
                    if (database.connect()) {
                        //Log.d("Connection",username.getText().toString());
                        isRequestSuccessful = database.sendCareGiverRequest(Login.thisUserLoginName,careGiverLoginID);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return isRequestSuccessful;
    }

    public List<String> getPendingRequests()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    if (database.connect())
                        pendingRequests = database.getPendingRequests();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.pendingRequests;
    }

    public List<String> getUserCareGivers() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect())
                        userCareGivers = database.getUserCareGivers();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.userCareGivers;
    }

    public boolean deleteCaregiver(final String careGiverLoginName) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Your code goes here
                    if (database.connect()) {
                        isSuccessful = database.deleteCaregiver(careGiverLoginName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return isSuccessful;

    }

    public boolean setPrimary(final String careGiverLoginName) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Your code goes here
                    if (database.connect()) {
                        isSuccessful = database.setPrimary(careGiverLoginName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return isSuccessful;
    }

    public String getPrimaryCaregiver()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect())
                        primaryCaregiver = database.getPrimaryCaregiver();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return primaryCaregiver;
    }

    public List<String> getTodaysAverage() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect())
                        todaysAverage = database.getTodaysAverage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.todaysAverage;
    }

    public List<String> getGlucoseTodaysAverage() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect())
                        GlucoseTodaysAverage = database.getGlucoseTodaysAverage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.GlucoseTodaysAverage;
    }


    public List<String> getHeartRateTodaysAverage() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect())
                        HeartRateTodaysAverage = database.getHeartRateTodaysAverage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.HeartRateTodaysAverage;
    }


    public List<String> getGlucoseWeeklyAverage() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect()) {
                        GlucoseWeeklyAverage = new ArrayList<>();
                        for(int count = 0; count < 4; count++) {
                            List<String> templist = database.getGlucoseWeeklyAverage(count+1);
                            double ave = 0;
                            double total = 0;
                            String week = "";
                            if (templist != null && templist.size() > 0) {
                                for (int i = 0; i < templist.size(); i++) {
                                    String[] sqldateString = templist.get(i).split(",");
                                    //Log.e("Values", averages + " = " + sqldateString[2] + " : " + sqldateString[0] + " / " + sqldateString[1]);
                                    double high = Integer.parseInt(sqldateString[0]);//high

                                    total = total + high;
                                    week = sqldateString[1];
                                }
                                ave = total / templist.size();
                                String string = ave + "," + week;
                                Log.e("WeeklyAvg",string);
                                GlucoseWeeklyAverage.add(string);
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.GlucoseWeeklyAverage;
    }

    public List<String> getHeartRateWeeklyAverage() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect()) {
                        HeartRateWeeklyAverage = new ArrayList<>();
                        for(int count = 0; count < 4; count++) {
                            List<String> templist = database.getHeartRateWeeklyAverage(count+1);
                            double ave = 0;
                            double total = 0;
                            String week = "";
                            if (templist != null && templist.size() > 0) {
                                for (int i = 0; i < templist.size(); i++) {
                                    String[] sqldateString = templist.get(i).split(",");
                                    //Log.e("Values", averages + " = " + sqldateString[2] + " : " + sqldateString[0] + " / " + sqldateString[1]);
                                    double high = Integer.parseInt(sqldateString[0]);//high

                                    total = total + high;
                                    week = sqldateString[1];
                                }
                                ave = total / templist.size();
                                String string = ave + "," + week;
                                Log.e("WeeklyAvg",string);
                                HeartRateWeeklyAverage.add(string);
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.HeartRateWeeklyAverage;
    }

    public List<String> getHeartRateMonthlyAverage() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect()) {
                        HeartRateMonthlyAverage = new ArrayList<>();
                        for(int count = 0; count < 12; count++) {
                            List<String> templist = database.getHeartRateMonthlyAverage(count+1);
                            double ave = 0;
                            double total = 0;
                            String week = "";
                            if (templist != null && templist.size() > 0) {
                                for (int i = 0; i < templist.size(); i++) {
                                    String[] sqldateString = templist.get(i).split(",");
                                    //Log.e("Values", averages + " = " + sqldateString[2] + " : " + sqldateString[0] + " / " + sqldateString[1]);
                                    double high = Integer.parseInt(sqldateString[0]);//high

                                    total = total + high;
                                    week = sqldateString[1];
                                }
                                ave = total / templist.size();
                                String string = ave + "," + week;
                                Log.e("MonthlyAvg",string);
                                HeartRateMonthlyAverage.add(string);
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.HeartRateMonthlyAverage;
    }


    public List<String> getGlucoseMonthlyAverage() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect()) {
                        GlucoseMonthlyAverage = new ArrayList<>();
                        for(int count = 0; count < 12; count++) {
                            List<String> templist = database.getGlucoseMonthlyAverage(count+1);
                            double ave = 0;
                            double total = 0;
                            String week = "";
                            if (templist != null && templist.size() > 0) {
                                for (int i = 0; i < templist.size(); i++) {
                                    String[] sqldateString = templist.get(i).split(",");
                                    //Log.e("Values", averages + " = " + sqldateString[2] + " : " + sqldateString[0] + " / " + sqldateString[1]);
                                    double high = Integer.parseInt(sqldateString[0]);//high

                                    total = total + high;
                                    week = sqldateString[1];
                                }
                                ave = total / templist.size();
                                String string = ave + "," + week;
                                Log.e("MonthlyAvg",string);
                                GlucoseMonthlyAverage.add(string);
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.GlucoseMonthlyAverage;
    }




    public List<String> getWeeklyAverage() {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect()) {
                        weeklyAverage = new ArrayList<>();
                        for(int count = 0; count < 4; count++) {
                            List<String> templist = database.getWeeklyAverage(count+1);
                            double highAve = 0;
                            double highTotal = 0;
                            double lowAve = 0;
                            double lowTotal = 0;
                            String week = "";
                            if (templist != null && templist.size() > 0) {
                                for (int i = 0; i < templist.size(); i++) {
                                    String[] sqldateString = templist.get(i).split(",");
                                    //Log.e("Values", averages + " = " + sqldateString[2] + " : " + sqldateString[0] + " / " + sqldateString[1]);
                                    double high = Integer.parseInt(sqldateString[0]);//high
                                    double low = Integer.parseInt(sqldateString[1]);//low

                                    highTotal = highTotal + high;
                                    lowTotal = lowTotal + low;
                                    week = sqldateString[2];
                                }
                                highAve = highTotal / templist.size();
                                lowAve = lowTotal / templist.size();
                                String string = highAve + "," + lowAve + "," + week;
                                //Log.e("Connection",string);
                                weeklyAverage.add(string);
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.weeklyAverage;
    }

    public List<String> getMonthlyAverage() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect()) {
                        monthlyAverage = new ArrayList<>();
                        for(int count = 0; count < 12; count++) {
                            List<String> templist = database.getMonthlyAverage(count+1);
                            double highAve = 0;
                            double highTotal = 0;
                            double lowAve = 0;
                            double lowTotal = 0;
                            String month = "";
                            if (templist != null && templist.size() > 0) {
                                for (int i = 0; i < templist.size(); i++) {
                                    String[] sqldateString = templist.get(i).split(",");
                                    //Log.e("Values", averages + " = " + sqldateString[2] + " : " + sqldateString[0] + " / " + sqldateString[1]);
                                    double high = Integer.parseInt(sqldateString[0]);//high
                                    double low = Integer.parseInt(sqldateString[1]);//low

                                    highTotal = highTotal + high;
                                    lowTotal = lowTotal + low;
                                    month = sqldateString[2];
                                }
                                highAve = highTotal / templist.size();
                                lowAve = lowTotal / templist.size();
                                String string = highAve + "," + lowAve + "," + month;
                                //Log.e("Connection",string);
                                monthlyAverage.add(string);
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
        return this.monthlyAverage;

    }


    public void addbp(final String thisUserLoginName, final int high, final int low) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect())
                        database.addbp(thisUserLoginName,high,low);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
    }

    public void addbg(final String thisUserLoginName, final int num) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect())
                        database.addbg(thisUserLoginName,num);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
    }

    public void addHr(final String thisUserLoginName, final int num) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (database.connect())
                        database.addHr(thisUserLoginName,num);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.close();
    }


}
