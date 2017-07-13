package com.emc.emergency.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.emc.emergency.Chat.ChatBoxActivity;
import com.emc.emergency.Fragment.fragment_accident_page;
import com.emc.emergency.R;
import com.emc.emergency.model.Accident;
import com.emc.emergency.model.MessageEvent;
import com.emc.emergency.utils.SystemUtils;
import com.emc.emergency.utils.ThoiGian;
import com.google.gson.Gson;


import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link fragment_accident_page.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAccidentRecyclerViewAdapter extends RecyclerView.Adapter<MyAccidentRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private final List<Accident> mValues;
    private final fragment_accident_page.OnListFragmentInteractionListener mListener;

    public MyAccidentRecyclerViewAdapter(Context context, List<Accident> items, fragment_accident_page.OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_accidents, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.txtDes.setText(mValues.get(position).getDescription_AC());
        holder.txtStatus.setText(mValues.get(position).getStatus_AC());
//        holder.txtLong_Ac.setText((mValues.get(position).getLong_AC().toString()));
//        holder.txtLat_Ac.setText((mValues.get(position).getLat_AC().toString()));
        String origin = mValues.get(position).getDate_AC();
        String []subCurrent=origin.split("T");
        String subDay=subCurrent[0];
        String subTime=subCurrent[1];
        String []subCurrent1=subTime.split(".000+0000");
        String subTime1=subCurrent1[0];
        Log.d("subTime1",subTime1);
        String dateStart=subDay+" "+subTime1;
                        Log.d("dateStart", dateStart);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateStop = dateFormat.format(date);
        Log.d("dateStop", dateStop);
        String thoiGianOnOff = ThoiGian.thoiGianOnOff(dateStart,dateStop);
        holder.txtDate.setText(thoiGianOnOff);
        holder.txtAdress.setText(mValues.get(position).getAddress());
        holder.imgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, mValues.get(position).getDescription_AC(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), ChatBoxActivity.class);
                                       i.putExtra("type", SystemUtils.TYPE_HELPER);
                                       Log.d("type",SystemUtils.TYPE_HELPER);
                               
                                       i.putExtra("FirebaseKey", holder.mItem.getFirebaseKey());
                                       Log.d("FirebaseKey",holder.mItem.getFirebaseKey());
                                       i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });
        holder.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ChatBoxActivity.class);
                       i.putExtra("type", SystemUtils.TYPE_HELPER);
                       Log.d("type",SystemUtils.TYPE_HELPER);
                       i.putExtra("FirebaseKey", holder.mItem.getFirebaseKey());
                v.getContext().startActivity(i);
            }
        });
        // request cho glide
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.marker)
                .error(R.drawable.material_drawer_circle_mask)
                .priority(Priority.HIGH);
        Glide.with(context)
                .load("https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=640x250&maptype=roadmap&markers=color:red%7Clabel:C%7C" + mValues.get(position).getLat_AC() + "," + mValues.get(position).getLong_AC())
                .apply(options)
                .into(holder.imgV);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Accident mItem;
        public final TextView txtDes;
        public final TextView txtStatus;
//        public final TextView txtLong_Ac;
//        public final TextView txtLat_Ac;
        public final TextView txtDate;
        public final ImageView imgV;
        public final FloatingActionButton floatingActionButton;
        public final TextView txtAdress;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtDes = (TextView) view.findViewById(R.id.txtDes_AC);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
//            txtLong_Ac = (TextView) view.findViewById(R.id.txtLong_Ac);
//            txtLat_Ac = (TextView) view.findViewById(R.id.txtLat_Ac);
            txtDate = (TextView) view.findViewById(R.id.txtDate_AC);
            imgV = (ImageView) view.findViewById(R.id.imgVAC);
            floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
            txtAdress = (TextView) view.findViewById(R.id.txtAddess_Ac);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
