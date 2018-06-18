package com.example.administrator.guiprototype;

/**
 * Created by Administrator on 4/10/2018.
 */
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AzureConnect {
    public String server    = "matsmain.database.windows.net";
    public String database  = "mats_user";
    public String user      = "mats@matsmain";
    public String dbpassword  = "Path1234";
//    private String connectionString = String.format("jdbc:sqlserver://%s:1433;\" +  \n" +
//            "   \"databaseName=%s;user=%s;password=%s;", server, database, user, password);//encrypt=true;trustServerCertificate=true
//    public String connectionString = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",server,database,user,password);
    public String connectionString = String.format("jdbc:jtds:sqlserver://%s:1433/%s;user=%s;password=%s;prepareSQL=2",server, database, user, dbpassword);
    private Connection connection = null;
    private  CallableStatement cstmt = null;
//    private PreparedStatement statement = null;
    private int result = 0;
    public boolean connect()
    {
        try {
            //Log.d("Connection",connectionString);
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(this.connectionString);
            Log.d("Connection","Connected");
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }



    public List<String> getMedicationConditions()
    {
        List<String> listofmc = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getMedicalConditions}");
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                listofmc.add(rs.getString(2));
            }
            return listofmc;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  boolean createUser(String firstname, String lastname, String middlename, String loginName, String loginPassword, String emailAddress, int medID, String date, String phonenumber, String gender ){
        try {
            Date data1 = null;
            cstmt = connection.prepareCall("{call DATA.AddUser(?,?,?,?,?,?,?,?,?,?,?,?)}");
            try {
                data1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cstmt.setString(1,firstname.trim());
            cstmt.setString(2,lastname.trim());
            cstmt.setString(3,middlename.trim());
            cstmt.setString(4,loginName.trim());
            cstmt.setString(5,loginPassword.trim());
            cstmt.setString(6,emailAddress.trim());
            cstmt.setInt(7,medID);
            cstmt.setInt(8,1);
            cstmt.setString(9,phonenumber);
            cstmt.setString(10,date);
            cstmt.setString(11,gender);
            cstmt.registerOutParameter(12, Types.INTEGER);
            cstmt.execute();
            result = cstmt.getInt(12);
            Log.e("Connection<----------------------->",result+"");
            if(result == 0)
                return true;
            else
                return false;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String loginName, String password)
    {

        try {
            cstmt = connection.prepareCall("{call DATA.UserLogin(?,?,?)}");
            cstmt.setString(1,loginName);
            cstmt.setString(2,password);
            cstmt.registerOutParameter(3, Types.VARCHAR);

            cstmt.executeUpdate();
            result = cstmt.getInt(3);
            Log.e("Connection<----------------------->",result+"");
            if(result == 1 || result == 2 || result == 4) {
                return true;
            }
            else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void close(){
        try {
            connection.close();
        }catch(Exception e){
            Log.e("Connection","Did not connect");
        }
    }

    public List<String> getBloodPressures()
    {
        List<String> listofbp = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getBloodPressures}");
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                listofbp.add(rs.getString(2));
            }
            return listofbp;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getHeartRates()
    {
        List<String> listofhr = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getHeartRates}");
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                listofhr.add(rs.getString(2));
            }
            return listofhr;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getBloodGlucoses()
    {
        List<String> listofbg = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getBloodGlucose}");
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                listofbg.add(rs.getString(2));
            }
            return listofbg;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAllCareGivers()
    {
        List<String> listofcg = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getCareGivers}");
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                listofcg.add(rs.getString(4));
                //Log.e("Connection",rs.getString(4));
            }
            return listofcg;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean sendCareGiverRequest(String userId, String careGiverLoginID) {
        try {
            cstmt = connection.prepareCall("{call DATA.requestCareGiver(?,?,?)}");
            cstmt.setString(1,userId);
            cstmt.setString(2,careGiverLoginID);
            cstmt.registerOutParameter(3, Types.VARCHAR);
            Log.e("Connection<----------------------->",userId + "-" + careGiverLoginID);
            cstmt.executeUpdate();
            result = cstmt.getInt(3);
            Log.e("Connection<----------------------->",result+"");
            if(result == 1) {
                return true;
            }
            else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean deleteCaregiver(String careGiverLoginID) {
        try {
            cstmt = connection.prepareCall("{call DATA.deleteCareGiver(?,?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setString(2,careGiverLoginID);
            cstmt.registerOutParameter(3, Types.VARCHAR);
            cstmt.executeUpdate();
            result = cstmt.getInt(3);
            Log.e("Connection<----------------------->",result+"");
            if(result == 1) {
                return true;
            }
            else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public List<String> getPendingRequests() {
        List<String> listofpcg = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getPendingCareGivers(?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                listofpcg.add(rs.getString(1) + " request pending..");
                //Log.e("Connection",rs.getString(4));
            }
            return listofpcg;

        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<String> getUserCareGivers() {
        List<String> listofucg = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getUserCareGivers(?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                listofucg.add(rs.getString(5));
                //Log.e("Connection",rs.getString(4));
            }
            return listofucg;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean setPrimary(String careGiverLoginName) {
        try {
            cstmt = connection.prepareCall("{call DATA.setPrimaryCareGiver(?,?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setString(2,careGiverLoginName);
            cstmt.registerOutParameter(3, Types.VARCHAR);
            cstmt.executeUpdate();
            result = cstmt.getInt(3);
            Log.e("Connection<----------------------->",result+"");
            if(result == 1) {
                return true;
            }
            else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getPrimaryCaregiver() {
        String primaryCaregiver = "";
        try{
            cstmt = connection.prepareCall("{call DATA.getPrimaryCareGiver(?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                primaryCaregiver = rs.getString(1);
                //Log.e("Connection",rs.getString(4));
            }
            return primaryCaregiver;

        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getLastCall(String caregiver)
    {
        int num = 11;
        try {
            cstmt = connection.prepareCall("{call DATA.automatedTextApproval(?,?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setString(2,caregiver);
            cstmt.registerOutParameter(3, Types.INTEGER);

            cstmt.executeUpdate();
            num = cstmt.getInt(3);
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }

    public String getCarePhoneNumber(String name){
        String number = "";
        try{
            cstmt = connection.prepareCall("{call DATA.getCareGiverPhoneNumber(?)}");
            cstmt.setString(1,name);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                number = rs.getString(1);
                //Log.e("Connection",rs.getString(4));
            }
            return number;

        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getBpAnalysis(double high, double low)
    {
        String analysis = "";
        try{
            cstmt = connection.prepareCall("{call DATA.getAnalysis(?,?)}");
            cstmt.setDouble(1,high);
            cstmt.setDouble(2,low);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                String string = rs.getString(1);;
                analysis = string;
                //Log.e("Connection",rs.getString(3));
            }
            return analysis;

        }catch (SQLException e) {
            e.printStackTrace();
            return analysis;
        }
    }

    public String getNormalBp(int age)
    {
        String hlbp = "";
        try{
            cstmt = connection.prepareCall("{call DATA.getBpNorm(?)}");
            cstmt.setInt(1,age);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                String string = rs.getString(1) + "\n" + rs.getString(2);;
                hlbp = string;
                //Log.e("Connection",rs.getString(3));
            }
            return hlbp;

        }catch (SQLException e) {
            e.printStackTrace();
            return hlbp;
        }
    }

    public String getCareGiverPermissions(String caregiver) {
        String perm = "";
        try{
            cstmt = connection.prepareCall("{call DATA.getCareGiverPermissions(?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setString(2,caregiver);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                String string = rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3);;
                perm = string;
                //Log.e("Connection",rs.getString(3));
            }
            return perm;

        }catch (SQLException e) {
            e.printStackTrace();
            return perm;
        }


    }

    public int getAge() {
        String age = "";
        try{
            cstmt = connection.prepareCall("{call Data.getUserAge(?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                age = rs.getString(3);
                //Log.e("Connection",rs.getString(3));
            }
            return Integer.parseInt(age);

        }catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<String> getTodaysAverage() {
        List<String> listofucg = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getTodaysAverage(?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setInt(2,0);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                String string = rs.getString(1) + "," +rs.getString(2) + "," + rs.getString(3) ;
                listofucg.add(string);
                //Log.e("Connection",string);
            }
            return listofucg;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getWeeklyAverage(int type) {
        List<String> listofucg = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getWeeklyAverage(?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setInt(2,type);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                String string = rs.getString(1) + "," +rs.getString(2) + "," + rs.getString(3) ;
                listofucg.add(string);
                //Log.e("Connection",string);
            }
            return listofucg;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getMonthlyAverage(int type) {
        List<String> listofucg = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getMonthlyAverage(?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setInt(2,type);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                String string = rs.getString(1) + "," +rs.getString(2) + "," + rs.getString(3) ;
                listofucg.add(string);
                //Log.e("Connection",string);
            }
            return listofucg;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getGlucoseTodaysAverage() {
        List<String> listofucg = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getGlucoseTodaysAverage(?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setInt(2,0);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                String string = rs.getString(1) + "," +rs.getString(2) ;
                listofucg.add(string);
                //Log.e("Connection",string);
            }
            return listofucg;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getGlucoseWeeklyAverage(int type) {
        List<String> listofucg = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getGlucoseWeeklyAverage(?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setInt(2,type);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                String string = rs.getString(1) + "," +rs.getString(2) ;
                listofucg.add(string);
                //Log.e("Connection",string);
            }
            return listofucg;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getGlucoseMonthlyAverage(int type) {
        List<String> listofucg = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getGlucoseMonthlyAverage(?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setInt(2,type);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                String string = rs.getString(1) + "," +rs.getString(2) ;
                listofucg.add(string);
                //Log.e("Connection",string);
            }
            return listofucg;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<String> getHeartRateTodaysAverage() {
        List<String> listofucg = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getHeartRateTodaysAverage(?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setInt(2,0);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                String string = rs.getString(1) + "," +rs.getString(2) ;
                listofucg.add(string);
                //Log.e("Connection",string);
            }
            return listofucg;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getHeartRateWeeklyAverage(int type) {
        List<String> listofucg = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getHeartRateWeeklyAverage(?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setInt(2,type);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                String string = rs.getString(1) + "," +rs.getString(2) ;
                listofucg.add(string);
                //Log.e("Connection",string);
            }
            return listofucg;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getHeartRateMonthlyAverage(int type) {
        List<String> listofucg = new ArrayList<String>();
        try{
            cstmt = connection.prepareCall("{call DATA.getHeartRateMonthlyAverage(?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setInt(2,type);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()){
                String string = rs.getString(1) + "," +rs.getString(2) ;
                listofucg.add(string);
                //Log.e("Connection",string);
            }
            return listofucg;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void savePermissions(String caregiver,String s) {
        String[] perms = s.split(",");
        int vBp = Integer.parseInt(perms[0]);
        int vBG = Integer.parseInt(perms[1]);
        int vHr = Integer.parseInt(perms[2]);
        try{
            cstmt = connection.prepareCall("{call DATA.setCareGiverPermissions(?,?,?,?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setString(2,caregiver);
            cstmt.setInt(3,vBp);
            cstmt.setInt(4,vBG);
            cstmt.setInt(5,vHr);
            cstmt.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addbp(String thisUserLoginName, int high, int low) {
        try{
            cstmt = connection.prepareCall("{call DATA.addBp(?,?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setInt(2,high);
            cstmt.setInt(3,low);
            cstmt.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addbg(String thisUserLoginName, int num) {
        try{
            cstmt = connection.prepareCall("{call DATA.addBg(?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setString(2,num+"");
            cstmt.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void addHr(String thisUserLoginName, int num) {
        try{
            cstmt = connection.prepareCall("{call DATA.addHr(?,?)}");
            cstmt.setString(1,Login.thisUserLoginName);
            cstmt.setString(2,num+"");
            cstmt.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
