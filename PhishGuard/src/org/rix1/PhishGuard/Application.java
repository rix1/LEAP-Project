package org.rix1.PhishGuard;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Stack;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */

public class Application implements Comparable<Application>{

    private static int globalCounter = 0;

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


    public long getStartTXPackets() {
        return startTXPackets;
    }

    public long getStartTXBytes() {
        return startTXBytes;
    }

    public Application(int uid, String packageName, String applicationName, long startPackets, long startBytes, int iconResID){
        this.uid = uid;
        this.packageName = packageName;
        this.applicationName = applicationName;

        packetsSent = 0;
        this.startTXPackets = startPackets;
        this.startTXBytes = startBytes;

        this.iconURI = "android.resource://" + packageName + "/" + iconResID;
        isTracked = false;

        datalog = new Stack<Datalog>();
        globalCounter++;
        isUpdated = false;

        updateLatestPackageStamp();

        logData(startPackets, startBytes, latestTimeStamp);
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
     * @param
     * @param
     * @param
     * @return True if new data has been recorded. This will be used to send notifications.
     */

    public void logData(long packet, long bytes, long timeStamp) {
        if(datalog.size() > 0){
            long dx = bytes - datalog.peek().getPacketsSinceBoot();
        }
        isUpdated = true;
        if (!datalog.empty()) {
            Datalog prevRecord = datalog.peek();
            pcs.firePropertyChange(this.applicationName, prevRecord.getByteSinceBoot(), bytes);
            datalog.push(new Datalog(packet, bytes, timeStamp));
        } else {
            pcs.firePropertyChange(applicationName, startTXBytes, bytes);
            datalog.add(new Datalog(packet, bytes, timeStamp));
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
