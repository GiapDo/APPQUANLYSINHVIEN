package edu.hune.quanlysinhvien02.model;

public class LopTinChi {
    private int maKhoa, svMax;
    private String maLopTC;

    public LopTinChi() {

    }

    public LopTinChi(int maKhoa, int svMax, String maLopTC) {
        this.maKhoa = maKhoa;
        this.svMax = svMax;
        this.maLopTC = maLopTC;
    }

    public int getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(int maKhoa) {
        this.maKhoa = maKhoa;
    }

    public int getSvMax() {
        return svMax;
    }

    public void setSvMax(int svMax) {
        this.svMax = svMax;
    }

    public String getMaLopTC() {
        return maLopTC;
    }

    public void setMaLopTC(String maLopTC) {
        this.maLopTC = maLopTC;
    }

    @Override
    public String toString() {
        return  maLopTC ;
    }
}
