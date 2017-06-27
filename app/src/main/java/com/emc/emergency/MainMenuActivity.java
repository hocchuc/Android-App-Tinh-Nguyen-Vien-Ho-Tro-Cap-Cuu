package com.emc.emergency;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.emc.emergency.Chat.IRequestListener;
import com.emc.emergency.Fragment.fragment_countdown;

import com.emc.emergency.Fragment.fragment_menu_page;
import com.emc.emergency.Login.LoginActivity;
import com.emc.emergency.model.Accident;
import com.emc.emergency.model.Personal_Infomation;
import com.emc.emergency.model.Route;

import com.emc.emergency.model.User;
import com.emc.emergency.model.User_Type;
import com.emc.emergency.utils.DirectionFinder;
import com.emc.emergency.utils.DirectionFinderListener;
import com.emc.emergency.utils.GPSTracker;
import com.emc.emergency.utils.SystemUtils;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainMenuActivity extends AppCompatActivity
        implements fragment_menu_page.onFragmentMenu1Interaction
        , OnMapReadyCallback, DirectionFinderListener, GoogleMap.OnMarkerClickListener
        , fragment_countdown.OnFragmentInteractionListener, IRequestListener {


    Toolbar toolbar;
    private AccountHeader headerResult = null;
    public Drawer result = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;

    String idUser_UID;
    Long id_usertype;
    int id_user;
    //-----------------------------------------------------------------------
//    private MarkerOptions myMarkerOption;
//    public Marker myMarker;
//    LatLng myLocation;
    GoogleMap mMap;
    double viDo, kinhDo, viDoUser, kinhDoUser;
    String description, address;
    Button btnVeDuong;
    ImageButton btnToGMap;
    double latitude = 0;
    double longitude = 0;
    SharedPreferences sharedPreferences, sharedPreferences1, sharedPreferences2,sharedPreferences3;
    /* biến dùng cho firebase */
    FirebaseStorage storage;
    StorageReference storageRef ;
    Uri uriAvatar = null;
    private StorageReference imagesRef;

    //    private Button btnFindPath;
    // XU LY NUT VE DUONG
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private SupportMapFragment mapFragment;

    private ArrayList<Accident> arrayAccident;
    private ArrayList<User> arrayUser;

    Personal_Infomation pi;
    //------------------------
//    private LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        addControls();

        BuildDrawer(savedInstanceState);

        setLoadImageLogic();

        BuildFragment();

        addEvents();
    }

    /**
     * Khi location thay doi
     */
    private void LocationChange() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                mDatabase.child(idUser_UID).child("lat_PI").setValue(latitude);
                mDatabase.child(idUser_UID).child("long_PI").setValue(longitude);
                // thay đổi market dựa trên location của bản thân
//                myMarker.setPosition(latLng);

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    /**
     * Load hinh cho drawer
     */
    private void setLoadImageLogic() {
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.profile3)
                        .error(R.drawable.material_drawer_circle_mask)
                        .priority(Priority.HIGH);
                Glide.with(imageView.getContext()).load(uri).apply(options).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {

            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });

    }

    private void addEvents() {
        // yeu cau quyen doi voi cac thiet chay android M tro len
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123
            );
        }

