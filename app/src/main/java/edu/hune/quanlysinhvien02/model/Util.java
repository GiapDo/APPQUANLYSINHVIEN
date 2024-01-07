package edu.hune.quanlysinhvien02.model;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

    public static boolean checkMatKhau(String pas){
        int chuT = 0, chuH = 0, so = 0, kiTuDB = 0;
        for(int i = 0; i < pas.length(); i++){
            if(pas.charAt(i) >= 'a' && pas.charAt(i) <= 'z'){
                chuT += 1;
            } else if (pas.charAt(i) >= 'A' && pas.charAt(i) <= 'Z') {
                chuH += 1;
            } else if (pas.charAt(i) >= '0' && pas.charAt(i) <= '9') {
                so += 1;
            }else {
                kiTuDB += 1;
            }
        }

        return chuT != 0 && chuH != 0 && so != 0 && kiTuDB != 0;
    }

    public static double getDiem10(double dcc, double ddk, double dt){
        return dcc * 0.1 + ddk * 0.3 + dt * 0.6;
    }

    public static double getDiem4(double diem10){
        if(diem10 >= 8.5 ){
            return 4;
        } else if (diem10 >= 7.8) {
            return 3.5;
        }else if (diem10 >= 7) {
            return 3;
        }else if (diem10 >= 6.3) {
            return 2.5;
        }else if (diem10 >= 5.5) {
            return 2;
        }else if (diem10 >= 4.8) {
            return 1.5;
        }else if (diem10 >= 4) {
            return 1;
        }else {
            return 0;
        }
    }

    public static String getDiemChu(double diem10){
        if(diem10 >= 8.5 ){
            return "A";
        } else if (diem10 >= 7.8) {
            return "B+";
        }else if (diem10 >= 7) {
            return "B";
        }else if (diem10 >= 6.3) {
            return "C+";
        }else if (diem10 >= 5.5) {
            return "C";
        }else if (diem10 >= 4.8) {
            return "D+";
        }else if (diem10 >= 4) {
            return "D";
        }else {
            return "F";
        }
    }

    public static String chuanDiem2STP(double diem){
        return String.format("%.2f", diem);
    }

    public static LocalDate toDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String toStringDate(LocalDate date){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dtf.format(date);
    }

    public static LocalDateTime toDateTime(String date){
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm:ss"));
    }

    public static String toStringDateTime(LocalDateTime date){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm:ss");
        return dtf.format(date);
    }

    public static SQLiteDatabase initDatabase(Activity activity, String databaseName){
        String outFileName = activity.getApplicationInfo().dataDir + "/databases/" + databaseName;
        File f = new File(outFileName);
        try {
            if(!f.exists()){
                InputStream e = activity.getAssets().open(databaseName);
                File foder = new File(activity.getApplicationInfo().dataDir + "/databases/");
                if(!foder.exists()){
                    foder.mkdir();
                }
                FileOutputStream myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];

                int length;
                while ((length = e.read(buffer)) > 0){
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                e.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return activity.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
    }

}
