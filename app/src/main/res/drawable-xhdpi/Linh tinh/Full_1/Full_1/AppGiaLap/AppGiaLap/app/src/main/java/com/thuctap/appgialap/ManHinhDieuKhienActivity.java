package com.thuctap.appgialap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thuctap.model.ThietBi;
import com.thuctap.task.LuuLaiLichSuTask;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ManHinhDieuKhienActivity extends AppCompatActivity {
    ThietBi thietBi;
    TextView txtTenThietBi;
    TextView txtTrangThai;
    Button btnOn;
    Button btnOff;
    ProgressDialog progressDialog;
    SimpleDateFormat sdf1 =  new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat sdf2 =  new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_dieu_khien);
        addControls();
        addEvents();
    }

    private void addControls() {
        Intent intent =  getIntent();
        Bundle bundle =  intent.getBundleExtra("tb");
        thietBi = (ThietBi) bundle.getSerializable("thietbi");
        txtTenThietBi = (TextView)findViewById(R.id.txtTenThietBi);
        txtTrangThai = (TextView)findViewById(R.id.txtTrangThai);
        btnOn =(Button)findViewById(R.id.btnOn);
        btnOff =(Button)findViewById(R.id.btnOff);

    }

    private void addEvents() {
        Date date =  new Date();
        String ngay =  sdf2.format(date)+"";
        thietBi.setNgayHienTai(java.sql.Date.valueOf(ngay));
        String gio =  sdf1.format(date)+"";
        thietBi.setGioHienTai(Time.valueOf(gio));
        txtTenThietBi.setText(thietBi.getTenThietBi());
        if(thietBi.getTrangThaiHienTai()==1){
            txtTrangThai.setText("Thiết bị đang mở");
            btnOn.setVisibility(View.INVISIBLE);
        }else {
            txtTrangThai.setText("Thiết bị đag tắt");
            btnOff.setVisibility(View.INVISIBLE);
        }

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyOn();
            }
        });
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyOff();
            }
        });
    }

    private void xuLyOff() {
        btnOff.setVisibility(View.INVISIBLE);
        btnOn.setVisibility(View.VISIBLE);
        thietBi.setTrangThaiHienTai(0);
        txtTrangThai.setText("Thiết bị đang off");
        LuuLaiLichSuTask task1 = new LuuLaiLichSuTask(ManHinhDieuKhienActivity.this, thietBi, progressDialog);
        task1.execute("http://10.0.3.2:8080/RestProject/Rest/thietbiModifier/ModifiThietbi");
    }

    private void xuLyOn() {
        btnOff.setVisibility(View.VISIBLE);
        btnOn.setVisibility(View.INVISIBLE);
        thietBi.setTrangThaiHienTai(1);
        txtTrangThai.setText("Thiết bị đang on");
        LuuLaiLichSuTask task1 = new LuuLaiLichSuTask(ManHinhDieuKhienActivity.this, thietBi, progressDialog);
        task1.execute("http://10.0.3.2:8080/RestProject/Rest/thietbiModifier/ModifiThietbi");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =  new Intent(ManHinhDieuKhienActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
