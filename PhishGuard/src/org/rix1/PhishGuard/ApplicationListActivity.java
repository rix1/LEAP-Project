package org.rix1.PhishGuard;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;
import org.rix1.PhishGuard.adapter.ApplicationAdapter;
import org.rix1.PhishGuard.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */

public class ApplicationListActivity extends ListActivity{

    private PackageManager pm = null;
    private List<ApplicationInfo> allApplications = null;
    private ApplicationAdapter listAdapter = null;

    private ArrayList<Application> outNetworkApps;
    private ArrayList<Application> tempListTXapps;



    private Application currentApplication;
    private NetworkService networkService;

    private Switch menuSwitch;
    private boolean showAllApps;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("APP_LIST", "onCreate called");

        outNetworkApps = new ArrayList<Application>();
        setContentView(R.layout.activity_app_list);
        pm = getPackageManager();
        networkService = new NetworkService(pm);

        initList();

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // Maybe remove this?
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_about:
                displayInfoDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayInfoDialog(){
        String message = getString(R.string.android_security);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        String message = getString(R.string.application_desc_start) + " " + currentApplication.getStartTXBytes() + " " +getString(R.string.application_desc_end);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(currentApplication.getApplicationName());
        builder.setMessage(message);

        builder.setPositiveButton("Yes, notify me", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                currentApplication.setTracked(true);
                Log.d("APP_LIST", currentApplication.toString());
                Collections.sort(outNetworkApps);
                listAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No Thanks!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                currentApplication.setTracked(false);
                Log.d("APP_LIST", currentApplication.toString());
                Collections.sort(outNetworkApps);
                listAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });

        builder.show();
    }

    protected void onListItemClick(ListView listview, View view, int position, long id){
        super.onListItemClick(listview, view, position, id);

        setCurrentApplication(outNetworkApps.get(position));
        displayApplicationDialog();

    }

    public void setCurrentApplication(Application application){
        this.currentApplication = application;
    }

    protected void onSaveInstanceState (Bundle outState){
        Log.d("APP_LIST", "onSaveInstanceState called");

        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("APP_LIST", "onRestoreInstanceState called");

        super.onRestoreInstanceState(savedInstanceState);
/*
        Log.d("APP_LIST", "Restoring state....");
        Gson gson = new Gson();
        if(savedInstanceState.containsKey(APPLIST_NAME)){
            String json = savedInstanceState.getString(APPLIST_NAME, "");
            Log.d("APP_LIST", "Restoring state...JSON:   " + json);
            outNetworkApps = gson.fromJson(json, listOfApplicationObject);
        }else Log.d("APP_LIST", "Key '" + APPLIST_NAME +"' not found");
*/
//        outNetworkApps = new ArrayList<Application>();
    }

    /**
     * Store the application state
     */
    protected void onStop(){
        super.onStop();
        Log.d("APP_LIST", "onStop called");
        Utils.storeApplicationState(getApplicationContext(), outNetworkApps);
    }

    protected  void onStart(){
        super.onStart();
        Log.d("APP_LIST", "onStart called");
        outNetworkApps = Utils.getApplicationState(getApplicationContext());
    }

    protected void onPause(){
        super.onPause();
        Log.d("APP_LIST", "onPause called");

    }

    public void onResume(){
        super.onResume();
        Log.d("APP_LIST", "onResume called");

    }

    public void onRestart(){
        super.onRestart();
        Log.d("APP_LIST", "onRestart called");

    }

    public void initList(){
        boolean shouldUpdate = false;
        if(outNetworkApps != null)
            shouldUpdate = true;
        new LoadApplications().execute(shouldUpdate);

    }

    public void updateList(){
        for (Application appA : tempListTXapps){
            if(!outNetworkApps.contains(appA)){
                Log.d("APP_LIST", "Adding app " + appA.getPackageName());
                outNetworkApps.add(appA); // Add new objects
            }
        }
    }

    public void viewList(){
        Collections.sort(outNetworkApps);
        listAdapter = new ApplicationAdapter(ApplicationListActivity.this, R.layout.app_list_row, outNetworkApps);
    }

    /**
     * Private class to start and handle the loading of
     * all applicaitons into the list. This could take a long time,
     * so this is done in a Async Task to take resources off the main thread
     */

    private class LoadApplications extends AsyncTask<Boolean, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Boolean ... shouldUpdate) {

            allApplications = checkForLaunchIntent(pm.getInstalledApplications(PackageManager.GET_META_DATA));

            if(shouldUpdate[0]){
                outNetworkApps = networkService.update(outNetworkApps, allApplications);
            }else {
                outNetworkApps = networkService.init(allApplications);
            }
            viewList();
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
            progress = ProgressDialog.show(ApplicationListActivity.this, null, "Loading application info...");
            super.onPreExecute();
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }

}