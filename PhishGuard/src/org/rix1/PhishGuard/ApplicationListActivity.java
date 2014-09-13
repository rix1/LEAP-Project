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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */

public class ApplicationListActivity extends ListActivity{

    private PackageManager pm = null;
    private List<ApplicationInfo> appInfoList = null;
    private ApplicationAdapter listAdapter = null;
    private ArrayList<Application> outNetworkApps;
    private Application currentApplication;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_list);
        pm = getPackageManager();
        new LoadApplications().execute();
        outNetworkApps = new ArrayList<Application>();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }


    private void updateApplist(){
        //TODO: Make call to networkService and get list of applications.
        outNetworkApps.clear();

        for(ApplicationInfo appI : appInfoList){
            try {
                outNetworkApps.add(new Application(appI.packageName, appI.loadLabel(pm).toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayApplicationDialog(){
        String message = getString(R.string.application_desc_start) + " " +currentApplication.getConnectionsMade() + " " +getString(R.string.application_desc_end);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(currentApplication.getApplicationName());
        builder.setMessage(message);

        builder.setPositiveButton("Yes, notify me", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                currentApplication.setNotificationFlag(true);
                listAdapter.notifyDataSetChanged();
                print();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No Thanks!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                currentApplication.setNotificationFlag(false);
                listAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void print(){
        Collections.sort(outNetworkApps);
        for (Application app: outNetworkApps){
            Log.d("APP_SORT",app.getConnectionsMade() + " : " + app.getApplicationName());
        }
    }

    protected void onListItemClick(ListView listview, View view, int position, long id){
        super.onListItemClick(listview, view, position, id);

        ApplicationInfo app = appInfoList.get(position);
        setCurrentApplication(app.packageName);
        displayApplicationDialog();

    }

    public void setCurrentApplication(String packageName){
        for(Application app : outNetworkApps){
            if(app.getPackageName().equals(packageName))
                currentApplication = app;
        }
    }

    // Maybe remove this?
    public boolean onOptionsItemSelected(MenuItem item){
        boolean result = true;

        if(item.getItemId() == R.id.menu_about){

        }else result = super.onOptionsItemSelected(item);
        return result;
    }




    /**
     * Private class to start and handle the loading of
     * all applicaitons into the list. This could take a long time,
     * so this is done in a Async Task to take resources off the main thread
     */

    private class LoadApplications extends AsyncTask<Void, Void, Void>{

        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... voids) {

            appInfoList = checkForLaunchIntent(pm.getInstalledApplications(PackageManager.GET_META_DATA));
            updateApplist();
            listAdapter = new ApplicationAdapter(ApplicationListActivity.this, R.layout.app_list_row, appInfoList, outNetworkApps);
            return null;
        }


        /**
         * This method receives a list containing meta data about all installed applications.
         * It checks weather or not this application can be started or not (getLaunchIntent
         * searches for main activities associated with the given package).
         * @param list A list with info with META_DATA from all applications
         * @return list of all applications that can be started.
         */

        private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list){
            ArrayList<ApplicationInfo> applicationList = new ArrayList<ApplicationInfo>();

            for(ApplicationInfo info: list){
                try {
                    if(pm.getLaunchIntentForPackage(info.packageName) != null){
                        applicationList.add(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return applicationList;
        }


        protected void onCancelled(){
            super.onCancelled();
        }

        protected void onPostExecute(Void result){
            setListAdapter(listAdapter);
            progress.dismiss();
            super.onPostExecute(result);
        }

        protected void onPreExecute(){
            progress = ProgressDialog.show(ApplicationListActivity.this, null, "Loading application info...");
            super.onPreExecute();
        }

        protected void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);

        }
    }

}