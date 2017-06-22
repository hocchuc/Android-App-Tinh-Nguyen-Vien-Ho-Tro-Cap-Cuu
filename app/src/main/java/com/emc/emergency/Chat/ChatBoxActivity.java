package com.emc.emergency.Chat; /**
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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emc.emergency.Fragment.fragment_map_page;
import com.emc.emergency.Login.LoginActivity;
import com.emc.emergency.R;
import com.emc.emergency.RequestRescueActivity;
import com.emc.emergency.model.Accident;
import com.emc.emergency.model.FriendlyMessage;
import com.emc.emergency.utils.GPSTracker;
import com.emc.emergency.utils.SystemUtils;
import com.emc.emergency.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.builders.PersonBuilder;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatBoxActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, fragment_map_page.onFragmentMapInteraction {

    @Override
    public void onFragmentMapInteraction(Uri uri) {

    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
        TextView messengerTextView;
        CircleImageView messengerImageView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
        }
    }
    private String Type_User = "victim";
    private static final String TAG = "MainActivity";
    public static final String IMAGE_STORE = "images";
    public static final String MESSAGES_CHILD = "messages";
    private static final int REQUEST_INVITE = 1;
    private static final int REQUEST_IMAGE = 2;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 100;
    public static final String ANONYMOUS = "anonymous";
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private static final String MESSAGE_URL = "http://friendlychat.firebase.google.com/message/";
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    final private String TYPE_HELPER = "helper";
    final private String TYPE_VICTIM = "victim";


    String id_user="ID_USER";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final MediaType mediaType = MediaType.parse("text/uri-list");
    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences,mSharedPreferences2;
    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder> mFirebaseAdapter;
    private ProgressBar mProgressBar;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAnalytics mFirebaseAnalytics;
    private EditText mMessageEditText;
    private ImageView mAddMessageImageView;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private GoogleApiClient mGoogleApiClient;
    Double longitude,latitude;
    Accident accident;
    Accident accident2;
    Response postResponse,putResponse;
    private  String AccidentKey = "" ;
    public static final String ACCIDENTS_CHILD = "accidents";
    private String response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        //load Intent về để xử lý
        prepareAccidentRoom();


        //Lấy tọa độ hiện tại đang đứng
        loadLocation();

        //Khởi tạo từng control trong activity, lấy sharedpreferent
        prepareControl();


        //Đổ fragment (map) vào activity
        buildFragment();

        //Kiểm tra người vào chat này là ai, nếu là victim thì tạo acccident mới
        if(Type_User.equals("victim")) {

            //Tạo mới accident trên serverSpring
            createAccidentOnServer();

            //Tạo mới accident trên firebase
          //  createAccidentOnFirebase();
        }


        //Khởi tạo các cài đặt của firebase
        initFirebase();

        //Đổ tin nhăn vào recycleview
        LoadMessage();

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

        // Define default config values. Defaults are used when fetched config values are not
        // available. Eg: if an error occurred fetching values from the server.
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put("friendly_msg_length", 10L);

        // Apply config settings and default values.
        mFirebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings);
        mFirebaseRemoteConfig.setDefaults(defaultConfigMap);

        // Fetch remote config.
        fetchConfig();


    }

    private void onControl() {

        mMessageEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(
                        mSharedPreferences.getInt(CodelabPreferences.FRIENDLY_MSG_LENGTH,
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
                FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(),
                        mUsername,
                        mPhotoUrl, null);
                Log.d("messageImage",friendlyMessage.toString());
                mFirebaseDatabaseReference.
                        child(ACCIDENTS_CHILD).
                        child(AccidentKey).
                        child(MESSAGES_CHILD).push().setValue(friendlyMessage);
                mMessageEditText.setText("");
                mFirebaseAnalytics.logEvent(MESSAGE_SENT_EVENT, null);
            }
        });

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

    }

    private void loadLocation() {
        GPSTracker gps = new GPSTracker(ChatBoxActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }

    }

    private void LoadMessage() {

        //todo [bookmark] Load dữ liệu chat cũ đổ vào recycleview chat
        mFirebaseAdapter = new FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(
                FriendlyMessage.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.
                        child(ACCIDENTS_CHILD).
                        child(AccidentKey).
                        child(MESSAGES_CHILD)) {

            @Override
            protected FriendlyMessage parseSnapshot(DataSnapshot snapshot) {
                FriendlyMessage friendlyMessage = super.parseSnapshot(snapshot);
                if (friendlyMessage != null) {
                    friendlyMessage.setId(snapshot.getKey());
                }
                Log.d("parseSnapshot",friendlyMessage.toString());

                return friendlyMessage;

            }

            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder,
                                              FriendlyMessage friendlyMessage,
                                              int position) {
                Log.d("populateViewHolder",friendlyMessage.toString());

                //todo tạo logic để bật tắt mProgressBar
                 mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                if (friendlyMessage.getText() != null) {
                    viewHolder.messageTextView.setText(friendlyMessage.getText());
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                    viewHolder.messageImageView.setVisibility(ImageView.GONE);
                } else {
                    String imageUrl = friendlyMessage.getImageUrl();
                    if (imageUrl.startsWith("gs://")) {

                        StorageReference storageReference =
                                FirebaseStorage.getInstance()
                                        .getReferenceFromUrl(imageUrl);

                        storageReference.getDownloadUrl()
                                .addOnCompleteListener(
                                        new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()) {
                                                    String downloadUrl = task.getResult().toString();
                                                    Glide.with(viewHolder.messageImageView.getContext())
                                                            .load(downloadUrl)
                                                            .into(viewHolder.messageImageView);
                                                } else {
                                                    Log.w(TAG, "Getting download url was not successful.",
                                                            task.getException());
                                                }
                                            }
                                        });
                    } else {
                        Glide.with(viewHolder.messageImageView.getContext())
                                .load(friendlyMessage.getImageUrl())
                                .into(viewHolder.messageImageView);
                    }
                    viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                    viewHolder.messageTextView.setVisibility(TextView.GONE);
                }


                viewHolder.messengerTextView.setText(friendlyMessage.getName());
                if (friendlyMessage.getPhotoUrl() == null) {
                    viewHolder.messengerImageView.
                            setImageDrawable(
                                    ContextCompat.
                                            getDrawable(ChatBoxActivity.this,
                                                    R.drawable.ic_people_black_48dp));
                } else {
                    Glide.with(ChatBoxActivity.this)
                            .load(friendlyMessage.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }

                if (friendlyMessage.getText() != null) {
                    // write this message to the on-device index
                    FirebaseAppIndex.getInstance().update(getMessageIndexable(friendlyMessage));
                }

                // log a view action on it
                FirebaseUserActions.getInstance().end(getMessageViewAction(friendlyMessage));
            }
        };


        //todo [bookmark] Khi chat insert thêm vào recycleview
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
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
        Intent intent = getIntent();
        if(intent.getStringExtra("type").equals(TYPE_HELPER)){
            Type_User = TYPE_HELPER;
            Log.d("Type_User",Type_User);
            AccidentKey = intent.getStringExtra("FirebaseKey");
            Log.d("AccidentKey",AccidentKey);

        }

        mSharedPreferences2 = getSharedPreferences(SystemUtils.PI, Context.MODE_PRIVATE);
        mUsername = mSharedPreferences2.getString(SystemUtils.NAME_PI,ANONYMOUS);
        mPhotoUrl = mSharedPreferences2.getString(SystemUtils.AVATAR_PI,"");
        Log.d("mUsername",mUsername);

        Log.d("mPhotoUrl",mPhotoUrl);



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

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mMessageEditText = (EditText) findViewById(R.id.messageEditText);



    }

    /**
     *  Đổ Fragment Map vào acitivity
     */
    private void buildFragment() {
        //FragmentManager managerTop = getSupportFragmentManager();
        fragment_map_page fragment_map_page = new fragment_map_page();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,fragment_map_page).commit();

    }
    private void onEvents() {


        //mFirebaseAnalytics.logEvent(MESSAGE_SENT_EVENT, null);
    }

    /**
     * Tạo accident mới trên firebase
     */
    private void createAccidentOnFirebase() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        // Chuẩn bị folder cho accident và lấy key về
        AccidentKey = mFirebaseDatabaseReference.child(ACCIDENTS_CHILD).push().getKey();
        accident.setFirebaseKey(AccidentKey);
        // Bỏ accident mới vào key vừa tạo trên firebase
        mFirebaseDatabaseReference.child(ACCIDENTS_CHILD).child(AccidentKey).setValue(accident);
        // cập nhập firebasekey cho accident vừa tạo
        Log.d("AccidentKey",AccidentKey);
    }

    //// TODO: 22-Jun-17 tìm hiểu ?
    private Action getMessageViewAction(FriendlyMessage friendlyMessage) {
        return new Action.Builder(Action.Builder.VIEW_ACTION)
                .setObject(friendlyMessage.getName(), MESSAGE_URL.concat(friendlyMessage.getId()))
                .setMetadata(new Action.Metadata.Builder().setUpload(false))
                .build();
    }
    //// TODO: 22-Jun-17 tìm hiểu ?
    private Indexable getMessageIndexable(FriendlyMessage friendlyMessage) {
        PersonBuilder sender = Indexables.personBuilder()
                .setIsSelf(mUsername.equals(friendlyMessage.getName()))
                .setName(friendlyMessage.getName())
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId() + "/sender"));

        PersonBuilder recipient = Indexables.personBuilder()
                .setName(mUsername)
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId() + "/recipient"));

        Indexable messageToIndex = Indexables.messageBuilder()
                .setName(friendlyMessage.getText())
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId()))
                .setSender(sender)
                .setRecipient(recipient)
                .build();

        return messageToIndex;
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
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invite_menu:
                sendInvitation();
                return true;
            case R.id.crash_menu:
                FirebaseCrash.logcat(Log.ERROR, TAG, "crash caused");
                causeCrash();
                return true;
            case R.id.sign_out_menu:
                mUsername = ANONYMOUS;
                mPhotoUrl = null;
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.fresh_config_menu:
                fetchConfig();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void causeCrash() {
        throw new NullPointerException("Fake null pointer exception");
    }

    private void sendInvitation() {
       /* Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);*/
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
                    FriendlyMessage tempMessage =
                            new FriendlyMessage(
                                    null,
                                    mUsername,
                                    mPhotoUrl,
                            LOADING_IMAGE_URL);

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

                               /*         StorageReference storageReference =
                                                FirebaseStorage.getInstance()
                                                        .getReference()
                                                        .child(IMAGE_STORE)
                                                        .child(key)
                                                        .child(uri.getLastPathSegment());*/
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
        } else if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Use Firebase Measurement to log that invitation was sent.
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "inv_sent");

                // Check how many invitations were sent and log.
              //  String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
               //Log.d(TAG, "Invitations sent: " + ids.length);
            } else {
                // Use Firebase Measurement to log that invitation was not sent
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "inv_not_sent");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, payload);

                // Sending failed or it was canceled, show failure message to the user
                Log.d(TAG, "Failed to send invitation.");
            }
        }
    }

    /**
     * Lưu image vào store
     * @param storageReference đường dẫn đến nơi muốn lưu
     * @param uri
     * @param key
     */@SuppressWarnings("VisibleForTests")
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
                            FriendlyMessage friendlyMessage =
                                    new FriendlyMessage(null, mUsername,
                                            mPhotoUrl,
                                            task.getResult().getDownloadUrl().toString());
                            // thử bỏ key
                            Log.d("messageImage",friendlyMessage.toString());
                            mFirebaseDatabaseReference
                                    .child(ACCIDENTS_CHILD)
                                    .child(AccidentKey)
                                    .child(MESSAGES_CHILD)
                                    .setValue(friendlyMessage);
                            Log.d("putImageInStorage",friendlyMessage.toString());

                        } else {
                            Log.w(TAG, "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
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
        mSharedPreferences = getSharedPreferences(id_user, Context.MODE_PRIVATE);
        final int id = mSharedPreferences.getInt(SystemUtils.ID_USER, -1);
        accident = new Accident();
        accident.setDescription_AC("Tai nạn");


        //TODO thêm locate sau này, sử dụng giờ hệ thống
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        String currentDateandTime = sdf.format(new Date());
        accident.setDate_AC(currentDateandTime);

        accident.setStatus_AC("Active");

        accident.setLat_AC(latitude);

        accident.setLong_AC(longitude);
        Log.d("createAccidentOnServer",accident.toString());

        createAccidentOnFirebase();
        // convert object to json
        Log.d("afterOnFirebase",accident.toString());

        Gson gson = new Gson();
        String json = gson.toJson(accident);

        PostAccident example = new PostAccident();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                response = example.post(SystemUtils.getServerBaseUrl() + "accidents", json);
                Log.d("response",response);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        accident2 = new Accident();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);


            for (int i = 0; i < jsonObject.length(); i++) {
                if (jsonObject == null) continue;
                if (jsonObject.has("id_AC"))
                    accident2.setId_AC(Long.parseLong(jsonObject.getString("id_AC")));
                if (jsonObject.has("description_AC"))
                    accident2.setDescription_AC(jsonObject.getString("description_AC"));
                if (jsonObject.has("date_AC"))
                    accident2.setDate_AC(jsonObject.getString("date_AC"));
                if (jsonObject.has("long_AC"))
                    accident2.setLong_AC(jsonObject.getDouble("long_AC"));
                if (jsonObject.has("lat_AC"))
                    accident2.setLat_AC( jsonObject.getDouble("lat_AC"));
                if (jsonObject.has("status_AC"))
                    accident2.setStatus_AC(jsonObject.getString("status_AC"));
                if (jsonObject.has("adress"))
                    accident2.setAddress(jsonObject.getString("adress"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("accident2",accident2.toString());

        /**
         * tiếp tục gởi put lên  server để xác định user nào tạo tai nào
         */
        PutRelation putRel = new PutRelation();

        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                String response2 = putRel.put(SystemUtils.getServerBaseUrl()+"accidents/"+accident2.getId_AC()+"/id_user",
                        SystemUtils.getServerBaseUrl()+"users/"+id);
                Log.d("response2",response2);
                Log.d("PutUrl",SystemUtils.getServerBaseUrl()+"users/"+id);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Gửi accident lên server
     */
    public class PostAccident{

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Log.d("postURl",url);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try {
                postResponse = client.newCall(request).execute();
                Log.d("postResponse",postResponse.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return postResponse.body().string();

        }
    }

    /**
     * Gởi put lên server để tạo relation giữa user và accident
     */
    public class PutRelation{

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
    public void sendPatchAccidentKeyToServer(String key){
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");

        String strRequest = "{\n      \"firebaseKey\": \""+key+"\"\n}";
        Log.d("PatchBody",strRequest);
        RequestBody body = RequestBody.create
                (mediaType, strRequest);
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl()+"accidents/"+accident2.getId_AC())
                .patch(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        // TODO: 21-Jun-17 kiểm soát lỗi từ responge
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
