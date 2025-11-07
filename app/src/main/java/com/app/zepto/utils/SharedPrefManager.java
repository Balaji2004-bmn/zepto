package com.app.zepto.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String PREF_NAME = "ZeptoApp";
    private SharedPreferences sharedPreferences;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLastSyncTime(long time) {
        sharedPreferences.edit().putLong("last_sync_time", time).apply();
    }

    public long getLastSyncTime() {
        return sharedPreferences.getLong("last_sync_time", 0);
    }

    public void setOfflineMode(boolean enabled) {
        sharedPreferences.edit().putBoolean("offline_mode", enabled).apply();
    }

    public boolean isOfflineMode() {
        return sharedPreferences.getBoolean("offline_mode", false);
    }
}
