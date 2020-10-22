package com.example.wattodo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {


    NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onReceive(Context context, Intent intent) {

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent contentIntent = new Intent(context,MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context,NOTIFICATION_ID,contentIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.task_icon);
        builder.setContentTitle("Task Pending ");
        builder.setContentText(intent.getStringExtra("title"))
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
