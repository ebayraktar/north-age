package com.nage.north_age;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.google.firebase.auth.FirebaseAuth;

import me.gilo.woodroid.Woocommerce;
import me.gilo.woodroid.data.ApiVersion;

public class App extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    public static FirebaseAuth mAuth;
    public static Woocommerce woocommerce;

    private static final String SITE_URL = "https://northageofficial.com";
    private static final String CONSUMER_KEY = "ck_dd51adc235b7fb74c599204a61533f53120d5db2";
    private static final String CONSUMER_SECRET = "cs_bd30d24c45e7cfe238e92c94279fa8b4d74a3314";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
        woocommerce = Woocommerce.Builder()
                .setSiteUrl(SITE_URL)
                .setApiVersion(ApiVersion.API_VERSION3)
                .setConsumerKey(CONSUMER_KEY)
                .setConsumerSecret(CONSUMER_SECRET)
                .build();
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is Channel 2");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
