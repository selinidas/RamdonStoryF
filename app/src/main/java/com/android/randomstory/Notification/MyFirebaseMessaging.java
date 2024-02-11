package com.android.randomstory.Notification;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.randomstory.Activities.DetailPostActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.paperdb.Paper;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    // For notifications of Firebase
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented = remoteMessage.getData().get("sented");
        String user = remoteMessage.getData().get("user");

        SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        String currentUser = preferences.getString("currentuser", "none");


        Log.d("TAGee", "onMessageReceived: " + user);
        Log.d("TAGee", "onMessageReceived: " + currentUser);
        Log.d("dfdjkhfsdjkhf", currentUser);


        if (FirebaseAuth.getInstance().getUid() != null && sented.equals(FirebaseAuth.getInstance().getUid())) {

            if (!currentUser.equals(user)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreoNotification(remoteMessage);
                } else {
                    sendNotification(remoteMessage);
                }
            }
        }

    }

    @Override
    public void onNewToken(@NonNull String token) {

        super.onNewToken(token);
        Log.d("abc", token);
        SharedPreferences sharedPreference = getSharedPreferences("TokenData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString("token", token);
        editor.commit();

    }


    private void sendOreoNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String ID = remoteMessage.getData().get("ID");

        Log.d("TAGNotify", "sendNotifi2 " + user + ID);

        Intent intent = new Intent(this, DetailPostActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        Paper.book().write("NotificationData", ID);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                defaultSound, icon);

        int i = 0;


        oreoNotification.getManager().notify(i, builder.build());

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String ID = remoteMessage.getData().get("ID");

        Log.d("TAGNotify", "sendNotification: " + user + ID);
        Intent intent = new Intent(this, DetailPostActivity.class);
        Paper.book().write("NotificationData", ID);
        sendMyNotification(title, body, icon, intent, user, ID);
    }

    void sendMyNotification(String title, String body, String icon, Intent intent, String user, String PostID) {
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        Paper.book().write("NotificationData", PostID);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Show notification
        noti.notify(0, builder.build());
    }
}
