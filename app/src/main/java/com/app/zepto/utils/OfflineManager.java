package com.app.zepto.utils;

import android.content.Context;

public class OfflineManager {
    private SharedPrefManager sharedPrefManager;

    public OfflineManager(Context context) {
        sharedPrefManager = new SharedPrefManager(context);
    }

    public void saveLastSyncTime(long time) {
        sharedPrefManager.saveLastSyncTime(time);
    }

    public long getLastSyncTime() {
        return sharedPrefManager.getLastSyncTime();
    }

    public void setOfflineMode(boolean enabled) {
        sharedPrefManager.setOfflineMode(enabled);
    }

    public boolean isOfflineMode() {
        return sharedPrefManager.isOfflineMode();
    }
}
