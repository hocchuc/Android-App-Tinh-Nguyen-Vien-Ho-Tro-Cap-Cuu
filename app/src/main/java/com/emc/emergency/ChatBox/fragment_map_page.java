package com.emc.emergency.ChatBox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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
    //    String moTa, diaChi;
    Accident accident;
    private final Handler mHandler;
    private Runnable mAnimation;
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
    private SharedPreferences sharedPreferences, sharedPreferences1, sharedPreferences2;
    String id_AC = "";
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
        Active = sharedPreferences2.getString("Type_active", "");
        AccidentKey_noti = sharedPreferences2.getString("accident_key_noti", "");

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        GetAccient();

        UserJoinedKey = mFirebaseDatabaseReference.child(ACCIDENTS_CHILD)
                .child(AccidentKey_noti)
                .child("User joined").push().getKey();
        if (Active.equals("Active"))
            SendtoActionOnFirebase();
        return view;
    }

    private void GetAccient() {
//        sharedPreferences = getActivity().getSharedPreferences("ID_ACC", MODE_PRIVATE);
//        id_AC = sharedPreferences.getString("id_acc", "");
//        Log.d("key_AC", id_AC);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl() + "accidents/" + id_AC)
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
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    accident = new Accident();
                    if (jsonObject.has("long_AC"))
                        accident.setLong_AC(jsonObject.getDouble("long_AC"));
                    if (jsonObject.has("lat_AC"))
                        accident.setLat_AC(jsonObject.getDouble("lat_AC"));
                    if (jsonObject.has("address"))
                        accident.setAddress(jsonObject.getString("address"));
                    Log.d("ds_1AC", accident.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

        BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.icon_sos);
        LatLng myLocation = new LatLng(lat, lon);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(myLocation)
                .icon(icon1)
                .title(accident.getAddress());
        Marker marker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        mMap.setMyLocationEnabled(false);

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
            addValueEventListener();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
//        mMap.setMyLocationEnabled(true);
    }

    private void addValueEventListener() {
        if (mMap == null) return;

        DatabaseReference ref1 = mFirebaseDatabaseReference.child(ACCIDENTS_CHILD)
                .child(AccidentKey_noti)
                .child("User joined");
        ref1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                UserJoined userJoined1 = dataSnapshot.getValue(UserJoined.class);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_user_sos);
                if (userJoined1 != null) {
                    LatLng loocation = new LatLng(userJoined1.getLat_userjoined(), userJoined1.getLong_userjoined());

                    mMap.addMarker(new MarkerOptions()
                            .position(loocation)
                            .title(String.valueOf(userJoined1.getName())))
//                                .setIcon(BitmapDescriptorFactory.fromBitmap(bmp));
                            .setIcon(icon);
                }
//                if (userJoined1 != null) {
//                    try {
//                        URL url = new URL(userJoined1.getAvatar());
//                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                        Bitmap b = Bitmap.createScaledBitmap(bmp, 48, 48, true);
//                        LatLng loocation = new LatLng(userJoined1.getLat_userjoined(), userJoined1.getLong_userjoined());
//
//                        mMap.addMarker(new MarkerOptions()
//                                .position(loocation)
//                                .title(String.valueOf(userJoined1.getName())))
//                                .setIcon(BitmapDescriptorFactory.fromBitmap(b));
////                        .setIcon(icon);
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        mMap.setMyLocationEnabled(true);
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void SendtoActionOnFirebase() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl() + "accident/GetAllUserJoined/" + id_AC)
                .get()
                .build();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                Response response = client.newCall(request).execute();
//                Log.d("responeUserJoined",response.body().string());
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        UserJoined userJoined = new UserJoined();
                        if (jsonObject.has("date"))
                            userJoined.setDate(jsonObject.getString("date"));
                        if (jsonObject.has("name"))
                            userJoined.setName(jsonObject.getString("name"));
                        if (jsonObject.has("id_user"))
                            userJoined.setUser_id(jsonObject.getLong("id_user"));
                        if (jsonObject.has("avatar"))
                            userJoined.setAvatar(jsonObject.getString("avatar"));
                        if (jsonObject.has("lat"))
                            userJoined.setLat_userjoined(jsonObject.getDouble("lat"));
                        if (jsonObject.has("long"))
                            userJoined.setLong_userjoined(jsonObject.getDouble("long"));

                        mFirebaseDatabaseReference.child(ACCIDENTS_CHILD)
                                .child(AccidentKey_noti)
                                .child("User joined").child(UserJoinedKey).setValue(userJoined);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