//        Log.d("UID after put", idUser_UID);
        GPSTracker gps = new GPSTracker(MainMenuActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }

        LocationChange();



        if (id_usertype == 2) {
            new GetAccidents(MainMenuActivity.this, arrayAccident).execute();
        } else {
            new GetAllUsers(MainMenuActivity.this, arrayUser).execute();
            new GetAccidents(MainMenuActivity.this, arrayAccident).execute();

        }
        btnVeDuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        btnToGMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dùng navigation turn by turn
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude + "&avoid=tf");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                // kiểm tra có cài map chưa, có ứng dụng nào có thể nhận intent hay không ?
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }
    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        arrayAccident = new ArrayList<>();
        arrayUser = new ArrayList<>();
        btnVeDuong = (Button) findViewById(R.id.btnVeDuong);
        btnToGMap = (ImageButton) findViewById(R.id.btnDirectionToGmap);

        // UID để tìm key đổ vào locationlistener
        sharedPreferences = getApplicationContext().getSharedPreferences("UID", MODE_PRIVATE);
        idUser_UID = sharedPreferences.getString("iduser_uid", "");

        sharedPreferences1 = getApplicationContext().getSharedPreferences("User", MODE_PRIVATE);
        id_usertype = sharedPreferences1.getLong("id_user_type", -1);
        //Log.d("IDusertype",id_usertype.toString());

        sharedPreferences2 = getApplicationContext().getSharedPreferences("ID_USER", MODE_PRIVATE);
        id_user = sharedPreferences2.getInt("id_user", -1);


        sharedPreferences3 = getApplicationContext().getSharedPreferences(SystemUtils.PI, MODE_PRIVATE);
        if(sharedPreferences3.contains(SystemUtils.PI)) {
            //sharedPreferences3 = getApplicationContext().getSharedPreferences(SystemUtils.PI, MODE_PRIVATE);
            pi.setName_PI(sharedPreferences3.getString(SystemUtils.NAME_PI, ""));
            pi.setAvatar(sharedPreferences3.getString(SystemUtils.AVATAR_PI, ""));
            pi.setEmail_PI(sharedPreferences3.getString(SystemUtils.EMAIL_PI, ""));
            Log.d("EmailPI",pi.getEmail_PI());
        }
        else {
            GetPersonalInfo();

        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Lấy user đang đăng nhập về
     */
    private void GetPersonalInfo() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl() + "users/" + id_user + "/personal_Infomation")
                .get()
                .build();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                Response response = client.newCall(request).execute();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.body().string());
                    pi = new Personal_Infomation();
                    if (jsonObj.has("work_location"))
                        pi.setWork_location(jsonObj.getString("work_location"));
                    if (jsonObj.has("birthday"))
                        pi.setBirthday(jsonObj.getString("birthday"));
                    if (jsonObj.has("phone_PI"))
                        pi.setPhone_PI(jsonObj.getString("phone_PI"));
                    if (jsonObj.has("sex__PI"))
                        pi.setSex__PI(jsonObj.getBoolean("sex__PI"));
                    if (jsonObj.has("email_PI"))
                        pi.setEmail_PI(jsonObj.getString("email_PI"));
                    if (jsonObj.has("address_PI"))
                        pi.setAddress_PI(jsonObj.getString("address_PI"));
                    if (jsonObj.has("personal_id"))
                        pi.setPersonal_id(jsonObj.getString("personal_id"));
                    if (jsonObj.has("name_PI"))
                        pi.setName_PI(jsonObj.getString("name_PI"));
                    if(jsonObj.has("avatar"))
                        pi.setAvatar(jsonObj.getString("avatar"));
                    if (jsonObj.has("id_PI")) {
                        pi.setId_PI(jsonObj.getLong("id_PI"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // luu thong tin vua load duoc vao SharedPreferences
                SharedPreferences preferences1 = getSharedPreferences(SystemUtils.PI, MODE_PRIVATE);
                SharedPreferences.Editor editor1 = preferences1.edit();
                editor1.putString(SystemUtils.NAME_PI, pi.getName_PI());
                editor1.putString(SystemUtils.EMAIL_PI, pi.getEmail_PI());
                editor1.putString(SystemUtils.AVATAR_PI, pi.getAvatar());
                editor1.putString(SystemUtils.EMAIL_PI, pi.getEmail_PI());
                editor1.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void BuildFragment() {

        fragment_menu_page fragment_menu_page1 = new fragment_menu_page();
        fragment_menu_page1.setArguments((getIntent()).getExtras());
        // Add the fragment to the 'fragment_container' FrameLayout
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
        IProfile profile = new ProfileDrawerItem();

        if(pi.getAvatar()!=null) {
              profile = new ProfileDrawerItem()
                    .withName(pi.getName_PI())
                    .withEmail(pi.getEmail_PI())
                    .withIcon(pi.getAvatar());
        }

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withProfileImagesVisible(true)
                .addProfiles(
                        profile
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
                                .withIdentifier(7),
                        new SecondaryDrawerItem()
                                .withName("Log Out")
                                .withIcon(R.drawable.ic_power_settings_new_black_36dp)
                                .withIdentifier(8)
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
                                intent = new Intent(MainMenuActivity.this, AccidentActivity.class);
                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(MainMenuActivity.this, Personal_Infomation.class);
                            } else if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(MainMenuActivity.this, MainMenuActivity.class);
                            } else if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(MainMenuActivity.this, MainMenuActivity.class);
                            } else if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(MainMenuActivity.this, MainMenuActivity.class);
                            }
                            else if (drawerItem.getIdentifier() == 8) {
                                intent.putExtra(SystemUtils.TYPE,SystemUtils.TYPE_LOGOUT);
                                intent = new Intent(MainMenuActivity.this, LoginActivity.class);
                            }
                            if(intent!=null) startActivity(intent);

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
                .addView(view
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
        //add the values which need to be saved from the crossFader to the bundle
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (SystemUtils.getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
            inflater.inflate(R.menu.embedded, menu);
            menu.findItem(R.id.menu_1)
                    .setIcon(new IconicsDrawable(this
                            , GoogleMaterial.Icon.gmd_sort)
                            .color(Color.WHITE).actionBar());
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        View mSwipeButton = findViewById(R.id.btnSwipe2Confirm);
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        }
        // neu swipe button dang mo, nguoi dung nhan backpress
        if (!mSwipeButton.isActivated()) {
            mSwipeButton.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentMenu1Interaction(Uri uri) {

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
            btnToGMap.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//
//        googleMap.setOnMarkerClickListener(this);
        LatLng myLocation = new LatLng(latitude, longitude);

//        myMarkerOption = new MarkerOptions()
//                .position(myLocation)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                .title("Bạn đang ở đây !!")
//                .snippet("You are here !!");
//
//        myMarker = mMap.addMarker(myMarkerOption);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        //tat/mo dau cham mau xanh
//        mMap.setMyLocationEnabled(true);
//        //tat/mo nut hien thi GPS
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        // tinh trang giao thong
//        googleMap.setTrafficEnabled(true);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setPadding(0, 400, 0, 0);
            // tinh trang giao thong
            googleMap.setTrafficEnabled(true);
        }

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
        LatLng position = marker.getPosition();
        viDo = Double.parseDouble(String.valueOf(position.latitude));
        kinhDo = Double.parseDouble(String.valueOf(position.longitude));
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(String message) {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        return Actions.newView("MainMenu", "http://[ENTER-YOUR-URL-HERE]");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().start(getIndexApiAction());
    }

    @Override
    public void onStop() {

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().end(getIndexApiAction());
        super.onStop();
    }

    private class GetAccidents extends AsyncTask<Void, Void, ArrayList<Accident>> {
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

            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin);

            for (int i = 0; i < arrAccidents.size(); i++) {
                viDo = Double.parseDouble(String.valueOf(arrAccidents.get(i).getLat_AC()));
                kinhDo = Double.parseDouble(String.valueOf(arrAccidents.get(i).getLong_AC()));
                LatLng loocation = new LatLng(viDo, kinhDo);
                try {

                    mMap.addMarker(new MarkerOptions()
                            .position(loocation)
                            .title(arrAccidents.get(i).getDescription_AC())
                            .snippet(arrAccidents.get(i).getAddress())
                            .icon(icon));

                    // tắt chuyển camera tới các tai nạn vừa load
                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loocation, 13));

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Xin hãy cập nhập Google Play Services", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected ArrayList<Accident> doInBackground(Void... params) {
            ArrayList<Accident> ds = new ArrayList<>();
            try {
                URL url = new URL(SystemUtils.getServerBaseUrl() + "accidents");
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
                        accident.setLong_AC(jsonObject.getDouble("long_AC"));
                    if (jsonObject.has("lat_AC"))
                        accident.setLat_AC(jsonObject.getDouble("lat_AC"));
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

    private class GetAllUsers extends AsyncTask<Void, Void, ArrayList<User>> {
        Activity activity;
        ArrayList<User> arrUser;

        public GetAllUsers(Activity activity, ArrayList<User> arrUser) {
            this.activity = activity;
            this.arrUser = arrUser;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrUser.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);
            arrUser.addAll(users);
            View markerView = LayoutInflater.from(getBaseContext()).inflate(R.layout.view_custom_marker, null);
            final ImageView image = (ImageView) markerView.findViewById(R.id.profile_image);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(image));

//            Log.d("UserSize", String.valueOf(arrUser.size()));
            for (int i = 0; i < arrUser.size(); i++) {
                if (arrUser.get(i).getUser_type().getId_user_type() == 2) {
                    viDoUser = Double.parseDouble(String.valueOf(arrUser.get(i).getLat_PI()));
                    kinhDoUser = Double.parseDouble(String.valueOf(arrUser.get(i).getLong_PI()));
                    LatLng loocation = new LatLng(viDoUser, kinhDoUser);
                    try {

                        mMap.addMarker(new MarkerOptions()
                                .position(loocation)
                                .title(arrUser.get(i).getUser_name())
                                .snippet(String.valueOf(arrUser.get(i).getId_user())))
                                .setIcon(icon);
                        // tắt chuyển camera tới các tai nạn vừa load
                        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loocation, 13));
                        imagesRef = storageRef.child("images/"+arrUser.get(i).getId_user()+".jpg");

                        imagesRef.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        uriAvatar = uri;
                                        Log.d("uriAvatar",uri.toString());
                                        RequestOptions options = new RequestOptions()
                                                .centerCrop()
                                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                                .placeholder(R.drawable.profile3)
                                                .error(R.drawable.material_drawer_circle_mask)
                                                .priority(Priority.HIGH);
                                        try {
                                            Glide.with(getBaseContext()).load(uriAvatar).apply(options).into(image);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(activity, "Xin hãy cập nhập Google Play Services", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }

        @Override
        protected ArrayList<User> doInBackground(Void... params) {
            ArrayList<User> userList = new ArrayList<>();
            try {

                URL url = new URL(SystemUtils.getServerBaseUrl() + "users");
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                InputStreamReader inStreamReader = new InputStreamReader(connect.getInputStream(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                JSONObject jsonObject = new JSONObject(builder.toString());
                JSONObject _embeddedObject = jsonObject.getJSONObject("_embedded");
                JSONArray usersJSONArray = _embeddedObject.getJSONArray("users");
//                Log.d("jsonObj", jsonObject.toString());
                for (int i = 0; i < usersJSONArray.length(); i++) {
                    User user1 = new User();
                    JSONObject jsonObj = usersJSONArray.getJSONObject(i);
                    if (jsonObj.has("id_user"))
                        user1.setId_user(Integer.parseInt((jsonObj.getString("id_user"))));
                    if (jsonObj.has("username"))
                        user1.setUser_name(jsonObj.getString("username"));
                    if (jsonObj.has("token"))
                        user1.setToken(jsonObj.getString("token"));
                    if (jsonObj.has("password"))
                        user1.setPassword(jsonObj.getString("password"));
                    if (jsonObj.has("long_PI"))
                        user1.setLong_PI(jsonObj.getDouble("long_PI"));
                    if (jsonObj.has("lat_PI"))
                        user1.setLat_PI(jsonObj.getDouble("lat_PI"));
                    if (jsonObj.has("id_user_type")) {
                        String user_type = jsonObj.getString("id_user_type");
                        User_Type user_type1 = new User_Type();
                        try {
                            JSONObject jsonObject1 = new JSONObject(user_type);
                            if (jsonObject1.has("id_user_type"))
                                user_type1.setId_user_type(jsonObject1.getLong("id_user_type"));
                            if (jsonObject1.has("name_user_type"))
                                user_type1.setName_user_type(jsonObject1.getString("name_user_type"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        user1.setUser_type(user_type1);
                    }
//                    Log.d("User1", user1.toString());
                    userList.add(user1);
                }
                Log.d("DSUser1", userList.toString());
            } catch (Exception ex) {
//                Log.e("LOI ", ex.toString());
            }
            return userList;
        }
    }

    //Convert view into bitmap, them vien ngoai.
    private Bitmap getMarkerBitmapFromView(ImageView view) {

        View customMarkerView = view;
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

}
