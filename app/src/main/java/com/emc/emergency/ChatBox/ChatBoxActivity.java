package com.emc.emergency.ChatBox; /**
 * Copyright Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import com.bumptech.glide.request.RequestOptions;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.emc.emergency.Helper.Model.UserJoined;

import com.emc.emergency.Helper.Utils.Utility;
import com.emc.emergency.Main_Menu.MainMenuActivity;
import com.emc.emergency.R;

import com.emc.emergency.Helper.Model.Accident;
import com.emc.emergency.Helper.Model.Message;
import com.emc.emergency.Helper.Services.fragment_play_video;
import com.emc.emergency.Helper.Utils.GPSTracker;
import com.emc.emergency.Helper.Utils.SystemUtils;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.CameraFragmentApi;

import com.github.florent37.camerafragment.PreviewActivity;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentControlsAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultAdapter;

import com.github.florent37.camerafragment.listeners.CameraFragmentStateAdapter;
import com.github.florent37.camerafragment.widgets.RecordButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appindexing.Action;

import com.google.firebase.appindexing.FirebaseUserActions;

import com.google.firebase.appindexing.builders.Actions;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatBoxActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, fragment_map_page.onFragmentMapInteraction, fragment_play_video.OnFragmentInteractionListener {

    private static final String FRAGMENT_TAG = "fragment_tag";
    private static final String FRIENDLY_MSG_LENGTH = "100";
    private static final int REQUEST_PREVIEW_CODE = 1001;

    @Override
    public void onFragmentMapInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        return Actions.newView("ChatBox", "http://[ENTER-YOUR-URL-HERE]");
    }

    @Override
    public void onStart()  {
        try {
            super.onStart();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error, please access chatbox through 'Accident Near You'", Toast.LENGTH_SHORT).show();
            this.finish();
        }

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

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
        TextView messengerTextView;
        ImageView messengerImageView;
        ImageView btnPlayVideo;
        FrameLayout frameLayout;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (ImageView) itemView.findViewById(R.id.messengerImageView);
            btnPlayVideo = (ImageView) itemView.findViewById(R.id.btnPlayVideo);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.frameImage);

        }
    }

    private String Type_User = "victim";
    private static final String TAG = "ChatBoxActivity";
    public static final String IMAGE_STORE = "images";
    public static final String VIDEO_STORE = "videos";

    public static final String MESSAGES_CHILD = "messages";
    private static final int REQUEST_INVITE = 1;
    private static final int REQUEST_IMAGE = 2;
    private static final int REQUEST_CAMERA = 3;

    public static final int DEFAULT_MSG_LENGTH_LIMIT = 100;
    public static final String ANONYMOUS = "anonymous";
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private static final String MESSAGE_URL = "http://friendlychat.firebase.google.com/message/";
    private static final String LOADING_IMAGE_URL = "https://media.giphy.com/media/3oEjI6SIIHBdRxXI40/giphy.gif";
    final private String TYPE_HELPER = "helper";
    final private String TYPE_VICTIM = "victim";


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType mediaType = MediaType.parse("text/uri-list");


    String id_user = "ID_USER";
    private String mId_user;
    private String mUsername;
    private String mPhotoUrl;
    private String id_victim;
    private SharedPreferences mSharedPreferences, mSharedPreferences2,preferences1,preferences3,preferences4;


    private RecordButton recordButton;
    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    private ProgressBar mProgressBar;
    private Toolbar toolbar;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAnalytics mFirebaseAnalytics;

    private EditText mMessageEditText;
    private ImageView mAddMessageImageView;
    //    private FrameLayout mframeImage;
//

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private RequestOptions options;
    SwipeButton swipeButton;
    Double longitude, latitude;
    Accident accident;
    Accident accident2;
    Response postResponse, putResponse;
    MaterialDialog dialog;
    ProgressDialog progressDialog;
    String id_AC = "";
    String AccidentKey = "";
    String UserJoinedKey = "";
    //    private String AccidentKey_noti="";
    public static final String ACCIDENTS_CHILD = "accidents";
    String response;
//    private DatabaseReference mDatabase1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        progressDialog = ProgressDialog.show(this, getString(R.string.progress_dialog_loading), getString(R.string.load_data_from_server));

        dialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog_chatbox)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.signInAnonymously();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        //Lấy tọa độ hiện tại đang đứng
        loadLocation();

        //load Intent về để xử lý, lúc này quyết định type của user
        prepareAccidentRoom();


        //Kiểm tra người vào chat này là ai, nếu là victim thì tạo acccident mới
        if (Type_User.equals(TYPE_VICTIM)) {

            //Tạo mới accident trên serverSpring + Firebase
            createAccidentOnServer();

        }

        //Khởi tạo từng control trong activity
        prepareControl();

        /* Đổ fragment (map + camera) vào activity */
        buildFragment();

        //Đổ tin nhăn vào recycleview
        LoadMessage();

        //Khởi tạo các thành phần của firebase
        initFirebase();

        // Bắt sự kiện cho từng control
        onControl();


    }

    private void initFirebase() {
        // Initialize Firebase Measurement.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Initialize Firebase Remote Config.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // Define Firebase Remote Config Settings.
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(true)
                        .build();

        // Tạo remoteconfig cho độ dài tin nhắn, có thể dùng remote config trên firebase để quản lý
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put("friendly_msg_length", 100L);

        // Apply config settings and default values.
        mFirebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings);
        mFirebaseRemoteConfig.setDefaults(defaultConfigMap);

        // Fetch remote config.
        fetchConfig();


    }

    private void onControl() {
        mMessageEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(
                        mSharedPreferences.getInt(
                                FRIENDLY_MSG_LENGTH,
                                DEFAULT_MSG_LENGTH_LIMIT))});

        //todo [bookmark] Khi thay đổi text trong messageEditText
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //Khi click nút image thì bắn intent lấy hình trong thư viện
        mAddMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo if api<19 thì bắn intent khác
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        // TODO: [bookmark] them message mới vào firebase
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Tạo lop message chưa thong tin cơ ban
                Message Message = new Message(mMessageEditText.getText().toString(),
                        mUsername,
                        mPhotoUrl, null, mId_user);
