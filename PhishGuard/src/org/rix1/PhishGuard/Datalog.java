package org.rix1.PhishGuard;

/**
 *
 * Created by Rikard Eide on 15/09/14.
 * Description:
 */

public class Datalog {


    private long packetsSinceBoot;
    private long byteSinceBoot;
    private long timeStamp;

    public Datalog(long packet, long bytes, long timeStamp){
        this.packetsSinceBoot = packet;
        this.byteSinceBoot = bytes;
        this.timeStamp = timeStamp;
    }


    public long getPacketsSinceBoot() {
        return packetsSinceBoot;
    }

    public long getByteSinceBoot() {
        return byteSinceBoot;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}