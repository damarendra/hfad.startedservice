package com.example.joke;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class DelayedMessageService extends IntentService {

    public static final String EXTRA_MESSAGE = "message";
    public static final int NOTIFICATION_ID = 682014;

    public DelayedMessageService() {
        super("DelayedMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this) {
            try {
                wait(10000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);
    }

    private void showText(final String text) {
//        Log.v("DelayedMessageService", "The message is: " + text);

        NotificationChannel channel =
                new NotificationChannel(
                        getResources().getString(R.string.app_name),
                        getResources().getString(R.string.notification_channel_name),
                        NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(getResources().getString(R.string.notification_channel_desc));
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat
                        .Builder(this, getResources().getString(R.string.app_name))
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setContentTitle(getString(R.string.question))
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVibrate(new long[] {0, 1000})
                        .setAutoCancel(true)
                        .setContentIntent(PendingIntent.getActivity(
                                this,
                                0,
                                intent,
                                PendingIntent.FLAG_ONE_SHOT
                        ))
                ;

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

}