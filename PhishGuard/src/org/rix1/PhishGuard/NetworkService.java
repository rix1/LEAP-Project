package org.rix1.PhishGuard;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.Log;
import org.rix1.PhishGuard.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing network related stuff.
 * Will most likely be modified in the future to extend an Android service.
 * Created by rikardeide on 12/09/14.
 */

public class NetworkService {

    private long startTXpackets = 0; // TX is transmitted
    private long startTXbytes = 0;
    private ArrayList<Application> outApplications;
    private PackageManager packageManager;
    private boolean firstTimeFlag = true;
    private PackageManager pm;


    public NetworkService(PackageManager pm){
        this.pm = pm;
    }

    public ArrayList<Application> init(List<ApplicationInfo> appInfo){

        ArrayList<Application> returnList = new ArrayList<Application>();
        // Should return a list of all applicaitons
        // having outoing communications

        int uid;
        if(appInfo != null){
            for (ApplicationInfo appI : appInfo){
                uid = appI.uid;
                startTXpackets = TrafficStats.getUidTxPackets(uid);
                startTXbytes = TrafficStats.getUidTxBytes(uid);

                if(startTXbytes > 0){
                    returnList.add(new Application(uid, appI.packageName, appI.loadLabel(packageManager).toString(), startTXpackets, startTXbytes, appI.loadIcon(packageManager)));
                }
            }
        }
        return returnList; // Should now contain all applications having sent more than one packet  since boot.
    }

    public ArrayList<Application> update(ArrayList<Application> applications){
        // Should return an updated list containting updated information.
        int uid;

        for (Application app : applications){
            uid = app.getUid();
            startTXpackets = TrafficStats.getUidTxPackets(uid);
            startTXbytes = TrafficStats.getUidTxBytes(uid);

            if(startTXbytes > app.getStartTXBytes()){ // This means the traffic has increased
                app.update(startTXpackets, startTXbytes, System.currentTimeMillis());
                if(app.isTracked()){
                    app.logData(startTXpackets, startTXbytes, System.currentTimeMillis());
                }
            }
        }
        return applications;
    }
}
