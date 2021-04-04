package com.test.mylifegoale.dailyAlarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.test.mylifegoale.utilities.AppPref;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action;
        if (intent != null && (action = intent.getAction()) != null && !action.isEmpty()) {
            char c = 65535;
            int hashCode = action.hashCode();
            if (hashCode != 505380757) {
                if (hashCode != 798292259) {
                    if (hashCode != 1041332296) {
                        if (hashCode == 1737074039 && action.equals("android.intent.action.MY_PACKAGE_REPLACED")) {
                            c = 2;
                        }
                    } else if (action.equals("android.intent.action.DATE_CHANGED")) {
                        c = 3;
                    }
                } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                    c = 0;
                }
            } else if (action.equals("android.intent.action.TIME_SET")) {
                c = 1;
            }
            switch (c) {
                case 0:
                    setAlarms(context);
                    return;
                case 1:
                    setAlarms(context);
                    return;
                case 2:
                    setAlarms(context);
                    return;
                default:
                    return;
            }
        }
    }

    private void setAlarms(Context context) {
        if (AppPref.isDailyReminder(context)) {
            AlarmUtil.remind24(context);
            AlarmUtil.remind3hour(context);
            AlarmUtil.setAlarm(context);
        }
    }
}
