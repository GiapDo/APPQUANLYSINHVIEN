package edu.hune.quanlysinhvien02.model;

public class ItemStudentLopTC {
    private int masv;
    private String tensv;
    private boolean trangThai;

    public ItemStudentLopTC() {

    }

    public ItemStudentLopTC(int masv, String tensv, boolean trangThai) {
        this.masv = masv;
        this.tensv = tensv;
        this.trangThai = trangThai;
    }

    public int getMasv() {
        return masv;
    }

    public void setMasv(int masv) {
        this.masv = masv;
    }

    public String getTensv() {
        return tensv;
    }

    public void setTensv(String tensv) {
        this.tensv = tensv;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "Mã sinh viên: " + masv + "\nTên Sinh viên: " + tensv ;
    }
}
