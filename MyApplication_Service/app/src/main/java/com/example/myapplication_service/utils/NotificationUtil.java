package com.example.myapplication_service.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;

import com.example.myapplication_service.BroadcastReceiver.NotificationReminderBroadcast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;

public class NotificationUtil {


    public static void setNotification(Context context, String title, String content) {
        Global.getInstance().setContent(content);
        Global.getInstance().setTitle(title);


        Intent intent = new Intent(context, NotificationReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
    }


    public static void createChannel(Context context, String channel_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NAME - CodeSpeedy";
            String description = "DESCRIPTION - Channel for Reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}