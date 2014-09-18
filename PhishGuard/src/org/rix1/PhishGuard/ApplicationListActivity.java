package org.rix1.PhishGuard;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import org.rix1.PhishGuard.adapter.ApplicationAdapter;
import org.rix1.PhishGuard.utils.LoadApplications;
import org.rix1.PhishGuard.utils.OnTaskCompleted;
import org.rix1.PhishGuard.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */

public class ApplicationListActivity extends ListActivity implements OnTaskCompleted{

    private PackageManager pm = null;
    private ApplicationAdapter listAdapter = null;
    private ArrayList<Application> outNetworkApps;
    private Application currentApplication;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("APP_LIST", "onCreate called");

        outNetworkApps = new ArrayList<Application>();
        pm = getPackageManager();

        setContentView(R.layout.activity_app_list);
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
    }

    /**
     * Store the application state
     */

    protected void onStop(){
        super.onStop();
        Log.d("APP_LIST", "onStop called");
        Utils.storeApplicationState(getApplicationContext(), outNetworkApps);
    }


    /**
     * Restore the application state
     */

    protected  void onStart(){
        super.onStart();
        Log.d("APP_LIST", "onStart called");
        initList();
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

    private void initList(){
        final GlobalClass globalVars = (GlobalClass) getApplicationContext();
        SharedPreferences settings = getSharedPreferences(globalVars.PREFS_NAME, 0);
        LoadApplications loadApplications = new LoadApplications(pm, this, this);
        Object[] conditions = new Object[2];
        boolean shouldInit;

        if(settings.getBoolean(globalVars.FIRST_RUN, true)){
            Log.d("APP_LIST", "Initiating list...");
            shouldInit = true;
            conditions[0] = shouldInit;
            conditions[1] = outNetworkApps;
            loadApplications.execute(conditions);

            globalVars.setFirstTime(false);
            settings.edit().putBoolean(globalVars.FIRST_RUN, false).commit();
        }else{
            outNetworkApps = Utils.getApplicationState(getApplicationContext());
            viewList();
        }
    }

    public void viewList(){
        Log.d("APP_LIST", "ViewList called...");
        Collections.sort(outNetworkApps);
        listAdapter = new ApplicationAdapter(ApplicationListActivity.this, R.layout.app_list_row, outNetworkApps);
        this.setListAdapter(listAdapter);
    }

    /**
     * Should only be called first time (on init)
     * @param outApps
     */

    @Override
    public void onTaskCompleted(ArrayList<Application> outApps) {
        this.outNetworkApps = outApps;
        viewList();
    }
}