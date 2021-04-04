package com.test.mylifegoale.dailyAlarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.test.mylifegoale.R;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.utilities.AppPref;
import com.test.mylifegoale.utilities.Constants;
import com.test.mylifegoale.view.AffirmPlayerActivity;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_NAME = "MyLifeGoal";
    Notification.Builder nBuilder;
    String notificationMsg = "";
    int requestCode = 0;

    public void onReceive(Context context, Intent intent) {
        this.requestCode = intent.getIntExtra(Constants.REQUEST_CODE_ALARM_NAME, 0);
        if (AppPref.isDailyReminder(context)) {
            int i = this.requestCode;
            if (i == 1111) {
                if (AppPref.isDailyReminder(context)) {
                    setNotification(context);
                }
            } else if (i == 1103) {
                AlarmUtil.remind24(context);
            } else if (i == 1124) {
                AlarmUtil.remind3hour(context);
                AlarmUtil.setAlarm(context);
            }
        }
    }

    private void setNotification(Context context) {
        String str = "0";
        try {

            this.notificationMsg = Constants.DEFAULT_AFFIRMATION_TEXT;

            str = AppDatabase.getAppDatabase(context).affirmationDao().getRandomId();
            String quoteText = AppDatabase.getAppDatabase(context).affirmationDao().getDetail(str).getQuoteText();
            if (quoteText == null || quoteText.isEmpty()) {
                if (AppDatabase.getAppDatabase(context).affirmationDao().getAllCount() > 0) {
                    str = AppDatabase.getAppDatabase(context).affirmationDao().getRandomId();
                    this.notificationMsg = quoteText;
                } else {
                    this.notificationMsg = Constants.DEFAULT_AFFIRMATION_TEXT;
                }
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(context, AffirmPlayerActivity.class);
                intent.putExtra(Constants.REQUEST_CODE_ALARM_NAME_ID, Constants.REQUEST_CODE_ALARM_ID);
                intent.putExtra(AffirmPlayerActivity.EXTRA_IS_NOTIFICATION, true);
                intent.putExtra(AffirmPlayerActivity.EXTRA_ID, str);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent activity = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (Build.VERSION.SDK_INT >= 26) {
                    NotificationChannel notificationChannel = new NotificationChannel("com.test.mylifegoale", NOTIFICATION_CHANNEL_NAME, 2);
                    notificationChannel.setLightColor(-16776961);
                    notificationChannel.setLockscreenVisibility(1);
                    notificationManager.createNotificationChannel(notificationChannel);
                    this.nBuilder = new Notification.Builder(context, "com.test.mylifegoale").setSmallIcon(R.drawable.notification).setContentTitle(context.getString(R.string.app_name)).setContentText(this.notificationMsg).setStyle(new Notification.BigTextStyle().bigText(this.notificationMsg)).setPriority(-1).setDefaults(2).setAutoCancel(true).setContentIntent(activity).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
                } else {
                    this.nBuilder = new Notification.Builder(context).setSmallIcon(R.drawable.notification).setContentTitle(context.getString(R.string.app_name)).setContentText(this.notificationMsg).setStyle(new Notification.BigTextStyle().bigText(this.notificationMsg)).setPriority(-1).setDefaults(2).setAutoCancel(true).setContentIntent(activity).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
                }
                notificationManager.notify(Constants.REQUEST_CODE_ALARM_ID, this.nBuilder.build());
            }

        } catch (Exception e) {
            try {
                this.notificationMsg = Constants.DEFAULT_AFFIRMATION_TEXT;
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, AffirmPlayerActivity.class);
        intent.putExtra(Constants.REQUEST_CODE_ALARM_NAME_ID, Constants.REQUEST_CODE_ALARM_ID);
        intent.putExtra(AffirmPlayerActivity.EXTRA_IS_NOTIFICATION, true);
        intent.putExtra(AffirmPlayerActivity.EXTRA_ID, str);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel("my_notification", "n_channel", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("description");
            notificationChannel.setName("Channel Name");
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(notificationMsg)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setOnlyAlertOnce(true)
                .setChannelId("my_notification")
                .setColor(Color.parseColor("#3F5996"));

        assert notificationManager != null;
        notificationManager.notify(Constants.REQUEST_CODE_ALARM_ID, notificationBuilder.build());
    }
}
