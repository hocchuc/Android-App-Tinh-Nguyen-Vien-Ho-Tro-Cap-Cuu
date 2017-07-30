package com.emc.emergency.Main_Menu;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.emc.emergency.Accidents_List.AccidentActivity;
import com.emc.emergency.Helper.AsyncTask.SendLocationToServer;
import com.emc.emergency.Helper.Model.User_Type;
import com.emc.emergency.Helper.Services.IRequestListener;

import com.emc.emergency.Login.LoginActivity;
import com.emc.emergency.Personal_Information.Personal_Inf_Activity;
import com.emc.emergency.R;
import com.emc.emergency.Helper.Model.Accident;
import com.emc.emergency.Helper.Model.Personal_Information;
import com.emc.emergency.Helper.Model.Route;

import com.emc.emergency.Helper.Model.User;
import com.emc.emergency.Helper.AsyncTask.GetAllAccident;
import com.emc.emergency.Helper.AsyncTask.GetAllUser;
import com.emc.emergency.Helper.AsyncTask.ReturnDataAllAccident;
import com.emc.emergency.Helper.AsyncTask.ReturnDataAllUser;
import com.emc.emergency.Helper.Utils.DirectionFinder;
import com.emc.emergency.Helper.Utils.DirectionFinderListener;
import com.emc.emergency.Helper.Utils.GPSTracker;
import com.emc.emergency.Helper.Utils.SystemUtils;


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
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;

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
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainMenuActivity extends AppCompatActivity
        implements fragment_menu_page.onFragmentMenu1Interaction
        , OnMapReadyCallback, DirectionFinderListener
        , fragment_countdown.OnFragmentInteractionListener, IRequestListener
        , ReturnDataAllUser
        , ReturnDataAllAccident {

    MaterialDialog mProgressDialog;

    Toolbar toolbar;
    private AccountHeader headerResult = null;
    public Drawer result = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;

    //    String idUser_UID;
    Long id_usertype;
    int id_user;
    //-----------------------------------------------------------------------
    GoogleMap mMap;
    double viDo = -1, kinhDo = -1, viDoAC, kinhDoAC, viDoUser, kinhDoUser;
    String description, address;
    Button btnVeDuong;
    ImageButton btnToGMap, imgbtnRefresh;
    double latitude = 0;
    double longitude = 0;
    SharedPreferences sharedPreferences1, sharedPreferences2, sharedPreferences3;
    /* biến dùng cho firebase */
    FirebaseStorage storage;
    StorageReference storageRef;

    // XU LY NUT VE DUONG
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private SupportMapFragment mapFragment;
    Personal_Information pi;
    User user1;
    User_Type user_type;
    //------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        addControls();
        setLoadImageLogic();
        BuildDrawer(savedInstanceState);

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


                SendLocationToServer SendLocationToServer = new SendLocationToServer(MainMenuActivity.this, id_user, longitude, latitude);
                SendLocationToServer.excute();

//                LatLng latLng = new LatLng(latitude, longitude);

//                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
//                mDatabase.orderByChild("id_user").equalTo(id_user).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                            idUser_UID = childSnapshot.getKey();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
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
//        Log.d("UID after put", idUser_UID);

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
        imgbtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                if (id_usertype == 2) {
                    new GetAllAccident(MainMenuActivity.this).execute();
                } else {
                    GetAllUser getAllUser = new GetAllUser(MainMenuActivity.this);
                    getAllUser.execute();
                    new GetAllAccident(MainMenuActivity.this).execute();

                }
            }
        });

        if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    private void addControls() {
        btnVeDuong = (Button) findViewById(R.id.btnVeDuong);
        btnToGMap = (ImageButton) findViewById(R.id.btnDirectionToGmap);
        imgbtnRefresh = (ImageButton) findViewById(R.id.imgBtnRefresh);
        pi = new Personal_Information();
        user1 = new User();

        progressDialog = ProgressDialog.show(this, getString(R.string.progress_dialog_loading),
                getString(R.string.load_data_from_server));

        mProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog_chatbox)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

//        if(isOnline()==false){
//            Logout();
//        }

        sharedPreferences1 = getApplicationContext().getSharedPreferences("User", MODE_PRIVATE);
        id_usertype = sharedPreferences1.getLong("id_user_type", -1);
//        Log.d("IDusertype", id_usertype.toString());

        sharedPreferences2 = getApplicationContext().getSharedPreferences("ID_USER", MODE_PRIVATE);
        id_user = sharedPreferences2.getInt("id_user", -1);

        GetPersonalInfo();
        GetUser();

