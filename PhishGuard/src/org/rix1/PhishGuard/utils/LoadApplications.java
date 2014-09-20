package org.rix1.PhishGuard.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import org.rix1.PhishGuard.Application;
import org.rix1.PhishGuard.service.NetworkService;
import org.rix1.PhishGuard.service.TXservice;

import java.util.ArrayList;
import java.util.List;

/**
 * Private class to start and handle the loading of
 * all applicaitons into the list. This could take a long time,
 * so this is done in a Async Task to take resources off the main thread
 */

public class LoadApplications extends AsyncTask<Object, Void, Void> {

    private ProgressDialog progress = null;
    private final PackageManager pm;
    private final Context context;
    private List<ApplicationInfo> allApplications;
    private final NetworkService networkService;
    private final OnTaskCompleted listener;
    private ArrayList<Application> outNetworkApps;

    public LoadApplications(PackageManager pm, Context context, OnTaskCompleted listener){
        this.listener = listener;
        this.pm = pm;
        this.context = context;
        allApplications = new ArrayList<ApplicationInfo>();
        networkService = new NetworkService(pm);
    }

    /**
     * Structure of args: [0]: ShouldUpdate [2]: List<Application>
     * @param args
     * @return
     */
    @Override
    protected Void doInBackground(Object ... args) {
        Boolean shouldInit = ((Boolean) args[0]).booleanValue();
        Log.d("APP_ASYNC", "Should we initialize now? args[0]: " + args[0] + " shouldinit: " + shouldInit);
        outNetworkApps = (ArrayList<Application>)args[1];

        allApplications = checkForLaunchIntent(pm.getInstalledApplications(PackageManager.GET_META_DATA));

        if(shouldInit){
            Log.d("APP_ASYNC", "Init list: " + outNetworkApps.toString());
            outNetworkApps = networkService.init(allApplications);
        }else {
            Log.d("APP_ASYNC", "Updating list: " + outNetworkApps.toString());
            outNetworkApps = networkService.update(outNetworkApps, allApplications);
        }
        return null;
    }


    /**
     * This method receives a list containing meta data about all installed applications.
     * It checks weather or not this application can be started or not (getLaunchIntent
     * searches for main activities associated with the given package).
     *
     * @param list A list with info with META_DATA from all applications
     * @return list of all applications that can be started.
     */

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applicationList = new ArrayList<ApplicationInfo>();

        for (ApplicationInfo info : list) {
            try {
                if (pm.getLaunchIntentForPackage(info.packageName) != null) {
                    applicationList.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return applicationList;
    }


    protected void onCancelled() {
        super.onCancelled();
    }

    protected void onPostExecute(Void result) {
        if(!(listener instanceof TXservice))
            progress.dismiss();
        Log.d("APP_ASYNC", "onPostExecute called...");
        listener.onTaskCompleted(outNetworkApps);
        super.onPostExecute(result);
    }

    protected void onPreExecute() {
        if(!(listener instanceof TXservice))
            progress = ProgressDialog.show(context, null, "Loading application info...");
        super.onPreExecute();
    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}