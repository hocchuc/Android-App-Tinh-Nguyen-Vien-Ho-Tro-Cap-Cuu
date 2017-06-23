package com.thuctap.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

/**
 * Created by Kiệt Nhii on 3/25/2017.
 */

public class ThietBi implements Serializable {
    private int maThietBi;
    private String tenThietBi;
    private int trangThaiHienTai;
    private Date ngayHienTai;
    private Time gioHienTai;
    private String hinhanh;

    public ThietBi() {
    }

    public int getMaThietBi() {
        return maThietBi;
    }

    public void setMaThietBi(int maThietBi) {
        this.maThietBi = maThietBi;
    }

    public String getTenThietBi() {
        return tenThietBi;
    }

    public void setTenThietBi(String tenThietBi) {
        this.tenThietBi = tenThietBi;
    }

    public int getTrangThaiHienTai() {
        return trangThaiHienTai;
    }

    public void setTrangThaiHienTai(int trangThaiHienTai) {
        this.trangThaiHienTai = trangThaiHienTai;
    }

    public Date getNgayHienTai() {
        return ngayHienTai;
    }

    public void setNgayHienTai(Date ngayHienTai) {
        this.ngayHienTai = ngayHienTai;
    }

    public Time getGioHienTai() {
        return gioHienTai;
    }

    public void setGioHienTai(Time gioHienTai) {
        this.gioHienTai = gioHienTai;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    @Override
    public String toString() {
        return "Thiết bị: "+tenThietBi;
    }
}
