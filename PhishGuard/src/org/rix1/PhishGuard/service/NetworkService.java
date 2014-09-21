package org.rix1.PhishGuard.service;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.Log;
import org.rix1.PhishGuard.Application;
import org.rix1.PhishGuard.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing network related stuff.
 * Will most likely be modified in the future to extend an Android service.
 * Created by rikardeide on 12/09/14.
 */


// TODO: Rename to TrafficFetcher
public class NetworkService {

    private long startTXpackets = 0; // TX is transmitted
    private long startTXbytes = 0;
    private final PackageManager pm;

    public NetworkService(PackageManager pm){
        this.pm = pm;
    }


    /**
     * Should sort out and return a list of all applications having outgoing communications
     * @param appInfo A list of all installed applications.
     * @return A list of all applications having outgoing communications
     */

    public ArrayList<Application> init(List<ApplicationInfo> appInfo){
        ArrayList<Application> returnList = new ArrayList<Application>();

        int uid;
        if(appInfo != null){
            for (ApplicationInfo appI : appInfo){
                uid = appI.uid;
                startTXpackets = TrafficStats.getUidTxPackets(uid);
                startTXbytes = TrafficStats.getUidTxBytes(uid);

                if(startTXbytes > 0){
                    returnList.add(new Application(uid, appI.packageName, appI.loadLabel(pm).toString(), startTXpackets, startTXbytes, appI.icon));
                }
            }
        }
        return returnList; // Should now contain all applications having sent more than one packet  since boot.
    }


    /**
     * Should return an updated list containing updated information.
     */

    public ArrayList<Application> update(ArrayList<Application> applications, List<ApplicationInfo> appI){

        ArrayList<Application> updatedList = init(appI);

        Log.d("APP_NETWORK", "All apps: " + appI.size() + " - network apps: " + updatedList.size() + " - currentList: " + applications.size());

        int uid;

        for (Application app : applications){
            app.addPropertyChangeListener(new TXservice());

            uid = app.getUid();
            startTXpackets = TrafficStats.getUidTxPackets(uid);
            startTXbytes = TrafficStats.getUidTxBytes(uid);

            if(startTXbytes > app.getStartTXBytes()){ // This means the traffic has increased
                if(app.isTracked()){
                    Log.d("APP_NETWORK", "Should log data at this point. Is it updated? " + startTXbytes +" bytes " + startTXpackets + " packets");
                    app.logData(startTXpackets, startTXbytes, System.currentTimeMillis());
                }
                app.update(startTXpackets, startTXbytes, System.currentTimeMillis());
            }else if(startTXbytes < app.getStartTXBytes()){ // This means the device have been rebooted
                app.reset();
                app.logData(startTXpackets, startTXbytes, System.currentTimeMillis());
            }
        }

        // We need to check for newly installed/internetusing apps
        if(updatedList.size() != applications.size()){
            List<String> currentListPackageNames = Utils.getPackageNames(applications);
            for (Application newApp : updatedList){
                if(!currentListPackageNames.contains(newApp.getPackageName())){
                    applications.add(newApp);
                }
            }
        }

        return applications;
    }
}
