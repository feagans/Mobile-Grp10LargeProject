package com.test.mylifegoale.dailyAlarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.test.mylifegoale.utilities.AppPref;
import com.test.mylifegoale.utilities.Constants;

import java.util.Calendar;

public class AlarmUtil {
    public static void setAlarm(Context context) {
        @SuppressLint("WrongConstant") AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(Constants.REQUEST_CODE_ALARM_NAME, Constants.REQUEST_CODE_SET_ALARM);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, Constants.REQUEST_CODE_SET_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(AppPref.getDailyReminderTime(context));
        Calendar instance2 = Calendar.getInstance();
        instance2.set(11, instance.get(11));
        instance2.set(12, instance.get(12));
        instance2.set(13, 0);
        instance2.set(14, 0);
        if (System.currentTimeMillis() > instance2.getTimeInMillis()) {
            return;
        }
        if (Build.VERSION.SDK_INT < 19) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, instance2.getTimeInMillis(), broadcast);
        } else if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 23) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, instance2.getTimeInMillis(), broadcast);
        } else if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, instance2.getTimeInMillis(), broadcast);
        }
    }

    public static void remind3hour(Context context) {
        @SuppressLint("WrongConstant") AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(Constants.REQUEST_CODE_ALARM_NAME, Constants.REQUEST_CODE_REMIND_3);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, Constants.REQUEST_CODE_REMIND_3, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar instance = Calendar.getInstance();
        instance.set(11, 6);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        if (System.currentTimeMillis() > instance.getTimeInMillis()) {
            return;
        }
        if (Build.VERSION.SDK_INT < 19) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), broadcast);
        } else if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 23) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), broadcast);
        } else if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), broadcast);
        }
    }

    public static void remind24(Context context) {
        @SuppressLint("WrongConstant") AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(Constants.REQUEST_CODE_ALARM_NAME, Constants.REQUEST_CODE_REMIND_24);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, Constants.REQUEST_CODE_REMIND_24, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar instance = Calendar.getInstance();
        instance.set(11, 24);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        if (Build.VERSION.SDK_INT < 19) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), broadcast);
        } else if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 23) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), broadcast);
        } else if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), broadcast);
        }
    }

    public static void setAllAlarm(Context context) {
        remind3hour(context);
        remind24(context);
        setAlarm(context);
    }

}
