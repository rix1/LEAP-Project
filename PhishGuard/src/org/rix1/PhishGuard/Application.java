package org.rix1.PhishGuard;

import java.util.Random;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */
public class Application implements Comparable<Application>{

    private static int globalCounter = 0;

    private String packageName;
    private String applicationName;
    private int connectionsMade;
    private boolean notificationFlag;
    private long latestPackageStamp;
    private boolean DEBUG_FLAG = false;


    public Application(String packageName, String applicationName){
        this.packageName = packageName;
        this.applicationName = applicationName;
        notificationFlag = false;
        connectionsMade = getRandomCount();
        latestPackageStamp = getTimeStamp();
        globalCounter++;
    }

    public long getTimeStamp(){
        return System.currentTimeMillis() / 1000L;
    }

    public int getRandomCount(){
        if(DEBUG_FLAG){
            return 0;
        }
        return new Random().nextInt(99);
    }

    public void setNotificationFlag(boolean notificationFlag) {
        this.notificationFlag = notificationFlag;
    }

    public boolean shouldWarn(){
        return notificationFlag;
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

    public int compareTo(Application otherApp) {

        int i = this.connectionsMade - otherApp.connectionsMade;
        if(i != 0) return i;

        return this.applicationName.toLowerCase().compareTo(otherApp.applicationName.toLowerCase());

    }
}
