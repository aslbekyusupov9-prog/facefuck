package com.aifacerating.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserProfileManager {
    private static final String PREF_NAME = "user_profile_prefs";
    
    private static final String KEY_NICKNAME = "user_nickname";
    private static final String KEY_AVATAR_URI = "user_avatar_uri";
    
    private static final String KEY_AUTO_CROP = "setting_auto_crop";
    private static final String KEY_MIRROR_MODE = "setting_mirror_mode";
    private static final String KEY_AUTO_SAVE_HISTORY = "setting_auto_save_history";
    private static final String KEY_LANGUAGE = "setting_language";
    private static final String KEY_WEEKLY_REMINDER = "setting_weekly_reminder";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Profile Nickname
    public static String getNickname(Context context) {
        return getPrefs(context).getString(KEY_NICKNAME, "Foydalanuvchi");
    }

    public static void setNickname(Context context, String nickname) {
        getPrefs(context).edit().putString(KEY_NICKNAME, nickname).apply();
    }

    // Profile Avatar
    public static String getAvatarUri(Context context) {
        return getPrefs(context).getString(KEY_AVATAR_URI, null);
    }

    public static void setAvatarUri(Context context, String uri) {
        getPrefs(context).edit().putString(KEY_AVATAR_URI, uri).apply();
    }

    // Settings
    public static boolean isAutoCrop(Context context) {
        return getPrefs(context).getBoolean(KEY_AUTO_CROP, true);
    }

    public static void setAutoCrop(Context context, boolean value) {
        getPrefs(context).edit().putBoolean(KEY_AUTO_CROP, value).apply();
    }

    public static boolean isMirrorMode(Context context) {
        return getPrefs(context).getBoolean(KEY_MIRROR_MODE, true);
    }

    public static void setMirrorMode(Context context, boolean value) {
        getPrefs(context).edit().putBoolean(KEY_MIRROR_MODE, value).apply();
    }

    public static boolean isAutoSaveHistory(Context context) {
        return getPrefs(context).getBoolean(KEY_AUTO_SAVE_HISTORY, true);
    }

    public static void setAutoSaveHistory(Context context, boolean value) {
        getPrefs(context).edit().putBoolean(KEY_AUTO_SAVE_HISTORY, value).apply();
    }

    public static String getLanguage(Context context) {
        return getPrefs(context).getString(KEY_LANGUAGE, "uz");
    }

    public static void setLanguage(Context context, String lang) {
        getPrefs(context).edit().putString(KEY_LANGUAGE, lang).apply();
    }

    public static boolean isWeeklyReminder(Context context) {
        return getPrefs(context).getBoolean(KEY_WEEKLY_REMINDER, true);
    }

    public static void setWeeklyReminder(Context context, boolean value) {
        getPrefs(context).edit().putBoolean(KEY_WEEKLY_REMINDER, value).apply();
    }

    public static void clearHistory(Context context) {
        context.getSharedPreferences("face_rating_history", Context.MODE_PRIVATE).edit().clear().apply();
    }
}
