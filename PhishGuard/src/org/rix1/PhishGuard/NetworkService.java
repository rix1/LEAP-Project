package org.rix1.PhishGuard;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.Log;

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


    public NetworkService(){
        outApplications = new ArrayList<Application>();
    }

    public ArrayList<Application> getTraffic(List<ApplicationInfo> appInfo, PackageManager packageManager){

        if(firstTimeFlag){
            // TODO: Fetch from database
            Log.d("APP_NETWORK" , "Gathering info for first time... Number of apps: " + appInfo.size());
            firstTimeFlag = false;
        }else {
            Log.d("APP_NETWORK" , "Info have already been gathered. Size of array: " + outApplications.size());
        }

        int uid;
        this.packageManager = packageManager;

        if(appInfo != null) {
            for (ApplicationInfo appI : appInfo) {
                uid = appI.uid;
                startTXpackets = TrafficStats.getUidTxPackets(uid);
                startTXbytes = TrafficStats.getUidRxBytes(uid);

                if(startTXpackets != 0){
                    Application app = contains(appI.packageName);
                    if(app == null){
                        addApplication(appI);
                    } else updateApplication(app);
                }
            }
        }else Log.d("APP_NETWORK", "Something went wrong...");

        return outApplications;
    }
    private void updateApplication(Application app){
        // Because Application is mutable we can just change the app object itself.
        app.updateLatestPackageStamp();
        app.setPacketsSent(startTXpackets);
    }

    private void addApplication(ApplicationInfo appI){
        outApplications.add(new Application(appI.packageName, appI.loadLabel(packageManager).toString(), startTXpackets, startTXbytes, appI.loadIcon(packageManager)));
    }

    private Application contains(String packageName){
        for (Application app: outApplications){
            if(app.getPackageName().equals(packageName)) {
                return app;
            }
        }
        return null;
    }
}
