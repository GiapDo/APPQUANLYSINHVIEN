package edu.hune.quanlysinhvien02.model;

public class ItemStudentDeModi {

    private String maSV, tenSV;

    public ItemStudentDeModi() {

    }

    public ItemStudentDeModi(String maSV, String tenSV) {
        this.maSV = maSV;
        this.tenSV = tenSV;
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
}
