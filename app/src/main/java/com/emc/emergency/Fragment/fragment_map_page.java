package com.emc.emergency.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.emc.emergency.MainMenuActivity;
import com.emc.emergency.R;
import com.emc.emergency.model.Accident;
import com.emc.emergency.model.MessageEvent;
import com.emc.emergency.model.Route;
import com.emc.emergency.utils.DirectionFinder;
import com.emc.emergency.utils.DirectionFinderListener;
import com.emc.emergency.utils.GPSTracker;
import com.emc.emergency.utils.SystemUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link .} interface
 * to handle interaction events.
 * Use the {@link fragment_map_page#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_map_page extends Fragment implements OnMapReadyCallback, LocationListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "vido";
    private static final String ARG_PARAM2 = "kinhdo";
    LocationManager locationManager;
    GoogleMap mMap;
    OnMapReadyCallback onMapReadyCallback;
    ArrayList<Accident> accidentList;
    double lat = 0;
    double lon = 0;
    double viDo;
    double kinhDo;
    String moTa, diaChi;

    private GoogleMap map;
    // TODO: Rename and change types of parameters
    private double mParam1;
    private double mParam2;
    String mprovider;
    MapView mapView;
    // XU LY NUT VE DUONG
//    private EditText etOrigin;
//    private EditText etDestination;

    Button btnVeDuong;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;


    private onFragmentMapInteraction mListener;

    public fragment_map_page() {
        // Required empty public constructor
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
        btnVeDuong= (Button) view.findViewById(R.id.btnVeDuong);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        accidentList = new ArrayList<>();
//        new GetAccidents(getActivity(), accidentList).execute();
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


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LatLng myLocation = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title("Bạn đang ở đây !!")
                .snippet("You are here !!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    class GetAccidents extends AsyncTask<Void, Void, ArrayList<Accident>> {
        Activity activity;
        ArrayList<Accident> arrAccidents;
//    AccidentAdapter accidentsAdapter;

        public GetAccidents(Activity activity, ArrayList<Accident> arrAccidents) {
            this.activity = activity;
            this.arrAccidents = arrAccidents;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrAccidents.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<Accident> accidents) {
            super.onPostExecute(accidents);
//        arrAccidents.clear();
            arrAccidents.addAll(accidents);

            for (int i = 0; i < arrAccidents.size(); i++) {
                viDo = Double.parseDouble(String.valueOf(arrAccidents.get(i).getLong_AC()));
                kinhDo = Double.parseDouble(String.valueOf(arrAccidents.get(i).getLat_AC()));
//                moTa=arrAccidents.get(i).getDescription_AC();
//                diaChi=arrAccidents.get(i).getAddress();
                LatLng loocation = new LatLng(viDo, kinhDo);
                  mMap.addMarker(new MarkerOptions()
                        .position(loocation)
                        .title(arrAccidents.get(i).getDescription_AC())
                        .snippet(arrAccidents.get(i).getAddress()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loocation, 13));
            }
        }

        @Override
        protected ArrayList<Accident> doInBackground(Void... params) {
            ArrayList<Accident> ds = new ArrayList<>();
            try {
                URL url = new URL(SystemUtils.getServerBaseUrl()+"accidents");
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                InputStreamReader inStreamReader = new InputStreamReader(connect.getInputStream(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                JSONObject jsonObj = new JSONObject(builder.toString());
                JSONObject _embeddedObject = jsonObj.getJSONObject("_embedded");
                JSONArray accidentsSONArray = _embeddedObject.getJSONArray("accidents");

                Log.d("JsonObject", _embeddedObject.toString());
                Log.d("JsonArray", accidentsSONArray.toString());

                for (int i = 0; i < accidentsSONArray.length(); i++) {
                    Accident accident = new Accident();
                    JSONObject jsonObject = accidentsSONArray.getJSONObject(i);
                    if (jsonObject == null) continue;
                    if (jsonObject.has("description_AC"))
                        accident.setDescription_AC(jsonObject.getString("description_AC"));
                    if (jsonObject.has("date_AC"))
                        accident.setDate_AC(jsonObject.getString("date_AC"));
                    if (jsonObject.has("long_AC"))
                        accident.setLong_AC((float) jsonObject.getDouble("long_AC"));
                    if (jsonObject.has("lat_AC"))
                        accident.setLat_AC((float) jsonObject.getDouble("lat_AC"));
                    if (jsonObject.has("status_AC"))
                        accident.setStatus_AC(jsonObject.getString("status_AC"));
                    if (jsonObject.has("adress"))
                        accident.setAddress(jsonObject.getString("adress"));
                    // Log.d("Accident", accident.toString());
                    ds.add(accident);
                    // Log.d("DS", ds.toString());
                }
                Log.d("ds", ds.toString());
            } catch (Exception ex) {
                Log.e("LOI ", ex.toString());
            }
            return ds;
        }
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

    @Subscribe(threadMode = ThreadMode.POSTING,priority = 1)
    public void MapListerner(MessageEvent event) {
        if(event.type.equals(SystemUtils.TAG_LOAD_MAP)){

        }

        if(event.type.equals(SystemUtils.TAG_GO_MAP)) {

            Accident accident = new Gson().fromJson(event.message,Accident.class);
                    LatLng lat_1 = new LatLng(accident.getLong_AC(), accident.getLat_AC());
            Toast.makeText(this.getContext(),accident.getDescription_AC(),Toast.LENGTH_SHORT).show();

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

}
