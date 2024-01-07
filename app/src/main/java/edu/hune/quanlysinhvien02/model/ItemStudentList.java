package edu.hune.quanlysinhvien02.model;

import android.app.Activity;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import edu.hune.quanlysinhvien02.database.MyDatabaseHelper;

public class ItemStudentList {

    private String maSV, tenSV, lop, khoa, email, sdt;
    private Activity activity;

    public ItemStudentList(String maSV, String tenSV, String lop, String khoa, String email, String sdt, Activity activity) {
        this.maSV = maSV;
        this.tenSV = tenSV;
        this.lop = lop;
        this.khoa = khoa;
        this.email = email;
        this.sdt = sdt;
        this.activity = activity;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getTenSV() {
        return tenSV;
    }

    public void setTenSV(String tenSV) {
        this.tenSV = tenSV;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    private class Diem{
        double diemCC, diemGK, diemThi;
        int tinChi;

        public Diem(double diemCC, double diemGK, double diemThi, int tinChi) {
            this.diemCC = diemCC;
            this.diemGK = diemGK;
            this.diemThi = diemThi;
            this.tinChi = tinChi;
        }

        public double getDiemCC() {
            return diemCC;
        }

        public double getDiemGK() {
            return diemGK;
        }

        public double getDiemThi() {
            return diemThi;
        }

        public int getTinChi() {
            return tinChi;
        }
    }


    public String getDiem(){
        MyDatabaseHelper db = new MyDatabaseHelper(activity);

        Cursor cs = db.readDiem(this.maSV);

        List<Diem> list = new ArrayList<>();
        if(cs.getCount() == 0){
            return "";
        }else {
            while (cs.moveToNext()){
                Diem diem = new Diem( !cs.isNull(0) ? cs.getDouble(0) : -1,
                                      !cs.isNull(1) ? cs.getDouble(1) : -1,
                                      !cs.isNull(2) ? cs.getDouble(2) : -1,
                                       cs.getInt(3));
                list.add(diem);
            }
        }

        cs.close();

        double diem = 0;
        int soTinChi = 0;

        for(Diem tmp : list){
            if(tmp.getDiemCC() != -1 && tmp.getDiemGK() != -1 && tmp.getDiemThi() != -1){
                diem += Util.getDiem4(tmp.getDiemCC() * 0.1 + tmp.getDiemGK() * 0.3 + tmp.getDiemThi() * 0.6) * tmp.getTinChi();
                soTinChi += tmp.getTinChi();
            }
        }

        if(diem != 0) {
            return Util.chuanDiem2STP(diem / soTinChi);
        }else {
            return "";
        }
    }
}
