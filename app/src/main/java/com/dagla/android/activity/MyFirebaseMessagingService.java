package com.dagla.android.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.dagla.android.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by sys on 12/12/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //private static final String TAG = "FCM Service";
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // TODO: Handle FCM messages here.
//        // If the application is in the foreground handle both data and notification messages here.
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated.
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Title: " + remoteMessage.getNotification().getTitle());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DeviceTokenId",s);
        editor.apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Notification Message Title: " + remoteMessage.getNotification().getTitle());
            //Calling method to generate notification
            sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }



        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.app_icon_april_2021)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());

    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_icon_april_2021)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }


}
