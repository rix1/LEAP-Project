package org.rix1.PhishGuard;


import android.util.Log;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Stack;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */

public class Application implements Comparable<Application>{

    private final int uid;
    private final String packageName;
    private final String applicationName;
    private final String iconURI;
    private long startTXPackets;
    private long startTXBytes;
    private long packetsSent;
    private long latestTimeStamp;
    private boolean isTracked;
    private boolean isUpdated;

    private final Stack<Datalog> datalog;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    public long getStartTXBytes() {
        return startTXBytes;
    }

    public Application(int uid, String packageName, String applicationName, long startPackets, long startBytes, int iconResID){
        this.uid = uid;
        this.packageName = packageName;
        this.applicationName = applicationName;
        this.startTXPackets = startPackets;
        this.startTXBytes = startBytes;
        this.iconURI = "android.resource://" + packageName + "/" + iconResID;
        packetsSent = 0;
        isTracked = false;
        datalog = new Stack<Datalog>();
        isUpdated = false;
        updateLatestPackageStamp();
    }

    public void updateLatestPackageStamp(){
        this.latestTimeStamp = System.currentTimeMillis();
    }


    public void update(long packet, long bytes, long timeStamp){
        this.startTXBytes = bytes;
        this.startTXPackets = packet;
        this.latestTimeStamp = timeStamp;
    }

    /**
     * This method logs data for tracked applications to a stack.
     * @param packet The packets updated number of packets that have been sent since boot
     * @param bytes The packets updated number of packets that have been sent since boot
     * @param timeStamp The timestamp the change was recorded.
     */

    public void logData(long packet, long bytes, long timeStamp) {
        Log.d("APP_NETWORK_APP", packet + " packet " + bytes + " bytes");

        if (!datalog.empty()) {
            Datalog prevRecord = datalog.peek();
            long dx = bytes - prevRecord.getByteSinceBoot();
            if(!isUpdated) {
                Log.d("APP_NETWORK", "Lets update and fire. datalog is not empty");
                pcs.firePropertyChange(this.applicationName, prevRecord.getByteSinceBoot(), bytes);
                isUpdated = true;
            }
            datalog.push(new Datalog(packet, bytes, timeStamp, dx));
        } else{
            Log.d("APP_NETWORK", "Lets update and fire. datalog is empty");
            if(!isUpdated) {
                Log.d("APP_NETWORK", "Lets update and fire. datalog is empty");
                pcs.firePropertyChange(applicationName, startTXBytes, bytes);
                isUpdated = true;
            }
            datalog.push(new Datalog(packet, bytes, timeStamp, bytes));
        }
    }

    public void reset(){
        datalog.clear();
        startTXBytes = 0;
        startTXPackets = 0;
        latestTimeStamp = 0;
        isUpdated = false;

    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(Boolean updateValue) {
        this.isUpdated = updateValue;
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

    public String getPackageName() {
        return packageName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public long getLatestStamp() {
        return latestTimeStamp;
    }

    public String getIconUri(){
        return iconURI;
    }

    public Stack<Datalog> getDatalog() {
        return datalog;
    }

    public int compareTo(Application otherApp) {

        int i = Boolean.valueOf(otherApp.isTracked()).compareTo(this.isTracked());
        if(i != 0) return i;
        i = (int)(this.startTXBytes - otherApp.startTXBytes);
        if(i != 0) return i;

        return this.applicationName.toLowerCase().compareTo(otherApp.applicationName.toLowerCase());
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs = new PropertyChangeSupport(this);
        pcs.addPropertyChangeListener(listener);
    }

        @Override
    public String toString() {
        return "Application{" +
                "uid=" + uid +
                ", packageName='" + packageName + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", startTXPackets=" + startTXPackets +
                ", startTXBytes=" + startTXBytes +
                ", packetsSent=" + packetsSent +
                ", latestTimeStamp=" + latestTimeStamp +
                ", iconURI=" + iconURI +
                ", isTracked=" + isTracked +
                '}';
    }
}
