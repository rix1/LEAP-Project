package org.rix1.PhishGuard;

/**
 *
 * Created by Rikard Eide on 15/09/14.
 * Description:
 */

public class Datalog {


    private final long packetsSinceBoot;
    private final long byteSinceBoot;
    private final long timeStamp;
    private long dx;

    public Datalog(long packet, long bytes, long timeStamp){
        this.packetsSinceBoot = packet;
        this.byteSinceBoot = bytes;
        this.timeStamp = timeStamp;
    }

    public long getDx(){return dx;}
    public long getPacketsSinceBoot() {
        return packetsSinceBoot;
    }

    public long getByteSinceBoot() {
        return byteSinceBoot;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "Datalog{" +
                "packetsSinceBoot=" + packetsSinceBoot +
                ", byteSinceBoot=" + byteSinceBoot +
                ", timeStamp=" + timeStamp +
                ", dx=" + dx +
                '}';
    }
}
