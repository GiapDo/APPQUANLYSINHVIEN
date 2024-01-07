package edu.hune.quanlysinhvien02.model;

public class HeDaoTao {
    private String maHDT, tenHDT;

    public HeDaoTao() {

    }

    public HeDaoTao(String maHDT, String tenHDT) {
        this.maHDT = maHDT;
        this.tenHDT = tenHDT;
    }

    public String getMaHDT() {
        return maHDT;
    }

    public void setMaHDT(String maHDT) {
        this.maHDT = maHDT;
    }

    public String getTenHDT() {
        return tenHDT;
    }

    public void setTenHDT(String tenHDT) {
        this.tenHDT = tenHDT;
    }

    @Override
    public String toString() {
        return tenHDT;
    }
}
