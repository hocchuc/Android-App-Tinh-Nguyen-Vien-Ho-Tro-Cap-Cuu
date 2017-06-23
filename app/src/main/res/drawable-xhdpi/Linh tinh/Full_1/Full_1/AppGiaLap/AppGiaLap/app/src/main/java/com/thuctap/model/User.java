package com.thuctap.model;

import java.io.Serializable;

/**
 * Created by Kiệt Nhii on 3/31/2017.
 */

public class User implements Serializable {
    private int maNguoiDung;
    private String username;
    private String password;
    private int loaiTaiKhoan;

    public User(int maNguoiDung, String username, String password, int loaiTaiKhoan) {
        this.maNguoiDung = maNguoiDung;
        this.username = username;
        this.password = password;
        this.loaiTaiKhoan = loaiTaiKhoan;
    }

    public User() {
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoaiTaiKhoan() {
        return loaiTaiKhoan;
    }

    public void setLoaiTaiKhoan(int loaiTaiKhoan) {
        this.loaiTaiKhoan = loaiTaiKhoan;
    }
}
