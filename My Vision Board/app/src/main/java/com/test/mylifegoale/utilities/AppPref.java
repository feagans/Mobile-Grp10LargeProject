package com.test.mylifegoale.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPref {
    static final String AFFIRMATION_OF_THE_DAY_DATE = "AFFIRMATION_OF_THE_DAY_DATE";
    static final String AFFIRMATION_OF_THE_DAY_ID = "AFFIRMATION_OF_THE_DAY_ID";
    static final String AUTO_PLAY_INTERVAL = "AUTO_PLAY_INTERVAL";
    static final String BACKGROUND_MUSIC_URL = "BACKGROUND_MUSIC_URL";
    static final String BACKGROUND_MUSIC_VOLUME = "BACKGROUND_MUSIC_VOLUME";
    static final String BACKGROUND_VOICE_URL = "BACKGROUND_VOICE_URL";
    static final String DAILY_REMINDER_TIME = "DAILY_REMINDER_TIME";
    static final String ENABLE_AUTO_PLAY_INTERVAL = "ENABLE_AUTO_PLAY_INTERVAL";
    static final String ENABLE_BACKGROUND_MUSIC = "ENABLE_BACKGROUND_MUSIC";
    static final String ENABLE_BACKGROUND_VOICE = "ENABLE_BACKGROUND_VOICE";
    static final String IS_ADFREE = "IS_ADFREE";
    static final String IS_DAILY_REMINDER = "IS_DAILY_REMINDER";
    static final String IS_DBVERSION_ADDED = "IS_DBVERSION_ADDED";
    static final String IS_FIRST_LAUNCH = "isFirstLaunch";
    static final String IS_RATE_US_SHOWN = "IS_RATE_US_SHOWN";
    static final String LOOP_PLAYING = "LOOP_PLAYING";
    static final String MyPref = "userPref";
    static final String NEVER_SHOW_RATTING_DIALOG = "isNeverShowRatting";
    static final String PLAY_ALL_IN_FOLDER = "PLAY_ALL_IN_FOLDER";
    static final String PLAY_IN_RANDOM_ORDER = "PLAY_IN_RANDOM_ORDER";
    static final String PLAY_VOICE_FILE = "PLAY_VOICE_FILE";
    static final String SCREEN_CONTROLS = "SCREEN_CONTROLS";
    static final String SHOW_DOWNLOAD_BUTTON = "SHOW_DOWNLOAD_BUTTON";
    static final String SLEEP_TIMER = "SLEEP_TIMER";
    static final String VOICE_RECORDING_VOLUME = "VOICE_RECORDING_VOLUME";

    public static void setNeverShowRatting(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(NEVER_SHOW_RATTING_DIALOG, z);
        edit.commit();
    }

    public static boolean isNeverShowRatting(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(NEVER_SHOW_RATTING_DIALOG, false);
    }

    public static boolean isFirstLaunch(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_FIRST_LAUNCH, true);
    }

    public static void setIsFirstLaunch(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_FIRST_LAUNCH, z);
        edit.commit();
    }

    public static void setBackgroundMusicVolume(Context context, int i) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putInt(BACKGROUND_MUSIC_VOLUME, i);
        edit.commit();
    }

    public static int getBackgroundMusicVolume(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getInt(BACKGROUND_MUSIC_VOLUME, 50);
    }

    public static void setVoiceRecordingVolume(Context context, int i) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putInt(VOICE_RECORDING_VOLUME, i);
        edit.commit();
    }

    public static int getVoiceRecordingVolume(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getInt(VOICE_RECORDING_VOLUME, 50);
    }

    public static void setAffirmationOfTheDayId(Context context, String str) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(AFFIRMATION_OF_THE_DAY_ID, str);
        edit.commit();
    }

    public static String getAffirmationOfTheDayId(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(AFFIRMATION_OF_THE_DAY_ID, "");
    }

    public static void setAffirmationOfTheDayDate(Context context, String str) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(AFFIRMATION_OF_THE_DAY_DATE, str);
        edit.commit();
    }

    public static String getAffirmationOfTheDayDate(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(AFFIRMATION_OF_THE_DAY_DATE, "");
    }

    public static void setDailyReminder(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_DAILY_REMINDER, z);
        edit.commit();
    }

    public static boolean isDailyReminder(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_DAILY_REMINDER, true);
    }

    public static void setDailyReminderTime(Context context, long j) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putLong(DAILY_REMINDER_TIME, j);
        edit.commit();
    }

    public static long getDailyReminderTime(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getLong(DAILY_REMINDER_TIME, 631162800000L);
    }

    public static void setEnableBackgroundMusic(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(ENABLE_BACKGROUND_MUSIC, z);
        edit.commit();
    }

    public static boolean isEnableBackgroundMusic(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(ENABLE_BACKGROUND_MUSIC, true);
    }

    public static void setBackgroundMusicUrl(Context context, String str) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(BACKGROUND_MUSIC_URL, str);
        edit.commit();
    }

    public static String getBackgroundMusicUrl(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(BACKGROUND_MUSIC_URL, "song4");
    }

    public static void setEnableAutoPlaying(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(ENABLE_AUTO_PLAY_INTERVAL, z);
        edit.commit();
    }

    public static boolean isEnableAutoPlaying(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(ENABLE_AUTO_PLAY_INTERVAL, true);
    }

    public static void setAutoPlayInterval(Context context, long j) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putLong(AUTO_PLAY_INTERVAL, j);
        edit.commit();
    }

    public static long getAutoPlayInterval(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getLong(AUTO_PLAY_INTERVAL, 5);
    }

    public static void setLoopPlaying(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(LOOP_PLAYING, z);
        edit.commit();
    }

    public static boolean isLoopPlaying(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(LOOP_PLAYING, false);
    }

    public static void setPlayVoiceFile(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(PLAY_VOICE_FILE, z);
        edit.commit();
    }

    public static boolean isPlayVoiceFile(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(PLAY_VOICE_FILE, true);
    }

    public static void setPlayInRandomOrder(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(PLAY_IN_RANDOM_ORDER, z);
        edit.commit();
    }

    public static boolean isPlayInRandomOrder(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(PLAY_IN_RANDOM_ORDER, false);
    }

    public static void setPlayAllInFolder(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(PLAY_ALL_IN_FOLDER, z);
        edit.commit();
    }

    public static boolean isPlayAllInFolder(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(PLAY_ALL_IN_FOLDER, false);
    }

    public static void setSleepTimer(Context context, long j) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putLong(SLEEP_TIMER, j);
        edit.commit();
    }

    public static long getSleepTimer(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getLong(SLEEP_TIMER, 0);
    }

    public static void setScreenControls(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(SCREEN_CONTROLS, z);
        edit.commit();
    }

    public static boolean isScreenControls(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(SCREEN_CONTROLS, true);
    }

    public static void setShowDownloadButton(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(SHOW_DOWNLOAD_BUTTON, z);
        edit.commit();
    }

    public static boolean isShowDownloadButton(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(SHOW_DOWNLOAD_BUTTON, true);
    }

    public static boolean getIsRateUsShown(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_RATE_US_SHOWN, false);
    }

    public static void setIsRateUsShown(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_RATE_US_SHOWN, z);
        edit.commit();
    }

    public static boolean getIsAdfree(Context context) {
        return context.getSharedPreferences(MyPref, 0).getBoolean(IS_ADFREE, false);
    }

    public static void setEnableBackgroundVoice(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(ENABLE_BACKGROUND_VOICE, z);
        edit.commit();
    }

    public static boolean isEnableBackgroundVoice(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(ENABLE_BACKGROUND_VOICE, false);
    }

    public static void setBackgroundVoiceUrl(Context context, String str) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(BACKGROUND_VOICE_URL, str);
        edit.commit();
    }

    public static String getBackgroundVoiceUrl(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(BACKGROUND_VOICE_URL, "");
    }

    public static boolean isDbVersionAdded(Context context) {
        return context.getSharedPreferences(MyPref, 0).getBoolean(IS_DBVERSION_ADDED, false);
    }

    public static void setIsDbversionAdded(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_DBVERSION_ADDED, z);
        edit.commit();
    }
}