//        sharedPreferences3 = getApplicationContext().getSharedPreferences(SystemUtils.PI, MODE_PRIVATE);
//        if (sharedPreferences3.contains(SystemUtils.PI)) {
//            //sharedPreferences3 = getApplicationContext().getSharedPreferences(SystemUtils.PI, MODE_PRIVATE);
//            pi.setName_PI(sharedPreferences3.getString(SystemUtils.NAME_PI, ""));
//            pi.setAvatar(sharedPreferences3.getString(SystemUtils.AVATAR_PI, ""));
//            pi.setEmail_PI(sharedPreferences3.getString(SystemUtils.EMAIL_PI, ""));
//        } else {
//            GetPersonalInfo();
//
//        }

//        setSupportActionBar(toolbar);
        mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        GPSTracker gps = new GPSTracker(MainMenuActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }

        LocationChange();

        if (id_usertype == 3) {
            new GetAllUser(MainMenuActivity.this).execute();
        } else {
            new GetAllUser(MainMenuActivity.this).execute();
            new GetAllAccident(MainMenuActivity.this).execute();

        }
    }

    /**
     * Lấy thông tin tài khoản 1 user
     */
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

    /**
     * Lấy thông tin cá nhân user đang đăng nhập về
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
                    pi = new Personal_Information();
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
                    if (jsonObj.has("avatar"))
                        pi.setAvatar(jsonObj.getString("avatar"));
                    if (jsonObj.has("id_PI")) {
                        pi.setId_PI(jsonObj.getLong("id_PI"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                // luu thong tin vua load duoc vao SharedPreferences
                SharedPreferences preferences1 = getSharedPreferences(SystemUtils.PI, MODE_PRIVATE);
                SharedPreferences.Editor editor1 = preferences1.edit();
                if (pi.getId_PI() != null) {
                    editor1.putLong(SystemUtils.ID_PI, pi.getId_PI());
                    editor1.putString(SystemUtils.NAME_PI, pi.getName_PI());
                    editor1.putString(SystemUtils.EMAIL_PI, pi.getEmail_PI());
                    editor1.putString(SystemUtils.AVATAR_PI, pi.getAvatar());
                    editor1.putString(SystemUtils.EMAIL_PI, pi.getEmail_PI());
                }
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.Emergency_SOS);

        // Create a few sample profile
        IProfile profile;
        profile = new ProfileDrawerItem()
                .withName(pi.getName_PI())
                .withEmail(user1.getUser_type().getName_user_type())
                .withIcon(pi.getAvatar());


        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
//                .withProfileImagesVisible(true)
                .withOnlySmallProfileImagesVisible(true)
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
                                .withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.personal_infomation)
                                .withIcon(FontAwesome.Icon.faw_user_circle)
                                .withIdentifier(3),
                        new SecondaryDrawerItem()
                                .withName(R.string.Logout)
                                .withIcon(R.drawable.icon_shutdown)
                                .withIdentifier(4)
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
                                intent = new Intent(MainMenuActivity.this, Personal_Inf_Activity.class);
                            } else if (drawerItem.getIdentifier() == 4) {
                                mProgressDialog.show();
                                if (!progressDialog.isShowing())
                                    progressDialog.show(MainMenuActivity.this, getString(R.string.cleanning), getString(R.string.we_are_cleanning));
                                Logout();
                                intent = new Intent(MainMenuActivity.this, LoginActivity.class);
                                intent.putExtra(SystemUtils.ACTION, SystemUtils.TYPE_LOGOUT);
                            }
                            if (intent != null) startActivity(intent);

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

    private void Logout() {
        removeSharedPreferences();

        try {
            removeTokenFromServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
        if (progressDialog.isShowing()) progressDialog.dismiss();
        Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
        intent.putExtra(SystemUtils.ACTION, SystemUtils.TYPE_LOGOUT);
        finish();
    }

    /**
     * Xóa SharedPreferences và set isLogined = false
     */
    private void removeSharedPreferences() {
        SharedPreferences preferences2 = getSharedPreferences(SystemUtils.USER, MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences2.edit();

        editor2.putBoolean("isLogined", false);

        editor2.putString("username", "");
        editor2.putString("password", "");
        editor2.putString("token", "");
        editor2.putString("lat_PI", "");
        editor2.putString("long_PI", "");
        editor2.putLong("id_user_type", 3);
        editor2.putString("name_user_type", "");
        editor2.commit();
    }

    /**
     * Xóa token của device trên server Spring
     */
    private void removeTokenFromServer() throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"token\":\"\"\n}");
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl() + "users/" + id_user)
                .patch(body)
                .addHeader("content-type", "application/json;charset=utf-8")
                .build();

        Log.d("removeToken", SystemUtils.getServerBaseUrl() + "users/" + id_user);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) Log.d("removeTokenResponge", "SUCCESS");
        }
    }


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
    public void onBackPressed() {
        //handle the back press close the drawer first and if the drawer is closed, close the activity
        View mSwipeButton = findViewById(R.id.btnSwipe2Confirm);
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        }
        // neu swipe button dang mo, nguoi dung nhan backpress thi se ẩn swipebutton
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
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_start))
                    .title(route1.startAddress)
                    .position(route1.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_end))
                    .title(description)
                    .snippet(address)
                    .position(route1.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.RED).
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

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            //mMap.setPadding(0, 400, 0, 0);
            // tinh trang giao thong
            googleMap.setTrafficEnabled(true);
        }
    }

