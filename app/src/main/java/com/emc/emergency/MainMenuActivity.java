package com.emc.emergency;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import android.widget.TextView;
import android.widget.Toast;

import com.emc.emergency.Fragment.fragment_countdown;
import com.emc.emergency.Fragment.fragment_map_page;
import com.emc.emergency.Fragment.fragment_menu_page;
import com.emc.emergency.Fragment.fragment_menu_page2;
import com.emc.emergency.model.Accident;
import com.emc.emergency.model.Route;
import com.emc.emergency.utils.DirectionFinder;
import com.emc.emergency.utils.DirectionFinderListener;
import com.emc.emergency.utils.GPSTracker;
import com.emc.emergency.utils.SystemUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import devlight.io.library.ntb.NavigationTabBar;

public class MainMenuActivity extends AppCompatActivity
        implements fragment_menu_page.onFragmentMenu1Interaction
        , fragment_menu_page2.onFragmentMenu2Interaction
        ,OnMapReadyCallback, DirectionFinderListener, GoogleMap.OnMarkerClickListener
        , fragment_countdown.OnFragmentInteractionListener {



    Toolbar toolbar;
    private AccountHeader headerResult = null;
    public Drawer result = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;

    //    private ActionBarDrawerToggle toggle;
//    public ViewPager viewPager;
//    public FragmentManager fragment_manager;
//    private PagerAdapter pagerdApter;
//    DrawerLayout drawerLayout;
//    ViewPager pager_menu;
//    private static final String LOG_TAG = "Refresh";
//    private static final long PROFILE_SETTING = 10000;
//    private FloatingActionButton floatingActionButton;
    //-----------------------------------------------------------------------
    private GoogleMap mMap;
    ProgressDialog pdl;
    double viDo, kinhDo;
    String description, address;
    Button btnVeDuong;
    double latitude = 0;
    double longitude = 0;
    boolean flag = false;


    private Button btnFindPath;
    // XU LY NUT VE DUONG
//    private EditText etOrigin;
//    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private SupportMapFragment mapFragment;
    private ArrayList<Accident> arrayAccident;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu2);
        addControls();
        BuildDrawer(savedInstanceState);
        BuildFragment();
        addEvents();
    }

    private void addEvents() {
        GPSTracker gps = new GPSTracker(MainMenuActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        new GetAccidents(MainMenuActivity.this, arrayAccident).execute();

        btnVeDuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        arrayAccident = new ArrayList<>();
        btnVeDuong= (Button) findViewById(R.id.btnVeDuong);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void BuildFragment() {
        FragmentManager managerBot = getSupportFragmentManager();
        FragmentManager managerTop = getSupportFragmentManager();

//        fragment_map_page fragment_map = new fragment_map_page();
        fragment_menu_page fragment_menu_page1 = new fragment_menu_page();
        fragment_menu_page2 fragment_menu_page2 = new fragment_menu_page2();

//        fragment_map.setArguments(getIntent().getExtras());
        fragment_menu_page1.setArguments((getIntent()).getExtras());
        fragment_menu_page2.setArguments((getIntent()).getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragment_map, fragment_map).commit();


        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_menu, fragment_menu_page1).commit();


    }

    private void BuildDrawer(Bundle savedInstanceState) {
        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.Emergency_SOS);

        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem()
                .withName("Mike Penz")
                .withEmail("mikepenz@gmail.com")
                .withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460");
        final IProfile profile2 = new ProfileDrawerItem()
                .withName("Bernat Borras")
                .withEmail("alorma@github.com")
                .withIcon(Uri
                        .parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460"));

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile,
                        profile2
                )
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withDrawerLayout(R.layout.crossfade_drawer)
                .withDrawerWidthDp(72)
                .withGenerateMiniDrawer(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.main_menu_activity)
                                .withIcon(FontAwesome.Icon.faw_th)
                                .withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.accident_activity)
                                .withIcon(GoogleMaterial.Icon.gmd_error)
                                .withBadge("22").withBadgeStyle(new BadgeStyle(Color.RED, Color.RED))
                                .withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.personal_infomation)
                                .withIcon(FontAwesome.Icon.faw_user_circle)
                                .withIdentifier(3),
                        new PrimaryDrawerItem()
                                .withName(R.string.chat_room)
                                .withIcon(FontAwesome
                                .Icon.faw_comments)
                                .withIdentifier(4),
                        new PrimaryDrawerItem()
                                .withDescription("A more complex sample")
                                .withName(R.string.setting)
                                .withIcon(GoogleMaterial.Icon.gmd_settings_applications)
                                .withIdentifier(5),
                        new SectionDrawerItem().withName(R.string.other_resources),
                        new SecondaryDrawerItem()
                                .withName(R.string.framework)
                                .withIcon(FontAwesome.Icon.faw_github)
                                .withIdentifier(6),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_contact)
                                .withIcon(GoogleMaterial.Icon.gmd_format_color_fill)
                                .withTag("Bullhorn")
                                .withIdentifier(7)
                )
                // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(MainMenuActivity.this, MainMenuActivity.class);
                            } else if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(MainMenuActivity.this, MainMenuActivity.class);
                                startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(MainMenuActivity.this, MainMenuActivity.class);
                            } else if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(MainMenuActivity.this, MainMenuActivity.class);
                            } else if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(MainMenuActivity.this, MainMenuActivity.class);
                            } else if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(MainMenuActivity.this, MainMenuActivity.class);
                            }

                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        //get the CrossfadeDrawerLayout which will be used as alternative DrawerLayout for the Drawer
        crossfadeDrawerLayout = (CrossfadeDrawerLayout) result
                .getDrawerLayout();

        //define maxDrawerWidth
        crossfadeDrawerLayout
                .setMaxWidthPx(DrawerUIUtils
                .getOptimalDrawerWidth(this));

        //add second view (which is the miniDrawer)
        final MiniDrawer miniResult = result.getMiniDrawer();

        //build the view for the MiniDrawer
        View view = miniResult.build(this);

        //set the background of the MiniDrawer as this would be transparent
        view.setBackgroundColor(UIUtils.
                getThemeColorFromAttrOrRes(this
                        , com.mikepenz.materialdrawer.R.attr.material_drawer_background
                        , com.mikepenz.materialdrawer.R.color.material_drawer_background));

        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout
                .getSmallView()
                .addView( view
                        , ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.MATCH_PARENT);

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    result.getDrawerLayout()
                            .closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });
    }
    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem
                , CompoundButton buttonView
                , boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: "
                                + ((Nameable) drawerItem).getName()
                                + " - toggleChecked: "
                                + isChecked);
            } else {
                Log.i("material-drawer"
                        , "toggleChecked: " + isChecked);
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);

        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (SystemUtils.getScreenOrientation()== Configuration.ORIENTATION_LANDSCAPE) {
            inflater.inflate(R.menu.embedded, menu);
            menu.findItem(R.id.menu_1)
                    .setIcon(new IconicsDrawable(this
                            , GoogleMaterial.Icon.gmd_sort)
                            .color(Color.WHITE).actionBar());
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentMenu1Interaction(Uri uri) {

    }

    @Override
    public void onFragmentMenu2Interaction(Uri uri) {

    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {
        mMap.clear();
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route1 : route) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route1.startLocation, 13));
            ((TextView) findViewById(R.id.tvDuration)).setText(route1.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route1.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .title(route1.startAddress)
                    .position(route1.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(description)
                    .snippet(address)
                    .position(route1.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.GREEN).
                    width(10);

            for (int i = 0; i < route1.points.size(); i++)
                polylineOptions.add(route1.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setOnMarkerClickListener(this);
        LatLng myLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title("Bạn đang ở đây !!")
                .snippet("You are here !!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private void sendRequest() {
        //String origin = "10.738100, 106.677811";
        String origin = latitude + "," + longitude;
        String destination = viDo + "," + kinhDo;
        try {
            new DirectionFinder(MainMenuActivity.this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng position =marker.getPosition();
        Toast.makeText(
                MainMenuActivity.this,
                "Lat " + position.latitude + " "
                        + "Long " + position.longitude,
                Toast.LENGTH_LONG).show();
        viDo = Double.parseDouble(String.valueOf(position.latitude));
        kinhDo = Double.parseDouble(String.valueOf(position.longitude));
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
                URL url = new URL("https://app-tnv-ho-tro-cap-cuu.herokuapp.com/api/accidents");
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
}
