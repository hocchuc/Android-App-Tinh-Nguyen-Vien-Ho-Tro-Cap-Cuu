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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thuctap.appgialap.R;
import com.thuctap.model.ThietBi;

import java.util.List;

/**
 * Created by Kiệt Nhii on 3/25/2017.
 */

public class ThietBiAdapter extends ArrayAdapter<ThietBi> {
    Activity activity;
    int resource;
    List<ThietBi> objects;
    public ThietBiAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ThietBi> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.resource=resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =  this.activity.getLayoutInflater();
        View view =  inflater.inflate(this.resource, null);

        ThietBi thietBi =  this.objects.get(position);
        TextView txtTenThietBi = (TextView) view.findViewById(R.id.txtTenThietBi);
        TextView txtTrangThai = (TextView) view.findViewById(R.id.txtTrangThai);
        ImageView imgvHinhAnh= (ImageView) view.findViewById(R.id.imageView);
        Picasso.with(view.getContext()).load("http://10.0.3.2/"+thietBi.getHinhanh()).resize(120, 60).into(imgvHinhAnh);

        txtTenThietBi.setText(thietBi.getTenThietBi());
        if(thietBi.getTrangThaiHienTai()==1){
            txtTrangThai.setText("MỞ");
            txtTrangThai.setTextColor(Color.BLUE);
        }else{
            txtTrangThai.setText("TẮT");
            txtTrangThai.setTextColor(Color.RED);
        }


        return view;
    }
}
