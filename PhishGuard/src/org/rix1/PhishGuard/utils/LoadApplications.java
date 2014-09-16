package org.rix1.PhishGuard.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikard Eide on 16/09/14.
 * Description:
 * Private class to start and handle the loading of
 * all applications into the list. This could take a long time,
 * so this is done in a Async Task to take resources off the main thread
 */

public class LoadApplications extends AsyncTask<Void, Void, List<ApplicationInfo>> {

    private ProgressDialog progress;
    private List<ApplicationInfo> allApplications;
    private OnTaskCompleted listener;
    private PackageManager pm;
    private Context context;

    public LoadApplications(OnTaskCompleted listener, Context context, PackageManager pm){
        this.listener = listener;
        this.context = context;
        this.pm = pm;

        progress = new ProgressDialog(context);
        allApplications = new ArrayList<ApplicationInfo>();

    }

    @Override
    protected List<ApplicationInfo> doInBackground(Void ... voids) {

        allApplications = checkForLaunchIntent(pm.getInstalledApplications(PackageManager.GET_META_DATA));

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

    protected void onPreExecute() {
        progress = ProgressDialog.show(context, null, "Loading application info...");
        super.onPreExecute();
    }

    protected void onPostExecute(List<ApplicationInfo> appI) {
        progress.dismiss();
        listener.onTaskCompleted(allApplications);
    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
