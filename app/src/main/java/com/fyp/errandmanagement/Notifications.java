package com.fyp.errandmanagement;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.fyp.errandmanagement.CustomerData.CustomerDashboard;
import com.fyp.errandmanagement.ProviderData.ProviderDashboard;
import com.fyp.errandmanagement.R;

public class Notifications {

    private Context context;

    public Notifications(Context context) {
        this.context = context;
    }

    public static final String CHANNEL_ID = "scNotifications";
    public static final String CHANNEL_NAME = "SC Notifications";
    public static final String CHANNEL_DESC = "Sending and Receiving Notifications";

    public static void displayNotifications(Context context, String title, String text){

        SharedPreferences shared = context.getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);

        if(shared.getString("STATUS","").equals("ServiceProviders")) {
            Intent intent = new Intent(context, ProviderDashboard.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    100,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.booking_icon)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
            managerCompat.notify(1, builder.build());
        }
        else{
            Intent intent = new Intent(context, CustomerDashboard.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    100,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.booking_icon)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
            managerCompat.notify(1, builder.build());
        }

    }

    public static void createNotificationChannel(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}

