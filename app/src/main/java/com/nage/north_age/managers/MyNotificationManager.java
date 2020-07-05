package com.nage.north_age.managers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.nage.north_age.R;

import static com.nage.north_age.App.CHANNEL_1_ID;
import static com.nage.north_age.App.CHANNEL_2_ID;

public class MyNotificationManager {
    private static PendingIntent pendingIntent;

    public static void setTapAction(Context context, Class<?> cls) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    }

    public static void sendOnChannel1(Context context, String title, String message) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }

    public void sendOnChannel2(Context context, String title, String message) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManager.notify(2, notification);
    }
}
