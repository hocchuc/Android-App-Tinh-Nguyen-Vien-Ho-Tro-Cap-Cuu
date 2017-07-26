package com.emc.emergency.ChatBox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.Toast;

import com.emc.emergency.Helper.Model.UserJoined;
import com.emc.emergency.R;
import com.emc.emergency.Helper.Model.Accident;
import com.emc.emergency.Helper.Model.Accident_Detail;
import com.emc.emergency.Helper.Model.MessageEvent;
import com.emc.emergency.Helper.Model.User;
import com.emc.emergency.Helper.Model.User_Type;
import com.emc.emergency.Helper.Utils.GPSTracker;
import com.emc.emergency.Helper.Utils.SystemUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link .} interface
 * to handle interaction events.
 * Use the {@link fragment_map_page#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_map_page extends Fragment implements OnMapReadyCallback, LocationListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "vido";
    private static final String ARG_PARAM2 = "kinhdo";
    //    LocationManager locationManager;
    GoogleMap mMap;
    //    OnMapReadyCallback onMapReadyCallback;
//    ArrayList<Accident> accidentList;
    double lat = 0;
    double lon = 0;
    double viDo = 0;
    double kinhDo = 0;
    //    String moTa, diaChi;
    private final Handler mHandler;
    private Runnable mAnimation;
    String AccidentKey = "";
    String AccidentKey_noti = "";
    String UserJoinedKey = "";
    int id_user;
    ArrayList<UserJoined> arrUserJoineds;
    String Active = "";
    public static final String ACCIDENTS_CHILD = "accidents";
    private DatabaseReference mFirebaseDatabaseReference;


    /**
     * Performs a bounce animation on a {@link Marker}.
     */
    private static class BounceAnimation implements Runnable {

        private final long mStart, mDuration;
        private final Interpolator mInterpolator;
        private final Marker mMarker;
        private final Handler mHandler;

        private BounceAnimation(long start, long duration, Marker marker, Handler handler) {
            mStart = start;
            mDuration = duration;
            mMarker = marker;
            mHandler = handler;
            mInterpolator = new BounceInterpolator();
        }

        @Override
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - mStart;
            float t = Math.max(1 - mInterpolator.getInterpolation((float) elapsed / mDuration), 0f);
            mMarker.setAnchor(0.5f, 1.0f + 1.2f * t);

            if (t > 0.0) {
                // Post again 16ms later.
                mHandler.postDelayed(this, 16L);
            }
        }
    }

    //    private GoogleMap map;
    // TODO: Rename and change types of parameters
    private double mParam1;
    private double mParam2;
    String mprovider;
    MapView mapView;
    private SharedPreferences sharedPreferences, sharedPreferences1, sharedPreferences2, sharedPreferences3;
    String id_AC = "";
    ArrayList<Accident_Detail> accident_details;
    // XU LY NUT VE DUONG
//    private EditText etOrigin;
//    private EditText etDestination;

    Button btnVeDuong;