//                Log.d("messageImage", Message.toString());

                mFirebaseDatabaseReference.
                        child(ACCIDENTS_CHILD).
                        child(AccidentKey).
                        child(MESSAGES_CHILD).push().setValue(Message);
                mMessageEditText.setText("");
                mFirebaseAnalytics.logEvent(MESSAGE_SENT_EVENT, null);
            }
        });

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        onClickedCameraButton();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takeVideoIntent, REQUEST_CAMERA);
                    }
                }
            }
        });
        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                if (Type_User.equals("victim")) {
                    sendDoneToAccident();
                }
                Intent intent = new Intent(ChatBoxActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();


            }

        });


        if (dialog.isShowing()) dialog.dismiss();
        if (progressDialog.isShowing()) progressDialog.dismiss();


    }


    /**
     * Khi là android K trở lên => dùng camera fragment và bắt URI ở đây
     */
    private void onClickedCameraButton() {
        final CameraFragmentApi cameraFragment = getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.takePhotoOrCaptureVideo
                    (new CameraFragmentResultAdapter() {
                         @Override
                         public void onVideoRecorded(String filePath) {
                             Uri uri = Uri.fromFile(new File(filePath));
                             uploadVideoFromFilePath(uri);


                         }

                         @Override
                         public void onPhotoTaken(byte[] bytes, String filePath) {
                             Toast.makeText(getBaseContext(), "onPhotoTaken " + filePath, Toast.LENGTH_SHORT).show();

                         }
                     },
                            "/storage/self/primary",
                            "photo0");
        }


    }

    private void uploadVideoFromFilePath(final Uri uri) throws NullPointerException {
        Message tempMessage = new Message(null, mUsername, mPhotoUrl, SystemUtils.LOADING_IMAGE_URL, mId_user);
        // tạo mới một key trong message
        mFirebaseDatabaseReference.child(ACCIDENTS_CHILD)
                .child(AccidentKey).child(MESSAGES_CHILD)
                .push().setValue(tempMessage,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                        // nêu database không có lỗi
                        if (databaseError == null) {
                            // getkey mới
                            String key = databaseReference.getKey();
                            StorageReference storageReference =
                                    FirebaseStorage.getInstance().getReference().child(VIDEO_STORE)
                                            .child(key).child(uri.getLastPathSegment());
                            putImageInStorage(storageReference, uri, key);
                        } else {
                            Log.w(TAG, "Unable to write message to database.",
                                    databaseError.toException());
                        }
                    }
                });

    }


    private void loadLocation() {
        GPSTracker gps = new GPSTracker(ChatBoxActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
//            Latitude=gps.getLatitude();
            longitude = gps.getLongitude();
//            Longtitude=gps.getLongitude();
        }
    }

    private void LoadMessage() {
//        Log.d("BeforeParseAccidentKey", AccidentKey);
        //todo [bookmark] Load dữ liệu chat cũ đổ vào recycleview chat
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.
                        child(ACCIDENTS_CHILD).
                        child(AccidentKey).
                        child(MESSAGES_CHILD)) {

            @Override
            protected Message parseSnapshot(DataSnapshot snapshot) throws DatabaseException {
                Message Message = super.parseSnapshot(snapshot);
                if (Message != null) {
                    Message.setId(snapshot.getKey());
//                    Log.d("parseSnapshot", Message.toString());

                }
                return Message;

            }

            // Bắt sự kiện hiển thị message khi có tin nhắn mới
            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder,
                                              Message Message,
                                              int position) {
                Log.d("populateViewHolder", Message.toString());
                //todo tạo logic để bật tắt mProgressBar
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                if (Message.getText() != null) { // Nếu là text thường
                    viewHolder.messageTextView.setText(Message.getText());
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                    viewHolder.messageImageView.setVisibility(ImageView.GONE);
                    viewHolder.btnPlayVideo.setVisibility(ImageView.GONE);
                    viewHolder.frameLayout.setVisibility(ViewGroup.GONE);


                } else { // nếu là image/video
                    String imageUrl = Message.getImageUrl();
                    if (imageUrl.startsWith("gs://")) { // Nếu image từ firebase

                        StorageReference storageReference =
                                FirebaseStorage.getInstance()
                                        .getReferenceFromUrl(imageUrl);

                        storageReference.getDownloadUrl()
                                .addOnCompleteListener(
                                        new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()) {
                                                    final String downloadUrl = task.getResult().toString();
                                                    if (downloadUrl != null)
                                                        options = new RequestOptions()
                                                                .placeholder(R.mipmap.ic_camera)
                                                                .error(R.drawable.rect_error)
                                                                .priority(Priority.HIGH);

                                                    Glide.with(viewHolder.messageImageView.getContext())
                                                            .load(downloadUrl).apply(options)
                                                            .into(viewHolder.messageImageView);

                                                } else {
                                                    Log.w(TAG, "Getting download url was not successful.",
                                                            task.getException());
                                                }
                                            }
                                        });
                    } else {
                        // Do lấy # downloadurl về không còn //gs nên hình và video sẽ vô đây

                        options = new RequestOptions()
                                .fitCenter()
                                .placeholder(R.mipmap.ic_image)
                                .error(R.drawable.rect_error)
                                .priority(Priority.HIGH);
                        // Nếu hình từ chỗ khác
                        Glide.with(viewHolder.messageImageView.getContext())
                                .load(Message.getImageUrl()).apply(options)
                                .into(viewHolder.messageImageView);

                        final String downloadUrl = Message.getImageUrl();
                        if (downloadUrl != null && !downloadUrl.equals("")) {
                            if (downloadUrl.contains("videos")) {
                                viewHolder.btnPlayVideo.setVisibility(ImageView.VISIBLE);
                                if (!viewHolder.btnPlayVideo.isShown()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        viewHolder.btnPlayVideo.setElevation(2L);
                                    }
                                }
                                viewHolder.btnPlayVideo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // todo fragment show video
                                        Intent intent = new Intent(ChatBoxActivity.this, VideoPlayActivity.class);
                                        intent.putExtra(SystemUtils.VideoUrl, downloadUrl);
                                        startActivity(intent);


                                    }
                                });
                            }
                        }

                    }

                    viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                    viewHolder.messageTextView.setVisibility(TextView.GONE);
                }

                // Load vào avatar
                viewHolder.messengerTextView.setText(Message.getName());
                if (Message.getPhotoUrl() == null) {
                    viewHolder.messengerImageView.
                            setImageDrawable(
                                    ContextCompat.
                                            getDrawable(ChatBoxActivity.this,
                                                    R.drawable.ic_people_black_48dp));
                } else {
                    options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile3)
                            .error(R.drawable.ic_people_black_48dp)
                            .priority(Priority.HIGH);

                    Glide.with(ChatBoxActivity.this)
                            .load(Message.getPhotoUrl()).apply(options)
                            .into(viewHolder.messengerImageView);
                }

            }
        };


        //todo [bookmark] Khi chat insert thêm vào recycleview
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int MessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (MessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);


    }


    /**
     * Lấy intent về để kiểm tra có phải người giúp đỡ không.
     */
    private void prepareAccidentRoom() {
        mSharedPreferences2 = getSharedPreferences(SystemUtils.PI, Context.MODE_PRIVATE);
        mUsername = mSharedPreferences2.getString(SystemUtils.NAME_PI, ANONYMOUS);
        mPhotoUrl = mSharedPreferences2.getString(SystemUtils.AVATAR_PI, "");
//        Log.d("mUsername", mUsername);
//        Log.d("mPhotoUrl", mPhotoUrl);

        mSharedPreferences = getSharedPreferences(id_user, Context.MODE_PRIVATE);
        mId_user = String.valueOf(mSharedPreferences.getInt(SystemUtils.ID_USER, -1));

        if (mPhotoUrl.equals("") || mPhotoUrl.equals("null") || mPhotoUrl == null)
            mPhotoUrl = SystemUtils.DefaultAvatar;

        Intent intent = getIntent();
        Utility.dumpIntent(intent,"CHATBOXACTIVITY");
        if (intent != null) {
            //nếu intent gửi tới có kiểu user là type_helper
            if (intent.hasExtra("type") || intent.getAction() != null) {
                if (intent.getStringExtra("type").equals(TYPE_HELPER)) {
                    Type_User = TYPE_HELPER;
//                    Log.d("Type_User", Type_User);
                    AccidentKey = intent.getStringExtra("FirebaseKey");
                    id_victim = intent.getStringExtra(SystemUtils.id_victim);

                    preferences1 = getSharedPreferences("ACCIDENT_KEY_NOTI", MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = preferences1.edit();
                    editor1.putString("accident_key_noti", AccidentKey);
                    editor1.putString("Type_active", "Active");
                    editor1.apply();
//                    Log.d("AccidentKey", AccidentKey);
                    id_AC = intent.getStringExtra("id_AC");

                    preferences4 = getSharedPreferences("ID_ACC", MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = preferences4.edit();
                    editor4.putString("id_acc",id_AC);
                    editor4.apply();

                    SendtoActionOnServer();
                    SendtoActionOnFirebase();
                    SendMessageJoinToServer(AccidentKey, mUsername);
                }
            }
//            if(intent.getAction()!=null) {
//                if (intent.getAction().equals(TYPE_HELPER)) {
//                    Type_User = TYPE_HELPER;
//                    Log.d("Type_User", Type_User);
//                    AccidentKey = intent.getStringExtra("FirebaseKey");
//                    Log.d("AccidentKey", AccidentKey);
//                    id_AC = intent.getStringExtra("id_AC");
//                }
//            }
        }

    }

    // TODO: 24/7/2017
    private void SendMessageJoinToServer(String accidentKey, String mUsername) {
        // Tạo lop message chưa thong tin cơ ban
        Message Message = new Message(mUsername + " đã tham gia",
                mUsername,
                mPhotoUrl, null, mId_user);
//        Log.d("messageImage", Message.toString());

        mFirebaseDatabaseReference.child(ACCIDENTS_CHILD).child(accidentKey).child(MESSAGES_CHILD).push().setValue(Message);
    }

    private void SendtoActionOnServer() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm:ss a", Locale.US);
        String currentDateTime = dateFormat.format(date);
//        Log.d("currentDateTime",currentDateTime);
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"id_user\": \"" + mId_user + "\",\n\t\"id_AC\": \"" + id_AC + "\",\n\t\"date\": \"" + currentDateTime + "\"\n}");
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl() + "accident/join")
                .post(body)
                .build();
        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                Response response = client.newCall(request).execute();
