package org.rix1.PhishGuard;

import android.app.Application;

/**
 * Created by Rikard Eide on 18/09/14.
 * Description: Global class to hold variables and flags
 */

public class GlobalClass extends Application{

    private boolean isServiceRunning = false;

    public boolean isServiceRunning() {
        return isServiceRunning;
    }

    public void setServiceRunning(boolean isServiceRunning) {
        this.isServiceRunning = isServiceRunning;
    }

}
