package org.rix1.PhishGuard;

import java.util.Random;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */
public class Application {

    private static int globalCounter;

    private String packageName;
    private String applicationName;
    private int connectionsMade;
    private boolean notificationFlag;
    private long latestPackageStamp;


    public Application(String packageName, String applicationName){
        this.packageName = packageName;
        this.applicationName = applicationName;
        notificationFlag = false;
        connectionsMade = getRandomCount();
        latestPackageStamp = getTimeStamp();
    }

    public long getTimeStamp(){
        return System.currentTimeMillis() / 1000L;
    }

    public int getRandomCount(){
        return new Random().nextInt(99);
    }

    public void setNotificationFlag(boolean notificationFlag) {
        this.notificationFlag = notificationFlag;
    }

    public static int getGlobalCounter() {
        return globalCounter;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public int getConnectionsMade() {
        return connectionsMade;
    }

    public long getLatestStamp() {
        return latestPackageStamp;
    }
}
