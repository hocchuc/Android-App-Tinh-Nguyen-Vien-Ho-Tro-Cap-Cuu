package com.emc.emergency.Accidents_List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.StrictMode;
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
import com.emc.emergency.ChatBox.ChatBoxActivity;
import com.emc.emergency.Helper.Model.User;
import com.emc.emergency.Helper.Model.User_Type;
import com.emc.emergency.Helper.Utils.GPSTracker;

import com.emc.emergency.R;
import com.emc.emergency.Helper.Model.Accident;
import com.emc.emergency.Helper.Utils.SystemUtils;
import com.emc.emergency.Helper.Utils.ThoiGian;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link fragment_accident_page.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAccidentRecyclerViewAdapter extends RecyclerView.Adapter<MyAccidentRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private final List<Accident> mValues;
    private final fragment_accident_page.OnListFragmentInteractionListener mListener;
    public double latitude = 0;
    public double longitude = 0;
    public SharedPreferences sharedPreferences1;
    int id_user;
    User user1;
    User_Type user_type;

    public MyAccidentRecyclerViewAdapter(Context context, List<Accident> items, fragment_accident_page.OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_accidents, parent, false);
        user1 = new User();
        user_type = new User_Type();
        GPSTracker gps = new GPSTracker(view.getContext());
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        sharedPreferences1 = view.getContext().getSharedPreferences("ID_USER", MODE_PRIVATE);
        id_user = sharedPreferences1.getInt("id_user", -1);

        GetUser();
        GetAccident();
        return new ViewHolder(view);
    }

    private void GetAccident() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl()+"accidents/2")
                .get()
                .build();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void GetUser() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl() + "users/" + id_user)
                .get()
                .build();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                Response response = client.newCall(request).execute();
                try {
                    JSONObject jsonObj = new JSONObject(response.body().string());
                    user1 = new User();
                    user_type = new User_Type();
                    if (jsonObj.has("id_user_type")) {
                        String user_Type = jsonObj.getString("id_user_type");
                        JSONObject jsonObject = new JSONObject(user_Type);
                        if (jsonObject.has("name_user_type"))
                            user_type.setName_user_type(jsonObject.getString("name_user_type"));
                        user1.setUser_type(user_type);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.txtDes.setText(mValues.get(position).getDescription_AC());
        holder.txtStatus.setText(mValues.get(position).getStatus_AC());

        LatLng latLng1 = new LatLng(latitude, longitude);
        LatLng latLng2 = new LatLng(Double.valueOf(mValues.get(position).getLat_AC().toString()), Double.valueOf(mValues.get(position).getLong_AC().toString()));
        double results = SphericalUtil.computeDistanceBetween(latLng1, latLng2) / 1000;

        holder.txtDistance.setText(String.format("%.2f", results) + " km");

        if (mValues.get(position).getRequest_AC())
            holder.txtRequest.setText("Request");
        else holder.txtRequest.setText("Report");
//        holder.txtRequest.setText(mValues.get(position).getRequest_AC()+"");
//        holder.txtLong_Ac.setText((mValues.get(position).getLong_AC().toString()));
//        holder.txtLat_Ac.setText((mValues.get(position).getLat_AC().toString()));
        String origin = mValues.get(position).getDate_AC();
        String[] subCurrent = origin.split("T");
        String subDay = subCurrent[0];
        String subTime = subCurrent[1];
        String[] subCurrent1 = subTime.split(".000+0000");
        String subTime1 = subCurrent1[0];
//        Log.d("subTime1", subTime1);
        String dateStart = subDay + " " + subTime1;
//        Log.d("dateStart", dateStart);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = new Date();
        String dateStop = dateFormat.format(date);
//        Log.d("dateStop", dateStop);
        String thoiGianOnOff = ThoiGian.thoiGianOnOff(dateStart, dateStop);
        holder.txtDate.setText(thoiGianOnOff);
        holder.txtAdress.setText(mValues.get(position).getAddress());
        holder.imgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, mValues.get(position).getDescription_AC(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), ChatBoxActivity.class);
                i.putExtra("type", SystemUtils.TYPE_HELPER);
                Log.d("type", SystemUtils.TYPE_HELPER);
                i.putExtra("FirebaseKey", holder.mItem.getFirebaseKey());
                Log.d("FirebaseKey", holder.mItem.getFirebaseKey());
                i.putExtra("id_AC", holder.mItem.getId_AC() + "");
                i.putExtra("id_victim", holder.mItem.getId_user() + "");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });
        holder.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user1.getUser_type().getName_user_type().equals("user")) {
                    Intent i = new Intent(v.getContext(), ChatBoxActivity.class);
                    i.setAction(SystemUtils.TYPE_HELPER);
                    i.putExtra("type", SystemUtils.TYPE_HELPER);
                    i.putExtra("id_AC", holder.mItem.getId_AC() + "");
                    i.putExtra("id_victim", holder.mItem.getId_user() + "");
                    i.putExtra("FirebaseKey", holder.mItem.getFirebaseKey());
                    v.getContext().startActivity(i);
                } else holder.floatingActionButton.setEnabled(false);
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
        public final TextView txtDistance;
        public final TextView txtRequest;

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
            txtRequest = (TextView) view.findViewById(R.id.txtRequest_NearYou);
            txtDistance = (TextView) view.findViewById(R.id.txtDistance_NearYou);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
