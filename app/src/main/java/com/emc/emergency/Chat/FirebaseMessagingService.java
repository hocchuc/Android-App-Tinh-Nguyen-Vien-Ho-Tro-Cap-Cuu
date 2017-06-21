package com.emc.emergency.Chat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import com.emc.emergency.R;
import com.emc.emergency.utils.FirebaseUtils;
import com.emc.emergency.utils.SystemUtils;
import com.emc.emergency.utils.Utils;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Firebase Messaging Service to handle push notifications
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FCMMessagingService";
    private TokenService tokenService;
    private Utils utils;
    private int notificationId = 001;
    private Double Latitude =null;
    private Double Longtitude = null;
    private String location="";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            // In this case the XMPP Server sends a payload data
             String message = remoteMessage.getData().get("message");
            Latitude = Double.parseDouble(remoteMessage.getData().get("latitude"));
            Longtitude = Double.parseDouble(remoteMessage.getData().get("longtitude"));
            location = remoteMessage.getData().get("address");

            Log.d(TAG, "Message received: " + message);
            if(remoteMessage.getData().containsKey(SystemUtils.BACKEND_ACTION_ACCIDENT))
            {
                showAccidentNotification(message);
                return;
            }

            showBasicNotification(message);

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            showInboxStyleNotification(remoteMessage.getNotification().getBody());

        }

    }

    private void showBasicNotification(String message) {
        Intent i = new Intent(this,ChatBoxActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Basic Notification")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_alert)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());

    }
    private void showAccidentNotification(String message) {
        Intent i = new Intent(this,ChatBoxActivity.class);
        i.putExtra("type","helper");
        i.putExtra("FirebaseKey","Kn96icCFz6zJOh_Xqqm");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        mapIntent.setData(geoUri);

        String uriBegin = "geo:" + Latitude + "," + Longtitude;
        String query = Latitude + "," + Longtitude + "(" + message + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);

        mapIntent.setData(geoUri);
        PendingIntent mapPendingIntent =
                PendingIntent.getActivity(this, 0, mapIntent, 0);
        // Create a WearableExtender to add functionality for wearables
        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(false)
                .setContentTitle("Tai nạn "+location)
                .setContentText(message)
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_map,
                getString(R.string.map), mapPendingIntent)
                .extend(wearableExtender);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.ic_accident_2);
        } else {
            builder.setSmallIcon(R.mipmap.ic_accident_noti);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId,builder.build());

    }

    public void showInboxStyleNotification(String message) {
        Intent i = new Intent(this,ChatBoxActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("Inbox Style notification")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .addAction(R.drawable.ic_notification, "show activity", pendingIntent);

        Notification notification = new Notification.InboxStyle(builder)
                .addLine(message).addLine("Second message")
                .addLine("Third message")
                .setSummaryText("+3 more").build();
        // Put the auto cancel notification flag
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    /**
     * Parsing the JSON message received from server The intent of message will
     * be identified by JSON node 'flag'. flag = self, message belongs to the
     * person. flag = new, a new person joined the conversation. flag = message,
     * a new message received from server. flag = exit, somebody left the
     * conversation.
     * */
    private void parseMessage(final String msg) {

        try {
            JSONObject jObj = new JSONObject(msg);

            // JSON node 'flag'
            String flag = jObj.getString("flag");

            // if flag is 'self', this JSON contains session id
            if (flag.equalsIgnoreCase(SystemUtils.TAG_SELF)) {

                String sessionId = jObj.getString("sessionId");

                // Save the session id in shared preferences
                FirebaseUtils.storeSessionId(sessionId);

                Log.e(TAG, "Your session id: " + FirebaseUtils.getSessionId());

            } else if (flag.equalsIgnoreCase(SystemUtils.TAG_NEW)) {
                // If the flag is 'new', new person joined the room
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                // number of people online
                String onlineCount = jObj.getString("onlineCount");

               /* showToast(name + message + ". Currently " + onlineCount
                        + " people online!");*/

            } else if (flag.equalsIgnoreCase(SystemUtils.TAG_LOAD_MESSAGE)) {
                // if the flag is 'message', new message received
                String fromName = "";//name;
                String message = jObj.getString("message");
                String sessionId = jObj.getString("sessionId");
                boolean isSelf = true;

                // Checking if the message was sent by you
                if (!sessionId.equals(FirebaseUtils.getSessionId())) {
                    fromName = jObj.getString("name");
                    isSelf = false;
                }


                //Chat m = new Chat(fromName, message, isSelf);

                // Appending the message to chat list
                //  appendMessage(m);

            } else if (flag.equalsIgnoreCase(SystemUtils.TAG_EXIT)) {
                // If the flag is 'exit', somebody left the conversation
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                //showToast(name + message);
            }
            // nếu flag là Tag_Old_Message thì load tin nhắn mới
            else if(flag.equalsIgnoreCase(SystemUtils.TAG_LOAD_MESSAGE)) {
                // if the flag is 'old_message', check if loaded or not by me
                String fromName = jObj.getString("name");
                String message = jObj.getString("message");
                String sessionId = jObj.getString("sessionId");
                // Nếu ssID người gửi không bằng với client thì add vao list tin nhắn
                if(!sessionId.equals(FirebaseUtils.getSessionId())) {
                    // Chat m = new Chat(fromName, message, false);
                    //   appendMessage(m);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
   /* *//**
     * Appending message to list view
     * *//*
    private void appendMessage(final Chat m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);

                adapter.notifyDataSetChanged();

                // Playing device's notification
                playBeep();
            }
        });
    }

    private void showToast(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
            }
        });

    }*/

    /**
     * Plays device's default notification sound
     * */
    public void playBeep() {

        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
