package org.rix1.PhishGuard;

import android.graphics.drawable.Drawable;

import java.util.Stack;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */

public class Application implements Comparable<Application>{

    private static int globalCounter = 0;

    private int uid;
    private String packageName;
    private String applicationName;

    private long startTXPackets;
    private long startTXBytes;

    private long packetsSent;
    private long latestTimeStamp;
    private Drawable icon;
    private boolean isTracked;
    private Stack<Datalog> datalog;

    private boolean DEBUG_FLAG = false;


    public long getStartTXPackets() {
        return startTXPackets;
    }

    public long getStartTXBytes() {
        return startTXBytes;
    }

    public Application(int uid, String packageName, String applicationName, long startPackets, long startBytes, Drawable icon){
        this.uid = uid;
        this.packageName = packageName;
        this.applicationName = applicationName;

        packetsSent = 0;
        this.startTXPackets = startPackets;
        this.startTXBytes = startBytes;


        this.icon = icon;
        isTracked = false;

        datalog = new Stack<Datalog>();
        globalCounter++;

        updateLatestPackageStamp();

//        logData(startPackets, startBytes, latestTimeStamp);
    }

    public void updateLatestPackageStamp(){
        this.latestTimeStamp = System.currentTimeMillis();
    }

    public void setLatestTimeStamp(long timestamp){
        this.latestTimeStamp = timestamp;
    }

    public void update(long packet, long bytes, long timeStamp){
        this.startTXBytes = bytes;
        this.startTXPackets = packet;
        this.latestTimeStamp = timeStamp;
    }



    /**
     * Check if we should update this dataobject
     * @param packet
     * @param bytes
     * @param timeStamp
     * @return True if new data has been recorded. This will be used to send notifications.
     */
    public boolean logData(long packet, long bytes, long timeStamp) {
        if (!datalog.empty()) {
            Datalog prevLog = datalog.peek();
            if (prevLog.getByteSinceBoot() < bytes) { // Indicates that packets have been sent
                datalog.push(new Datalog(packet, bytes, timeStamp));
                return true;
            } else return false;
        } else datalog.add(new Datalog(packet, bytes, timeStamp));
        return true;
    }

    public void setTracked(boolean tracked) {
        this.isTracked = tracked;
    }

    public boolean isTracked(){
        return isTracked;
    }

    public int getUid() {
        return uid;
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
        return latestTimeStamp;
    }

    /**
     * @deprecated
     * @param packetsSent
     */

    public void setPacketsSent(long packetsSent){
        this.packetsSent = packetsSent;
    }

    /**
     * @deprecated
     * @return
     */
    public long getPacketsSent(){
        return packetsSent;
    }

    public Drawable getIcon(){
        return icon;
    }

    public int compareTo(Application otherApp) {

        int i = Boolean.valueOf(this.isTracked()).compareTo(otherApp.isTracked());
        if(i != 0) return i;
        i = (int)(this.startTXBytes - otherApp.startTXBytes);
        if(i != 0) return i;

        return this.applicationName.toLowerCase().compareTo(otherApp.applicationName.toLowerCase());
    }


}
