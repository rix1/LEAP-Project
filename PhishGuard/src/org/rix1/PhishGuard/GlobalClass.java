package org.rix1.PhishGuard;

import android.app.Application;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Rikard Eide on 18/09/14.
 * Description: Global class to hold variables and flags
 */

public class GlobalClass extends Application{

    private boolean isServiceRunning = false;
    private boolean isFirstTime = true;

    public final Type APPLIST_TYPE = new TypeToken<List<org.rix1.PhishGuard.Application>>(){}.getType();
    public final String APPLIST_NAME = "ApplicationList";
    public final String PREFS_NAME = "Preferences";
    public final String FIRST_RUN = "FirstRunBool";



    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    public boolean isServiceRunning() {
        return isServiceRunning;
    }

    public void setServiceRunning(boolean isServiceRunning) {
        this.isServiceRunning = isServiceRunning;
    }

}
