package edu.hune.quanlysinhvien02.model;

import java.time.LocalDateTime;

public class History {
    private LocalDateTime date;
    private String content;
    private int loai;

    public History() {

    }

    public History(LocalDateTime date, String content, int loai) {
        this.date = date;
        this.content = content;
        this.loai = loai;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLoai() {
        return loai;
    }

    public void setLoai(int loai) {
        this.loai = loai;
    }
}
