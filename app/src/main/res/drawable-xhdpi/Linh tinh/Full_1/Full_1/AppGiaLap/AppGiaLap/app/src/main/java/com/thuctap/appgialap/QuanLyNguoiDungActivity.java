package com.thuctap.appgialap;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.thuctap.adapter.UserAdapter;
import com.thuctap.model.User;
import com.thuctap.task.LayDanhSachNguoiDungTask;
import com.thuctap.task.ThemThietBiTask;
import com.thuctap.task.ThemUserTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class QuanLyNguoiDungActivity extends AppCompatActivity {
    ListView lvUser;
    Button btnThemUser;
    ArrayList<User> arrUser;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_nguoi_dung);
        addControls();
        addEvents();

    }

    private void addControls() {
        lvUser =(ListView)findViewById(R.id.lvUser);
        btnThemUser= (Button)findViewById(R.id.btnThemUser1);
        arrUser =  new ArrayList<>();
        userAdapter =  new UserAdapter(QuanLyNguoiDungActivity.this,
                R.layout.user_layout,
                arrUser);
        lvUser.setAdapter(userAdapter);

        LayDanhSachNguoiDungTask task =  new LayDanhSachNguoiDungTask(QuanLyNguoiDungActivity.this,
                arrUser,
                userAdapter);
        task.execute();
    }

    private void addEvents() {
        btnThemUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThemUser();
            }
        });
    }

    private void xuLyThemUser() {
        final Dialog d =  new Dialog(this);
        d.setContentView(R.layout.themuser_layout);
        d.setTitle("Thêm user mới: ");
        d.setCanceledOnTouchOutside(false);
        final EditText edtUser = (EditText) d.findViewById(R.id.edtUsername);
        final EditText edtPass = (EditText) d.findViewById(R.id.edtPass);
        Button btnThem = (Button) d.findViewById(R.id.btnThem);
        Button btnQuayVe = (Button) d.findViewById(R.id.btnQuayLai);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username =  edtUser.getText().toString();
                String pass =  edtPass.getText().toString();
                if(username.equals("") || pass.equals("") ){
                    Toast.makeText(QuanLyNguoiDungActivity.this, "Bạn chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                ThemUserTask task =  new ThemUserTask(QuanLyNguoiDungActivity.this,
                        username,
                        pass,
                        d);
                task.execute("http://10.0.3.2:8080/RestProject/Rest/register/doregister");
            }
        });

        btnQuayVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }
}