//                Log.d("Reponse_1",response.body().string());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Khởi tạo các control cơ bản
     */
    private void prepareControl() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAddMessageImageView = (ImageView) findViewById(R.id.addMessageImageView);
        mSendButton = (Button) findViewById(R.id.sendButton);

        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        mMessageEditText = (EditText) findViewById(R.id.messageEditText);

        recordButton = (RecordButton) findViewById(R.id.record_button);

        swipeButton = (SwipeButton) findViewById(R.id.SwipeButton);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    /**
     * Đổ Fragment Map vào acitivity
     */
    private void buildFragment() {
        fragment_map_page fragment_map_page = new fragment_map_page();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, fragment_map_page).commit();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Configuration.Builder builder = new Configuration.Builder();
            builder .setCamera(Configuration.CAMERA_FACE_REAR)
                    .setMediaAction(Configuration.MEDIA_ACTION_VIDEO)
                    .setMediaQuality(Configuration.MEDIA_QUALITY_LOW)
                    .setFlashMode(Configuration.FLASH_MODE_OFF);

            final CameraFragment cameraFragment = CameraFragment.newInstance(builder.build());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameCamera, cameraFragment, FRAGMENT_TAG)
                    .commitAllowingStateLoss();

            cameraFragment.setStateListener(new CameraFragmentStateAdapter() {
                @Override
                public void onCameraSetupForVideo() {
                    super.onCameraSetupForVideo();
                    recordButton.displayVideoRecordStateReady();

                }

                @Override
                public void onRecordStateVideoReadyForRecord() {
                    super.onRecordStateVideoReadyForRecord();
                    recordButton.setActivated(true);
                    recordButton.displayVideoRecordStateReady();

                }

                @Override
                public void onRecordStateVideoInProgress() {
                    super.onRecordStateVideoInProgress();
                    recordButton.displayVideoRecordStateInProgress();


                }

                @Override
                public void onStartVideoRecord(File outputFile) {
                    super.onStartVideoRecord(outputFile);
                }

                @Override
                public void onStopVideoRecord() {
                    try {
                        super.onStopVideoRecord();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            cameraFragment.setControlsListener(new CameraFragmentControlsAdapter() {
                            @Override
                            public void lockControls() {
                                recordButton.setEnabled(false);

                            }

                            @Override
                            public void unLockControls() {
                                recordButton.setEnabled(true);

                            }

                            @Override
                            public void allowRecord(boolean allow) {
                                recordButton.setEnabled(allow);
                            }

                            @Override
                            public void setMediaActionSwitchVisible(boolean visible) {
                            }
                        });


        }
    }


    /**
     * Tạo accident mới trên firebase
     */
    private void createAccidentOnFirebase() {
        // Chuẩn bị folder cho accident và lấy key về
        accident2.setFirebaseKey(AccidentKey);

        preferences3 = getSharedPreferences("ACCIDENT_KEY", MODE_PRIVATE);
        SharedPreferences.Editor editor3 = preferences3.edit();
        editor3.putString("accident_key", AccidentKey);

        editor3.commit();


        // Bỏ accident mới vào key vừa tạo trên firebase
        mFirebaseDatabaseReference.child(ACCIDENTS_CHILD).child(AccidentKey).setValue(accident2);
        // cập nhập firebasekey cho accident vừa tạo
//        Log.d("AccidentKey",AccidentKey);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (Type_User.equals(SystemUtils.TYPE_HELPER))
            inflater.inflate(R.menu.chat_box_menu, menu);
        else inflater.inflate(R.menu.chat_box_menu_user, menu);

        return true;
    }


    private void causeCrash() {
        throw new NullPointerException("Fake null pointer exception");
    }


    // Fetch the config to determine the allowed length of messages.
    public void fetchConfig() {
        long cacheExpiration = 3600; // 1 hour in seconds
        // If developer mode is enabled reduce cacheExpiration to 0 so that each fetch goes to the
        // server. This should not be used in release builds.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Make the fetched config available via FirebaseRemoteConfig get<type> calls.
                        mFirebaseRemoteConfig.activateFetched();
                        applyRetrievedLengthLimit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // There has been an error fetching the config
                        Log.w(TAG, "Error fetching config", e);
                        applyRetrievedLengthLimit();
                    }
                });
    }

    // sau khi lấy hình trả về
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());
                    // new một message
                    Message tempMessage =
                            new Message(null, mUsername, mPhotoUrl, LOADING_IMAGE_URL, mId_user);

                    // tạo mới một key trong message
                    mFirebaseDatabaseReference
                            .child(ACCIDENTS_CHILD)
                            .child(AccidentKey)
                            .child(MESSAGES_CHILD)
                            .push()
                            .setValue(tempMessage,
                            new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError,
                                                       DatabaseReference databaseReference) {
                                    // nêu database không có lỗi
                                    if (databaseError == null) {
                                        // getkey mới
                                        String key = databaseReference.getKey();
                                        StorageReference storageReference =
                                                FirebaseStorage.getInstance()
                                                        .getReference()
                                                        .child(IMAGE_STORE)
                                                        .child(key)
                                                        .child(uri.getLastPathSegment());
                                        putImageInStorage(storageReference, uri, key);
                                    } else {
                                        Log.w(TAG, "Unable to write message to database.",
                                                databaseError.toException());
                                    }
                                }
                            });
                }
            }
        }
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            uploadVideoFromFilePath(videoUri);
        }
    }

    /**
     * Lưu image vào store
     *
     * @param storageReference đường dẫn đến nơi muốn lưu
     * @param uri
     * @param key
     */
    @SuppressWarnings("VisibleForTests")
    private void putImageInStorage(StorageReference storageReference,
                                   Uri uri, final String key) {

        // gủi file lên store và lấy về kết quả
        storageReference.putFile(uri)
                .addOnCompleteListener(ChatBoxActivity.this,
                        new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                // nếu up thành công
                                if (task.isSuccessful()) {
                                    //tạo message mới
                                    Message Message =
                                            new Message(null, mUsername,
                                                    mPhotoUrl,
                                                    task.getResult().getDownloadUrl().toString(), mId_user);
                                    Log.d("messageImage", Message.toString());
                                    mFirebaseDatabaseReference
                                            .child(ACCIDENTS_CHILD)
                                            .child(AccidentKey)
                                            .child(MESSAGES_CHILD)
                                            .child(key)
                                            .setValue(Message);
//                                    Log.d("putImageInStorage", Message.toString());

                                } else {
                                    Log.w(TAG, "Image upload task was not successful.",
                                            task.getException());
                                }
                            }
                        });
    }

    private void SendtoActionOnFirebase() {
        UserJoinedKey = mFirebaseDatabaseReference.child(ACCIDENTS_CHILD)
                .child(AccidentKey)
                .child("User joined").push().getKey();
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
                                .child(AccidentKey)
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

    /**
     * Apply retrieved length limit to edit text field. This result may be fresh from the server or it may be from
     * cached values.
     */
    private void applyRetrievedLengthLimit() {
        Long friendly_msg_length = mFirebaseRemoteConfig.getLong("friendly_msg_length");
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(friendly_msg_length.intValue())});
        Log.d(TAG, "FML is: " + friendly_msg_length);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    /**
     * Gửi dữ liệu mới lên server
     */
    private void createAccidentOnServer() {
        accident = new Accident();
        accident2 = new Accident();
        AccidentKey = mFirebaseDatabaseReference.child(ACCIDENTS_CHILD).push().getKey();

        accident.setDescription_AC("Tai nạn");


        //TODO thêm locate sau này, sử dụng giờ hệ thống
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        String currentDateandTime = sdf.format(new Date());
        accident.setDate_AC(currentDateandTime);

        accident.setStatus_AC("Pending");

        accident.setLat_AC(latitude);

        accident.setLong_AC(longitude);
        accident.setFirebaseKey(AccidentKey);
        accident.setRequest_AC(true);
//        Log.d("createAccidentOnServer", accident.toString());

        // convert object to json
//        Log.d("afterOnFirebase", accident.toString());

        Gson gson = new Gson();
        String json = gson.toJson(accident);

        PostAccident example = new PostAccident();
        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                response = example.post(SystemUtils.getServerBaseUrl() + "accidents", json);
//                Log.d("response", response);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);


            for (int i = 0; i < jsonObject.length(); i++) {
                if (jsonObject == null) continue;
                if (jsonObject.has("id_AC")) {
                    accident2.setId_AC(Long.parseLong(jsonObject.getString("id_AC")));
                    preferences4 = getSharedPreferences("ID_ACC", MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = preferences4.edit();
                    editor4.putString("id_acc", String.valueOf(accident2.getId_AC()));
                    editor4.apply();
                }
                if (jsonObject.has("description_AC"))
                    accident2.setDescription_AC(jsonObject.getString("description_AC"));
                if (jsonObject.has("date_AC"))
                    accident2.setDate_AC(jsonObject.getString("date_AC"));
                if (jsonObject.has("long_AC"))
                    accident2.setLong_AC(jsonObject.getDouble("long_AC"));
                if (jsonObject.has("lat_AC"))
                    accident2.setLat_AC(jsonObject.getDouble("lat_AC"));
                if (jsonObject.has("status_AC"))
                    accident2.setStatus_AC(jsonObject.getString("status_AC"));
                if (jsonObject.has("adress"))
                    accident2.setAddress(jsonObject.getString("adress"));
            }
            createAccidentOnFirebase();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Log.d("accident2", accident2.toString());

        /**
         * tiếp tục gởi put lên  server để xác định user nào tạo tai nạn nào
         */
        PutRelation putRel = new PutRelation();

        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                String response2 = putRel.put(SystemUtils.getServerBaseUrl() + "accidents/" + accident2.getId_AC() + "/id_user",
                        SystemUtils.getServerBaseUrl() + "users/" + mId_user);
                Log.d("response2", response2);
                Log.d("PutUrl", SystemUtils.getServerBaseUrl() + "users/" + mId_user);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Gửi accident lên server
     */
    public class PostAccident {

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Log.d("postURl", url);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try {
                postResponse = client.newCall(request).execute();
                //Log.d("postResponse",postResponse.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return postResponse.body().string();

        }
    }

    /**
     * Gởi put lên server để tạo relation giữa user và accident
     */
    public class PutRelation {

        OkHttpClient client = new OkHttpClient();

        String put(String url, String txt) throws IOException {
            RequestBody body = RequestBody.create(mediaType, txt);
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .addHeader("content-type", "text/uri-list")
                    .addHeader("cache-control", "no-cache")
                    .build();
            try {
                putResponse = client.newCall(request).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return putResponse.body().string();

        }
    }

    private CameraFragmentApi getCameraFragment() {
        return (CameraFragmentApi) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    private void sendDoneToAccident() {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, "{\n\t\"status_AC\" : \"Done\"\n}");
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl() + "accidents/" + accident2.getId_AC())
                .patch(body)
                .addHeader("content-type", "application/json;charset=utf-8")
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please swpipe button to cancle accident", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Toast.makeText(this,
                    "Volunteer still going to your location, please comeback swipe button to make accident finish ",
                    Toast.LENGTH_SHORT).show();
        }
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.Exit: {
                swipeButton.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.ShowMedi: {
                ShowMedicalInfo();
                break;
            }
            case R.id.ReportFake: {
                try {
                    ReportFakeAccident();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.ReportDone: {
                try {
                    ReportDoneAccident();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void ReportFakeAccident() throws IOException {
        OkHttpClient client = new OkHttpClient();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm:ss a", Locale.ENGLISH);
        String date = df.format(Calendar.getInstance().getTime());
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"id_user\":\"" + mId_user + "\",\n  \"id_AC\":\"" + id_AC +
                "\",\n   \"id_action_type\":\"3\",\n  \"date\":\"" + date + "\" \n}");

        Log.d("Body", "{\n  \"id_user\":" + mId_user + ",\n  \"id_AC\":" + id_AC +
                ",\n   \"id_action_type\":\"3\",\n  \"date\":" + date + "\n \n}");
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl() + "accident/action")
                .post(body)
                .addHeader("content-type", "text/plain")
                .build();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Response response = client.newCall(request).execute();

    }

    private void ReportDoneAccident() throws IOException {
        OkHttpClient client = new OkHttpClient();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm:ss a", Locale.ENGLISH);
        String date = df.format(Calendar.getInstance().getTime());
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"id_user\":\"" + mId_user + "\",\n  \"id_AC\":\"" + id_AC +
                "\",\n   \"id_action_type\":\"2\",\n  \"date\":\"" + date + "\" \n}");
        Log.d("Body", "{\n  \"id_user\":" + mId_user + ",\n  \"id_AC\":" + id_AC +
                ",\n   \"id_action_type\":\"4\",\n  \"date\":" + date + "\n \n}");
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl() + "accident/action")
                .post(body)
                .addHeader("content-type", "text/plain")
                .build();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Response response = client.newCall(request).execute();
    }

    private void ShowMedicalInfo() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
           if (prev != null) {
               ft.remove(prev);
           }
           ft.addToBackStack(null);
            if(id_victim!=null||!id_victim.equals("")) {
                // Create and show the dialog.
                fragment_dialog_medical_info newFragment = fragment_dialog_medical_info.newInstance(Long.parseLong(id_victim));
                newFragment.show(fm, "dialog");
            }
            else {

            }

    }

}
