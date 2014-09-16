package org.rix1.PhishGuard;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */
public class Application implements Comparable<Application>{

    private static int globalCounter = 0;

    private String packageName;
    private String applicationName;
    private final long startPackets;

    private long packetsSent;
    private long latestPackageStamp;

    private boolean notificationFlag;
    private boolean DEBUG_FLAG = false;
    private Drawable icon;

    private Stack<Datalog> datalog;


    public Application(String packageName, String applicationName, long startPackets, long startBytes, Drawable icon){
        this.packageName = packageName;
        this.applicationName = applicationName;

        packetsSent = 0;
        this.startPackets = startPackets;

        this.icon = icon;
        notificationFlag = false;

        datalog = new Stack<Datalog>();
        globalCounter++;

        updateLatestPackageStamp();
        logData(startPackets, startBytes, latestPackageStamp);
    }


    /**
     * Check if we should update this dataobject
     * @param packet
     * @param bytes
     * @param timeStamp
     * @return True if new data has been recorded. This will be used to send notifications.
     */
    public boolean logData(long packet, long bytes, long timeStamp){
        if(!datalog.empty()){
            Datalog prevLog = datalog.peek();
            if(prevLog.getByteSinceBoot() < bytes){ // Indicates that packets have been sent
                datalog.push(new Datalog(packet, bytes, timeStamp));
                return true;
            } else return false;
        }else datalog.add(new Datalog(packet, bytes, timeStamp));
        return true;
    }

    /*
    public long getTimeStamp(){
        return System.currentTimeMillis() / 1000L;
    }

    public int getRandomCount(){
        if(DEBUG_FLAG){
            return 0;
        }
        return new Random().nextInt(99);
    }
    */

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

    public long getLatestStamp() {
        return latestPackageStamp;
    }

    public void updateLatestPackageStamp(){
        this.latestPackageStamp = System.currentTimeMillis() / 1000L;
    }

    public void setPacketsSent(long packetsSent){
        this.packetsSent = packetsSent;
    }
    public long getPacketsSent(){
        return packetsSent;
    }

    public Drawable getIcon(){
        return icon;
    }

    public int compareTo(Application otherApp) {

        int i = (int)(this.packetsSent - otherApp.packetsSent);
        if(i != 0) return i;

        return this.applicationName.toLowerCase().compareTo(otherApp.applicationName.toLowerCase());
    }

    @Override
    public String toString() {
        return "Application{" +
                "packageName='" + packageName + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", packetsSent=" + packetsSent +
                ", latestPackageStamp=" + latestPackageStamp +
                ", notificationFlag=" + notificationFlag +
                ", DEBUG_FLAG=" + DEBUG_FLAG +
                '}';
    }
}
