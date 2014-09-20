package org.rix1.PhishGuard;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.rix1.PhishGuard.adapter.ApplicationAdapter;
import org.rix1.PhishGuard.service.Alarm;
import org.rix1.PhishGuard.utils.LoadApplications;
import org.rix1.PhishGuard.utils.OnTaskCompleted;
import org.rix1.PhishGuard.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */

public class ApplicationListActivity extends ListActivity implements OnTaskCompleted{

    private PackageManager pm = null;
    private ApplicationAdapter listAdapter = null;
    private ArrayList<Application> outNetworkApps;
    private Application currentApplication;
    private GlobalClass globalVars;
    private Alarm alarm = new Alarm();


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
        String message = getString(R.string.about_listview);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How to track:");
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
        String message = getString(R.string.application_desc_start) + " " + currentApplication.getStartTXBytes() + " bytes.";
        message += "\n";
        if(currentApplication.getDatalog().size() > 1){
            message += "History:\n";
            for (Datalog data : currentApplication.getDatalog()){
                message += Utils.formattedDate(data.getTimeStamp()) + " - " + data.getDx() + " bytes\n";
            }
        }
        message += getString(R.string.application_desc_end);
        Log.d("APP_LIST", currentApplication.toString());

        TextView dialogMessage = new TextView(this);
        dialogMessage.setText(message);
        dialogMessage.setGravity(Gravity.CENTER_HORIZONTAL);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(currentApplication.getApplicationName());
        builder.setView(dialogMessage);

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
     * Restore the application state
     * To avoid race conditions with the application state
     * in sharedPreferences, we turn off the monitoring
     * when the application starts.
     */

    protected  void onStart(){
        super.onStart();
        globalVars = (GlobalClass) getApplicationContext();
        globalVars.setListActivityRunning(true);
        Log.d("APP_LIST", "onStart called");
        initList();

        alarm.CancelAlarm(this);
        Log.d("APP_LIST", "Alarm cancelled");

    }


    /**
     * Store the application state
     * Means that the application (UI) is no longer showing
     * and monitoring can begin again
     */

    protected void onStop(){
        super.onStop();
        Log.d("APP_LIST", "onStop called");
        Utils.storeApplicationState(getApplicationContext(), outNetworkApps);
        globalVars.setListActivityRunning(false);
        Log.d("APP_START", "onStop called. Start activity: " + globalVars.isStartActivityRunning());


        if(!globalVars.isStartActivityRunning() && globalVars.isMonitoring()) {
            Log.d("APP_LIST", " alarm set");
            alarm.SetAlarm(this);
        }
        finish();
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
        LoadApplications loadApplications = new LoadApplications(pm, this, this);
        Object[] conditions = new Object[2];
        boolean shouldInit;

        // Check if this is the first time running the application
        if(globalVars.isFirstTime()){
            Log.d("APP_LIST", "Initiating list...");
            shouldInit = true;
            conditions[0] = shouldInit;
            conditions[1] = outNetworkApps;
            loadApplications.execute(conditions);

            // Register that the list have been initialized.
            globalVars.setFirstTime();
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