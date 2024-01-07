package edu.hune.quanlysinhvien02.model;

public class ItemDiemFg {

    private String maSV, tenHP, maHp, maLopTC, ghiChu;
    private int soTinChi;
    private Double diemCC, diemDK, diemThi;

    public ItemDiemFg() {

    }

    public ItemDiemFg(String maSV, String tenHP, String maHp, String maLopTC, String ghiChu, int soTinChi, Double diemCC, Double diemDK, Double diemThi) {
        this.maSV = maSV;
        this.tenHP = tenHP;
        this.maHp = maHp;
        this.maLopTC = maLopTC;
        this.ghiChu = ghiChu;
        this.soTinChi = soTinChi;
        this.diemCC = diemCC;
        this.diemDK = diemDK;
        this.diemThi = diemThi;
    }


    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getTenHP() {
        return tenHP;
    }

    public void setTenHP(String tenHP) {
        this.tenHP = tenHP;
    }

    public String getMaHp() {
        return maHp;
    }

    public void setMaHp(String maHp) {
        this.maHp = maHp;
    }

    public String getMaLopTC() {
        return maLopTC;
    }

    public void setMaLopTC(String maLopTC) {
        this.maLopTC = maLopTC;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }

    public Double getDiemCC() {
        return diemCC;
    }

    public void setDiemCC(Double diemCC) {
        this.diemCC = diemCC;
    }

    public Double getDiemDK() {
        return diemDK;
    }

    public void setDiemDK(Double diemDK) {
        this.diemDK = diemDK;
    }

    public Double getDiemThi() {
        return diemThi;
    }

    public void setDiemThi(Double diemThi) {
        this.diemThi = diemThi;
    }
}
