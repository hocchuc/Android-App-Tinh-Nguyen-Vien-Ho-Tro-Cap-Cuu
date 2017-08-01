package com.emc.emergency.Helper.Services;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.emc.emergency.ChatBox.ChatBoxActivity;
import com.emc.emergency.Main_Menu.MainMenuActivity;
import com.emc.emergency.R;
import com.emc.emergency.Helper.Utils.SystemUtils;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Firebase Messaging Service to handle push notifications
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FCMMessagingService";
    private TokenService tokenService;
    private int notificationId = 001;
    private Double Latitude =null;
    private Double Longtitude = null;
    private String location="";
    private String FirebaseKey="";
    private String id_AC="";
    private String title="";
    private String id_victim;
    Boolean is_not_getdata = true;
    final private String TYPE_HELPER = "helper";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            // In this case the XMPP Server sends a payload data
            String message = remoteMessage.getData().get("message");
            if(message!=null && !message.equals("")) Log.d(TAG,"message : "+message);

            if(remoteMessage.getData().containsKey(SystemUtils.BACKEND_ACTION_ACCIDENT)){
                Latitude = Double.parseDouble(remoteMessage.getData().get("latitude"));
                Longtitude = Double.parseDouble(remoteMessage.getData().get("longtitude"));
                location = remoteMessage.getData().get("address");
                FirebaseKey = remoteMessage.getData().get("FirebaseKey");
                id_AC = remoteMessage.getData().get("id_AC");
                id_victim = remoteMessage.getData().get("id_victim");
                if(id_AC!=null&&!id_AC.equals("")) Log.d("On_Mess_Rec_id_AC",id_AC);
                showAccidentNotification(message);
                is_not_getdata=false;


            }

            Log.d(TAG, "Message received: " + message);

            if(remoteMessage.getData().containsKey(SystemUtils.BACKEND_ACTION_MESSAGE))
            {
                title = remoteMessage.getData().get("title");
                Log.d(TAG,"Message Notification title: "+ remoteMessage.getNotification().getTitle());

                showBasicNotification(message);

                is_not_getdata=false;

            }
            if(remoteMessage.getData().containsKey(SystemUtils.BACKEND_DONE_ACCIDENT)) {
                title = remoteMessage.getData().get("title");
                Log.d(TAG, "Message Notification title: " + remoteMessage.getNotification().getTitle());

                showBasicNotification(message);

                is_not_getdata=false;

            }
            if(remoteMessage.getData().containsKey(SystemUtils.BACKEND_ACTION_LOCK_USER)){
                SharedPreferences preferences2 = getSharedPreferences(SystemUtils.USER, MODE_PRIVATE);
                 SharedPreferences.Editor editor2 = preferences2.edit();
                 editor2.putBoolean("isLogined", false);

                Intent intent = new Intent();
                intent.setAction("com.emc.emergency.onLockUser");
                sendBroadcast(intent);



            }


        }
        
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            if(is_not_getdata)
            showBasicNotification(remoteMessage.getNotification().getBody());
        }

    }

    private void showBasicNotification(String message) {
        Intent i = new Intent();

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,0);
        Uri alarmSound  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setColor(Color.RED)
                .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                      builder.setSmallIcon(R.drawable.ic_announcement_white_24dp);
       } else {
                      builder.setSmallIcon(R.drawable.ic_announcement_red_900_24dp);
        }
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());

    }

    /**
     * Hiện thị noti tai nạn
     * @param message tin nhắn sẽ hiển thị trên tiêu đề
     */
    private void showAccidentNotification(String message) {
        Intent i = new Intent(this, ChatBoxActivity.class);
        i.putExtra("type", TYPE_HELPER);
        Log.d("type",TYPE_HELPER);
        i.putExtra("FirebaseKey", FirebaseKey);
        if(FirebaseKey!=null&&!FirebaseKey.equals(""))Log.d("FirebaseKey",FirebaseKey);
        i.putExtra("id_victim",id_victim);
        i.putExtra("id_AC", id_AC);
        i.setAction("TYPE_HELPER");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,PendingIntent.FLAG_CANCEL_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(alarmSound==null) alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        mapIntent.setData(geoUri);


        mapIntent.setData(geoUri);
        PendingIntent mapPendingIntent =
                PendingIntent.getActivity(this, 0, mapIntent, 0);

        // intent turn by turn
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Latitude + "," + Longtitude + "&avoid=tf");
        Intent navmapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        PendingIntent navmapPendingIntent =
                PendingIntent.getActivity(this, 0, navmapIntent, 0);

        // Create a WearableExtender to add functionality for wearables
        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(false)
                    .setContentTitle("Tai nạn " + location)
                    .setContentText(message)
                    .setSound(alarmSound)
                    .setColor(Color.RED)
                    .setContentIntent(pendingIntent)
                    .addAction(R.drawable.ic_accident_2,"Vào tai nạn", pendingIntent)
                    .addAction(R.drawable.ic_map,
                            getString(R.string.map), navmapPendingIntent)
                    .addAction(R.drawable.ic_accident_location
                            ,"Dẫn đường",mapPendingIntent)
                    .extend(wearableExtender)
                    .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(message));;

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.ic_accident_2);
            } else {
                builder.setSmallIcon(R.mipmap.ic_accident_noti);
            }
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            playBeep();
        
            notificationManager.notify(++notificationId, builder.build());

        }

