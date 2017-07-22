package com.emc.emergency.Report_Accident;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emc.emergency.R;

/**
 *  Adapter để làm GridMenu cho Report Accident
 */

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private final int[] Imageid;
    private int selectedPosition = -1;

    public CustomGrid(Context mContext, String[] web, int[] imageid) {
        this.mContext = mContext;
        this.web = web;
        this.Imageid = imageid;
    }

    @Override
    public int getCount() {
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(web[position]);
            imageView.setImageResource(Imageid[position]);
        } else {
            grid = (View) convertView;
        }

        if (position == selectedPosition) {
            grid.setBackgroundColor(Color.RED);
        } else {
            grid.setBackgroundColor(Color.TRANSPARENT);
        }

        return grid;
    }
}
