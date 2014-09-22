package org.rix1.PhishGuard;

import android.app.Application;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import org.rix1.PhishGuard.utils.Utils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Rikard Eide on 18/09/14.
 * Description: Global class to hold variables and flags. Some of these are not currently in use,
 * but there could be a usage for them in the future.
 */

public class GlobalClass extends Application{

    private static Context context;

    private boolean isServiceRunning = false;
    private boolean isListActivityRunning = false;
    private boolean isStartActivityRunning = false;
    private boolean notificationFired = false;

    public static final Type APPLIST_TYPE = new TypeToken<List<org.rix1.PhishGuard.Application>>(){}.getType();
    public static final String APPLIST_NAME = "ApplicationList";
    public static final String PREFS_NAME = "Preferences";
    public static final String FIRST_RUN = "FirstRunBool";
    public static final String MONITOR = "Monitor";

    public static long updateInterval = 1000*10;

    public void onCreate(){
        super.onCreate();
        GlobalClass.context = getApplicationContext();
    }

    public static Context getAppContext(){
        return GlobalClass.context;
    }

    public boolean isNotificationFired() {
        return notificationFired;
    }

    public void setNotificationFired(boolean notificationFired) {
        this.notificationFired = notificationFired;
    }

    public long getUpdateInterval() {
        return 1000*10;
    }

    public void setUpdateInterval(long updateInterval) {
        GlobalClass.updateInterval = updateInterval;
    }

    public boolean isStartActivityRunning() {
        return isStartActivityRunning;
    }

    public void setStartActivityRunning(boolean isStartActivityRunning) {
        this.isStartActivityRunning = isStartActivityRunning;
    }

    public boolean isListActivityRunning() {
        return isListActivityRunning;
    }

    public void setListActivityRunning(boolean isOtherActivityRunning) {
        this.isListActivityRunning = isOtherActivityRunning;
    }

    public boolean isFirstTime() {
        return Utils.getApplicationBool(getApplicationContext(), FIRST_RUN);
    }

    public void setFirstTime() {
        boolean isFirstTime = false;
        Utils.storeBooleanApplicationState(getApplicationContext(), FIRST_RUN, false);
    }

    public boolean isServiceRunning() {
        return isServiceRunning;
    }

    public void setServiceRunning(boolean isServiceRunning) {
        this.isServiceRunning = isServiceRunning;
    }

    public boolean isMonitoring() {
        return Utils.getApplicationBool(getApplicationContext(), MONITOR);
    }

    public void setMonitoring(boolean isMonitoring) {
        boolean isMonitoring1 = isMonitoring;
        Utils.storeBooleanApplicationState(getApplicationContext(), MONITOR, isMonitoring);

    }
}