//    public void showInboxStyleNotification(String message) {
//        Intent i = new Intent(this,ChatBoxActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification.Builder builder = new Notification.Builder(this)
//                .setContentTitle("Inbox Style notification")
//                .setContentText(message)
//                .setSmallIcon(R.drawable.ic_notification)
//                .addAction(R.drawable.ic_notification, "show activity", pendingIntent);
//
//        Notification notification = new Notification.InboxStyle(builder)
//                .addLine(message).addLine("Second message")
//                .addLine("Third message")
//                .setSummaryText("+3 more").build();
//        // Put the auto cancel notification flag
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(0, notification);
//        playBeep();
//    }

//    /**
//     * Parsing the JSON message received from server The intent of message will
//     * be identified by JSON node 'flag'. flag = self, message belongs to the
//     * person. flag = new, a new person joined the conversation. flag = message,
//     * a new message received from server. flag = exit, somebody left the
//     * conversation.
//     * */
//    private void parseMessage(final String msg) {
//
//        try {
//            JSONObject jObj = new JSONObject(msg);
//
//            // JSON node 'flag'
//            String flag = jObj.getString("flag");
//
//            // if flag is 'self', this JSON contains session id
//            if (flag.equalsIgnoreCase(SystemUtils.TAG_SELF)) {
//
//                String sessionId = jObj.getString("sessionId");
//
//                // Save the session id in shared preferences
//                FirebaseUtils.storeSessionId(sessionId);
//
//                Log.e(TAG, "Your session id: " + FirebaseUtils.getSessionId());
//
//            } else if (flag.equalsIgnoreCase(SystemUtils.TAG_NEW)) {
//                // If the flag is 'new', new person joined the room
//                String name = jObj.getString("name");
//                String message = jObj.getString("message");
//
//                // number of people online
//                String onlineCount = jObj.getString("onlineCount");
//
//               /* showToast(name + message + ". Currently " + onlineCount
//                        + " people online!");*/
//
//            } else if (flag.equalsIgnoreCase(SystemUtils.TAG_LOAD_MESSAGE)) {
//                // if the flag is 'message', new message received
//                String fromName = "";//name;
//                String message = jObj.getString("message");
//                String sessionId = jObj.getString("sessionId");
//                boolean isSelf = true;
//
//                // Checking if the message was sent by you
//                if (!sessionId.equals(FirebaseUtils.getSessionId())) {
//                    fromName = jObj.getString("name");
//                    isSelf = false;
//                }
//
//
//                //Chat m = new Chat(fromName, message, isSelf);
//
//                // Appending the message to chat list
//                //  appendMessage(m);
//
//            } else if (flag.equalsIgnoreCase(SystemUtils.TAG_EXIT)) {
//                // If the flag is 'exit', somebody left the conversation
//                String name = jObj.getString("name");
//                String message = jObj.getString("message");
//
//                //showToast(name + message);
//            }
//            // nếu flag là Tag_Old_Message thì load tin nhắn mới
//            else if(flag.equalsIgnoreCase(SystemUtils.TAG_LOAD_MESSAGE)) {
//                // if the flag is 'old_message', check if loaded or not by me
//                String fromName = jObj.getString("name");
//                String message = jObj.getString("message");
//                String sessionId = jObj.getString("sessionId");
//                // Nếu ssID người gửi không bằng với client thì add vao list tin nhắn
//                if(!sessionId.equals(FirebaseUtils.getSessionId())) {
//                    // Chat m = new Chat(fromName, message, false);
//                    //   appendMessage(m);
//                }
//
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//

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
