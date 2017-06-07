package com.emc.emergency.Chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.emc.emergency.R;
import com.emc.emergency.model.Chat;
import com.emc.emergency.utils.FirebaseUtils;
import com.emc.emergency.utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;


import java.util.List;
import java.util.Random;

public class ChatBoxActivity extends AppCompatActivity implements IRequestListener {

    private static final String TAG = "ChatBoxActivity";
    public static final String FCM_PROJECT_SENDER_ID = "728085231482";
    public static final String FCM_SERVER_CONNECTION = "@gcm.googleapis.com";
    public static final String BACKEND_ACTION_MESSAGE = "MESSAGE";
    public static final String BACKEND_ACTION_ECHO = "com.emc.emergency.ECHO";
    public static final Random RANDOM = new Random();
   //private SystemUtils systemUtils;
    private Button btnSend;
    private EditText inputMsg;
    // Chat messages list adapter
 //   private MessagesListAdapter adapter;
    private List<Chat> listMessages;
    private ListView listViewMessages;
    private EditText editTextEcho;
    private TextView deviceText;
    private Button buttonUpstreamEcho;
    private TokenService tokenService;
    private Utils utils;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        Log.d(TAG, "FCM Token creation logic");

        initControl();

        //// TODO: 31-May-17 remove this logic and replace with chat box logic
        // Get variables reference
        deviceText = (TextView) findViewById(R.id.deviceText);
        editTextEcho = (EditText) findViewById(R.id.editTextEcho);
        buttonUpstreamEcho = (Button) findViewById(R.id.buttonUpstreamEcho);

        //Get token from Firebase
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token: " + token);
        deviceText.setText(token);

        //Call the token service to save the token in the database
        tokenService = new TokenService(this, this);
        tokenService.registerTokenInDB(token);

        buttonUpstreamEcho.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Echo Upstream message logic");

                String message = editTextEcho.getText().toString();

                Log.d(TAG, "Message: " + message + ", recipient: " + token);
                FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(FCM_PROJECT_SENDER_ID + FCM_SERVER_CONNECTION)
                        .setMessageId(Integer.toString(RANDOM.nextInt()))
                        .addData("message", message)
                        .addData("action", BACKEND_ACTION_ECHO)
                        .build());
                // To send a message to other device through the XMPP Server, you should add the
                // receiverId and change the action name to BACKEND_ACTION_MESSAGE in the data
            }
        });

    }

    private void initControl() {
        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Sending message to web socket server
                sendMessageToServer(FirebaseUtils.getSendMessageJSON(inputMsg.getText()
                        .toString()));
               /* {
                    String[] method = inputMsg.getText().toString().split(" ", 3);
                    try {
                        if(method[0].toString().equals("/w"))
                          //  Client_to_send_secrect = method[1];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
                // Clearing the input filed once message was sent
                inputMsg.setText("");
            }
        });
    }
    /**
     * Method to send message to web socket server
     * */
    private void sendMessageToServer(String message) {
     /*   if (client != null && client.isConnected()) {
            client.send(message);
        }*/
        Log.d(TAG, "Echo Upstream message logic");

        String message2 = inputMsg.getText().toString();

        Log.d(TAG, "Message: " + message + ", recipient: " + token);
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(FCM_PROJECT_SENDER_ID + FCM_SERVER_CONNECTION)
                .setMessageId(Integer.toString(RANDOM.nextInt()))
                .addData("message", message2)
                .addData("action", BACKEND_ACTION_ECHO)
                .build());
    }
    @Override
    public void onComplete() {
        Log.d(TAG, "Token registered successfully in the DB");

    }

    @Override
    public void onError(String message) {
        Log.d(TAG, "Error trying to register the token in the DB: " + message);
    }
}