//    public Boolean isOnline() {
//        try {
//            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
//            int returnVal = p1.waitFor();
//            boolean reachable = (returnVal == 0);
//            return reachable;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return false;
//    }

    private void sendRequest() {
        if (kinhDo == -1 && viDo == -1) {
            Toast.makeText(this, "Chưa chọn vị trí cần dẫn đường.", Toast.LENGTH_SHORT).show();

        } else {
            String origin = latitude + "," + longitude;
            String destination = viDo + "," + kinhDo;
            try {
                new DirectionFinder(MainMenuActivity.this, origin, destination).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
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


    @Override
    public void handleReturnDataAllUser(final ArrayList<User> arrUser) {
        for (int i = 0; i < arrUser.size(); i++) {
            if (arrUser.get(i).getUser_type().getName_user_type().equals("volunteer") || arrUser.get(i).getUser_type().getName_user_type().equals("admin")) {
                viDoUser = Double.parseDouble(String.valueOf(arrUser.get(i).getLat_PI()));
                kinhDoUser = Double.parseDouble(String.valueOf(arrUser.get(i).getLong_PI()));
                final LatLng loocation = new LatLng(viDoUser, kinhDoUser);
                try {
                    if (arrUser.get(i).getToken() != null) {
                        final int j=i;
                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(arrUser.get(i).getToken())
                                .into(new SimpleTarget<Bitmap>(48,48) {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                                        Bitmap b = Bitmap.createScaledBitmap(resource, 32, 48, true);
                                        mMap.addMarker(new MarkerOptions()
                                                .position(loocation)
                                                .title(arrUser.get(j).getUser_name())
                                                .snippet(String.valueOf(arrUser.get(j).getId_user())))
                                                .setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromBitmap(resource)));
                                    }
                                });
                    } else {
                        mMap.addMarker(new MarkerOptions()
                                .position(loocation)
                                .title(arrUser.get(i).getUser_name())
                                .snippet(String.valueOf(arrUser.get(i).getId_user())))
                                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_user_sos));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainMenuActivity.this, "Xin hãy cập nhập Google Play Services", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Bitmap getMarkerBitmapFromBitmap(Bitmap bitmap) {

        View customMarkerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageBitmap(bitmap);
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

    @Override
    public void handleReturnDataAllAccident(ArrayList<Accident> arrAccident) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_sos);

        for (int i = 0; i < arrAccident.size(); i++) {
            if (arrAccident.get(i).getStatus_AC().equals("Active")) {
                viDoAC = Double.parseDouble(String.valueOf(arrAccident.get(i).getLat_AC()));
                kinhDoAC = Double.parseDouble(String.valueOf(arrAccident.get(i).getLong_AC()));
                LatLng loocation = new LatLng(viDoAC, kinhDoAC);
                try {

                    mMap.addMarker(new MarkerOptions()
                            .position(loocation)
                            .title(arrAccident.get(i).getDescription_AC())
                            .snippet(arrAccident.get(i).getAddress())
                            .icon(icon));
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            LatLng position = marker.getPosition();
                            viDo = Double.parseDouble(String.valueOf(position.latitude));
                            kinhDo = Double.parseDouble(String.valueOf(position.longitude));
                            return false;
                        }
                    });
                    // tắt chuyển camera tới các tai nạn vừa load
                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loocation, 13));

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainMenuActivity.this, "Xin hãy cập nhập Google Play Services", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