//    private List<Marker> originMarkers = new ArrayList<>();
//    private List<Marker> destinationMarkers = new ArrayList<>();
//    private List<Polyline> polylinePaths = new ArrayList<>();
//    private ProgressDialog progressDialog;


    private onFragmentMapInteraction mListener;

    /**
     * Lưu ý khởi tạo Handler
     */
    public fragment_map_page() {
        mHandler = new Handler();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment fragment_map_page.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_map_page newInstance(String vido, String kinhdo) {
        fragment_map_page fragment = new fragment_map_page();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, vido);
        args.putString(ARG_PARAM2, kinhdo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   SupportMapFragment supportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_pager);
        supportMapFragment.getMapAsync(this);*/
        GPSTracker gps = new GPSTracker(this.getActivity());
        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            lon = gps.getLongitude();
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getDouble(ARG_PARAM1);
            mParam2 = getArguments().getDouble(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map_page, container, false);
        btnVeDuong = (Button) view.findViewById(R.id.btnVeDuong);

        arrUserJoineds = new ArrayList<>();

        sharedPreferences = getActivity().getSharedPreferences("ID_ACC", MODE_PRIVATE);
        id_AC = sharedPreferences.getString("id_acc", "");

        sharedPreferences1 = getActivity().getSharedPreferences("ID_USER", MODE_PRIVATE);
        id_user = sharedPreferences1.getInt("id_user", -1);

        sharedPreferences2 = getActivity().getSharedPreferences("ACCIDENT_KEY_NOTI", MODE_PRIVATE);
        AccidentKey_noti = sharedPreferences2.getString("accident_key_noti", "");
        Active = sharedPreferences2.getString("Type_active", "");

        sharedPreferences3 = getActivity().getSharedPreferences("ACCIDENT_KEY", MODE_PRIVATE);
        AccidentKey = sharedPreferences3.getString("accident_key", "");

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentMapInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentMapInteraction) {
            mListener = (onFragmentMapInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        addValueEventListener();


//        LatLng myLocation = new LatLng(lat, lon);
//        MarkerOptions markerOptions = new MarkerOptions()
//                .position(myLocation)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                .title("Bạn đang ở đây !!")
//                .snippet("You are here !!");
//        Marker marker = mMap.addMarker(markerOptions);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
//
//        /**
//         * Tạo hiệu ứng nảy
//         */
//        // This causes the marker at Perth to bounce into position when it is clicked.
//        final long start = SystemClock.uptimeMillis();
//        final long duration = 1500L;
//
//        // Cancels the previous animation
//        mHandler.removeCallbacks(mAnimation);
//
//        // Starts the bounce animation
//        mAnimation = new BounceAnimation(start, duration, marker, mHandler);
//        mHandler.post(mAnimation);
//        // for the default behavior to occur (which is for the camera to move such that the
//        // marker is centered and for the marker's info window to open, if it has one).

        if (Active.equals("Active"))
            SendtoActionOnFirebase();

    }

    private void addValueEventListener() {
        UserJoinedKey = mFirebaseDatabaseReference.child(ACCIDENTS_CHILD)
                .child(AccidentKey)
                .child("User joined").push().getKey();
        if (mMap == null) return;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        DatabaseReference ref1 = mFirebaseDatabaseReference.child(ACCIDENTS_CHILD)
                .child(AccidentKey)
                .child("User joined").child(UserJoinedKey);
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserJoined userJoined1 = dataSnapshot.getValue(UserJoined.class);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_user_sos);

                if (dataSnapshot.exists()) {
//                    viDo = userJoined1.getLat_userjoined();
//                    kinhDo = userJoined1.getLong_userjoined();
                    LatLng loocation = new LatLng(userJoined1.getLat_userjoined(), userJoined1.getLong_userjoined());
                    mMap.addMarker(new MarkerOptions()
                            .position(loocation)
                            .title(String.valueOf(userJoined1.getUser_id())))
                            .setIcon(icon);
                } else {
                    LatLng myLocation = new LatLng(lat, lon);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(myLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .title("Bạn đang ở đây !!")
                            .snippet("You are here !!");
                    Marker marker = mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            mParam1 = location.getLatitude();
            mParam2 = location.getLongitude();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        Intent callMapFragment = new Intent(getActivity(), fragment_map_page.class);
        startActivity(callMapFragment);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        Intent callMapFragment = new Intent(getActivity(), fragment_map_page.class);
        startActivity(callMapFragment);


        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        Intent callMapFragment = new Intent(getActivity(), fragment_map_page.class);
        startActivity(callMapFragment);
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        Intent callMapFragment = new Intent(getActivity(), fragment_map_page.class);
        startActivity(callMapFragment);
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface onFragmentMapInteraction {
        // TODO: Update argument type and name
        public void onFragmentMapInteraction(Uri uri);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 1)
    public void MapListerner(MessageEvent event) {
        if (event.type.equals(SystemUtils.TAG_LOAD_MAP)) {

        }

        if (event.type.equals(SystemUtils.TAG_GO_MAP)) {

            Accident accident = new Gson().fromJson(event.message, Accident.class);
            LatLng lat_1 = new LatLng(accident.getLong_AC(), accident.getLat_AC());
            Toast.makeText(this.getContext(), accident.getDescription_AC(), Toast.LENGTH_SHORT).show();

            try {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(lat_1));
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(lat_1);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(lat_1, 15);
                mMap.moveCamera(cameraUpdate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void SendtoActionOnFirebase() {
        final UserJoined userJoined = new UserJoined();

        userJoined.setUser_id(Long.valueOf(id_user));
        userJoined.setLat_userjoined(lat);
        userJoined.setLong_userjoined(lon);

        mFirebaseDatabaseReference.child(ACCIDENTS_CHILD)
                .child(AccidentKey_noti)
                .child("User joined").child(UserJoinedKey).setValue(userJoined);
//        // Get a reference to our posts
//        DatabaseReference ref = mFirebaseDatabaseReference.child(ACCIDENTS_CHILD)
//                .child(AccidentKey_noti)
//                .child("User joined").child(UserJoinedKey);
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                UserJoined userJoined1 = dataSnapshot.getValue(UserJoined.class);
//                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_user_sos);
//
//                viDo = userJoined1.getLat_userjoined();
//                kinhDo = userJoined1.getLong_userjoined();
//                LatLng loocation = new LatLng(viDo, kinhDo);
//                mMap.addMarker(new MarkerOptions()
//                        .position(loocation)
//                        .title(String.valueOf(userJoined1.getUser_id())))
//                        .setIcon(icon);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }
//    class GetAllUsers extends AsyncTask<Void, String, List<User>> {
//        Context context;
//
//        public GetAllUsers(Context context) {
//            this.context = context;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(List<User> userList) {
//            super.onPostExecute(userList);
//            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_user_sos);
//            accident_details=new ArrayList<>();
//
//            OkHttpClient client = new OkHttpClient();
//
//            RequestBody body = RequestBody.create(null, new byte[0]);
//            Request request = new Request.Builder()
//                    .url(SystemUtils.getServerBaseUrl()+"accident/GetAllUserJoined/"+id_AC)
//                    .post(body)
//                    .build();
//
//            try {
//                Response response = client.newCall(request).execute();
//                JSONArray jsonArray=new JSONArray(response.body().string());
//                for(int i=0;i<jsonArray.length();i++){
//                    Accident_Detail accident_detail=new Accident_Detail();
//                    User user=new User();
//                    JSONObject jsonObject=jsonArray.getJSONObject(i);
//                    if (jsonObject.has("id_user")) {
//                        user.setId_user(Long.parseLong(jsonObject.getString("id_user")));
//                        accident_detail.setUser_id(user);
//                    }
//                    if(jsonObject.has("date"))
//                        accident_detail.setDate_create(jsonObject.getString("date"));
//                    accident_details.add(accident_detail);
////                    Log.d("ds_chitiet_tainan",accident_details.toString());
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            for (int i = 0; i < userList.size(); i++) {
//                User user = userList.get(i);
////                if(user.getId_user_type().equals("2"))
//                viDo = user.getLat_PI();
//                kinhDo = user.getLong_PI();
//                LatLng loocation = new LatLng(viDo, kinhDo);
//               for(int j=0;j<accident_details.size();j++){
//                   if(userList.get(i).getId_user()==accident_details.get(j).getUser_id().getId_user()){
//                       try {
//
//                           mMap.addMarker(new MarkerOptions()
//                                   .position(loocation)
//                                   .title(user.getUser_name())
//                                   .snippet(String.valueOf(user.getUser_type().getId_user_type())))
//                                   .setIcon(icon);
//                       } catch (Exception e) {
//                           e.printStackTrace();
//                           Toast.makeText(getActivity(), "Xin hãy cập nhập Google Play Services", Toast.LENGTH_SHORT).show();
//                       }
//                   }
//               }
//            }
//
//        }
//
//        @Override
//        protected List<User> doInBackground(Void... params) {
//            List<User> userList = new ArrayList<>();
//            try {
//
//                URL url = new URL(SystemUtils.getServerBaseUrl() + "users");
//                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
//                InputStreamReader inStreamReader = new InputStreamReader(connect.getInputStream(), "UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(inStreamReader);
//                StringBuilder builder = new StringBuilder();
//                String line = bufferedReader.readLine();
//                while (line != null) {
//                    builder.append(line);
//                    line = bufferedReader.readLine();
//                }
//                JSONObject jsonObject = new JSONObject(builder.toString());
//                JSONObject _embeddedObject = jsonObject.getJSONObject("_embedded");
//                JSONArray usersJSONArray = _embeddedObject.getJSONArray("users");
////                Log.d("jsonObj", jsonObject.toString());
//                for (int i = 0; i < usersJSONArray.length(); i++) {
//                    User user1 = new User();
//                    JSONObject jsonObj = usersJSONArray.getJSONObject(i);
//                    if (jsonObj.has("id_user"))
//                        user1.setId_user(Long.parseLong((jsonObj.getString("id_user"))));
//                    if (jsonObj.has("username"))
//                        user1.setUser_name(jsonObj.getString("username"));
//                    if (jsonObj.has("token"))
//                        user1.setToken(jsonObj.getString("token"));
//                    if (jsonObj.has("password"))
//                        user1.setPassword(jsonObj.getString("password"));
//                    if (jsonObj.has("long_PI"))
//                        user1.setLong_PI(jsonObj.getDouble("long_PI"));
//                    if (jsonObj.has("lat_PI"))
//                        user1.setLat_PI(jsonObj.getDouble("lat_PI"));
//                    if (jsonObj.has("id_user_type")) {
//                        String user_type = jsonObj.getString("id_user_type");
//                        User_Type user_type1 = new User_Type();
//                        try {
//                            JSONObject jsonObject1 = new JSONObject(user_type);
//                            if (jsonObject1.has("id_user_type"))
//                                user_type1.setId_user_type(jsonObject1.getLong("id_user_type"));
//                            if (jsonObject1.has("name_user_type"))
//                                user_type1.setName_user_type(jsonObject1.getString("name_user_type"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        user1.setUser_type(user_type1);
//                    }
//                    Log.d("User1", user1.toString());
//                    userList.add(user1);
//                }
//            } catch (Exception ex) {
//                Log.e("LOI ", ex.toString());
//            }
//            return userList;
//        }
//    }

}