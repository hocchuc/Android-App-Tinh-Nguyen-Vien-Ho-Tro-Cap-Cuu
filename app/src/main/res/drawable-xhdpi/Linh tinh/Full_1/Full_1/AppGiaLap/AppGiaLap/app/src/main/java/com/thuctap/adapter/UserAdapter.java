package com.thuctap.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thuctap.appgialap.R;
import com.thuctap.model.User;

import java.util.List;

/**
 * Created by Kiệt Nhii on 3/31/2017.
 */

public class UserAdapter extends ArrayAdapter<User> {
    Activity activity;
    int resource;
    List<User> objects;

    public UserAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        this.activity =  context;
        this.resource =  resource;
        this.objects =  objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater =  this.activity.getLayoutInflater();
        View view =  inflater.inflate(this.resource, null);

        User user =  this.objects.get(position);
        TextView txtUsername = (TextView)view.findViewById(R.id.txtUsername);
        TextView txtPassword = (TextView)view.findViewById(R.id.txtPassword);
        TextView txtLoaiTaiKhoan = (TextView)view.findViewById(R.id.txtLoaiTaiKhoan);

        txtUsername.setText("Tài khoản: "+user.getUsername());
        txtPassword.setText("Mật khẩu: "+user.getPassword());
        if(user.getLoaiTaiKhoan()==0){
            txtLoaiTaiKhoan.setText("Người dùng admin");
        }else if(user.getLoaiTaiKhoan()==2){
            txtLoaiTaiKhoan.setText("Người dùng đang bị cấm");
            txtLoaiTaiKhoan.setTextColor(Color.RED);
        }
        return  view;
    }
}
