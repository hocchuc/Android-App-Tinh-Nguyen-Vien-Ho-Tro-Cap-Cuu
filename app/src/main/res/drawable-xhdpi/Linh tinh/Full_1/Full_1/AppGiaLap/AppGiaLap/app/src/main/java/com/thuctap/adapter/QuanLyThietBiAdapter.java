package com.thuctap.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thuctap.appgialap.R;
import com.thuctap.model.ThietBi;
import com.thuctap.util.ThoiGian;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Kiệt Nhii on 3/31/2017.
 */

public class QuanLyThietBiAdapter extends ArrayAdapter<ThietBi> {
    Activity activity;
    int resource;
    List<ThietBi> objects;
    public QuanLyThietBiAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ThietBi> objects) {
        super(context, resource, objects);
        this.activity=context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =  this.activity.getLayoutInflater();
        View view =  inflater.inflate(this.resource, null);
        ThietBi thietBi =  this.objects.get(position);
        TextView txtTenTb = (TextView) view.findViewById(R.id.txtTenThietBi);
        TextView txtTrangThai = (TextView) view.findViewById(R.id.txtTrangThaiHienTai);
        TextView txtThoiGian = (TextView) view.findViewById(R.id.txtThoiGian);
        ImageView imgvHinhAnh= (ImageView) view.findViewById(R.id.imgHinhAnh);
        Picasso.with(view.getContext()).load("http://10.0.3.2/"+thietBi.getHinhanh()).resize(120, 60).into(imgvHinhAnh);
        String dateStart = thietBi.getNgayHienTai()+" "+thietBi.getGioHienTai();
        DateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date =  new java.util.Date();
        String dateStop = dateFormat.format(date);
        String thoiGianOnOff = ThoiGian.thoiGianOnOff(dateStart, dateStop);
        txtTenTb.setText(thietBi.getTenThietBi());
        if(thietBi.getTrangThaiHienTai()==1){
            txtTrangThai.setText("Đang mở");
            txtTrangThai.setTextColor(Color.BLUE);
        }else {
            txtTrangThai.setText("Đang tắt");
            txtTrangThai.setTextColor(Color.RED);
        }
        txtThoiGian.setText(thoiGianOnOff+"");
        return view;
    }
}
