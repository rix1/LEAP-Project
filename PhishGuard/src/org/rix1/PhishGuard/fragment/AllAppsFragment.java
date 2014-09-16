package org.rix1.PhishGuard.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.*;
import android.widget.ListView;
import android.widget.Switch;
import org.rix1.PhishGuard.NetworkService;
import org.rix1.PhishGuard.R;
import org.rix1.PhishGuard.adapter.ApplicationAdapter;
import org.rix1.PhishGuard.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */

public class AllAppsFragment extends ListFragment {


    protected  PackageManager pm = null;
    protected  List<ApplicationInfo> allApplications = null;
    protected  ApplicationAdapter listAdapter = null;

    protected  static ArrayList<Application> listRXapps;

    protected  Application currentApplication;
    protected  NetworkService networkService;
    protected  Context context;
    protected  Switch menuSwitch;
    protected  boolean showAllApps;

    protected  boolean firstRun = true;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        context = getActivity();

        View rootView = inflater.inflate(R.layout.fragment_app_list, container, false);
        pm = context.getPackageManager();
        listRXapps = new ArrayList<Application>();
        networkService = new NetworkService(pm);

        new LoadApplications().execute();
            firstRun = false;
        return rootView;
    }

    private void displayInfoDialog(){
        String message = getString(R.string.android_security);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Android Security");
        builder.setMessage(message);
        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }



    private void displayApplicationDialog(){
        String message = getString(R.string.application_desc_start) + " " + currentApplication.getPacketsSent() + " " +getString(R.string.application_desc_end);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(currentApplication.getApplicationName());
        builder.setMessage(message);

        builder.setPositiveButton("Yes, notify me", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                currentApplication.setTracked(true);
                Log.d("APP_LIST", currentApplication.toString());
                listAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No Thanks!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                currentApplication.setTracked(false);
                Log.d("APP_LIST", currentApplication.toString());
                listAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void onListItemClick(ListView listview, View view, int position, long id){
        super.onListItemClick(listview, view, position, id);

        setCurrentApplication(listRXapps.get(position));
        currentApplication = listRXapps.get(0);                      // Set a current app.

        displayApplicationDialog();

    }

    public void setCurrentApplication(Application application){
        this.currentApplication = application;
    }


    public void buildList(){
        listAdapter = new ApplicationAdapter(context, R.layout.app_list_row, listRXapps);
    }

        /**
         * Private class to start and handle the loading of
         * all applicaitons into the list. This could take a long time,
         * so this is done in a Async Task to take resources off the main thread
         */

        private class LoadApplications extends AsyncTask<Void, Void, Void> {

            private ProgressDialog progress = null;

            @Override
            protected Void doInBackground(Void... voids) {

                allApplications = checkForLaunchIntent(pm.getInstalledApplications(PackageManager.GET_META_DATA));

                if(firstRun) {
                    listRXapps = networkService.init(allApplications); // Get all apps having outgoing traffic
                }else listRXapps = networkService.update(listRXapps);

                buildList();
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
                setListAdapter(listAdapter);
                progress.dismiss();
                super.onPostExecute(result);
            }

            protected void onPreExecute() {
                progress = ProgressDialog.show(context, null, "Loading application info...");
                super.onPreExecute();
            }

            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);

            }
        }

}