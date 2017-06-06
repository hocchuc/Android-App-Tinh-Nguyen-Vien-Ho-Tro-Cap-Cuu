package com.emc.emergency.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.emc.emergency.R;
import com.emc.emergency.model.Accident;

import java.util.ArrayList;


/**
 * Created by Admin on 31/5/2017.
 */

public class AccidentAdapter extends ArrayAdapter<Accident> {

    Context context;
    int resource;
    ArrayList<Accident> objects;

    public AccidentAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Accident> objects) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if(v==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(resource,null);
        }
        TextView txtDes = (TextView) v.findViewById(R.id.txtDes_Ac);
        TextView txtStatus = (TextView) v.findViewById(R.id.txtStatus);
//        TextView txtLong_Ac = (TextView) v.findViewById(R.id.txtLong_Ac);
//        TextView txtLat_Ac = (TextView) v.findViewById(R.id.txtLat_Ac);
        TextView txtDate = (TextView) v.findViewById(R.id.txtDate_Ac);

        Accident accident = objects.get(position);
        Log.d("Description",accident.getDescription_AC().toString());
        Log.d("Date",accident.getDate_AC().toString());
        Log.d("LongAC",accident.getLong_AC().toString());
        Log.d("LatAC",accident.getLat_AC().toString());
        Log.d("Status",accident.getStatus_AC().toString());
        txtDate.setText(accident.getDate_AC());
//        txtLong_Ac.setText(String.valueOf(accident.getLong_AC()));
//        txtLat_Ac.setText(String.valueOf(accident.getLat_AC()));
        txtDes.setText(accident.getDescription_AC());
        txtStatus.setText(accident.getStatus_AC());

        return v;
    }

}
