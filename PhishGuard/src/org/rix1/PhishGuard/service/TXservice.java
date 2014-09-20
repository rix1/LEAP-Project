package org.rix1.PhishGuard.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import org.rix1.PhishGuard.*;
import org.rix1.PhishGuard.utils.LoadApplications;
import org.rix1.PhishGuard.utils.OnTaskCompleted;
import org.rix1.PhishGuard.utils.Utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Created by rikardeide on 18/09/14.
 * Desc:
 */

public class TXservice extends Service implements OnTaskCompleted, PropertyChangeListener  {

    private ArrayList<Application> outNetworkApps;
    private final Object[] conditions = new Object[2];
    private GlobalClass globalVar;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate(){
        super.onCreate();
        outNetworkApps = new ArrayList<Application>();
    }

    public void onDestroy(){
        super.onDestroy();

        globalVar.setServiceRunning(false);
        Log.d("APP_SERVICE", "Destroying service...");
    }

    /**
     * Because the alarm only starts after the first run, the application
     * state is already stored. When the service is waked by the alarm,
     * it loads the application list (state) from sharedPreferences, sends it
     * to the asyncTask LoadApplication who updates the list. It will get
     * notified when the async task is done through the OnTaskCompleted interface.
     * When this happens, it stores the updated list in sharedPreferences and stops
     * itself.
     * @param intent
     * @param flags
     * @param startId
     * @return
     */

    public int onStartCommand(Intent intent, int flags, int startId){
        globalVar = (GlobalClass) getApplicationContext();
        globalVar.setServiceRunning(true);

        updateApplicationList();
        return super.onStartCommand(intent, flags, START_STICKY);
    }

    private void updateApplicationList(){
        LoadApplications asyncTask = new LoadApplications(getPackageManager(),this,this);
        Boolean firstTime = globalVar.isFirstTime(); // Copy variable in order to reduce calls to sharedPrefs.
        Log.d("APP_SERVICE", "Is this first time? Should we initialize? " + firstTime);
        if(firstTime){
            conditions[0] = true;
            globalVar.setFirstTime();
        }else {
            conditions[0] = false;
            outNetworkApps = Utils.getApplicationState(getApplicationContext());
        }
        conditions[1] = outNetworkApps;
//        Log.d("APP_RX", "Service started and loaded state. outNetworkApps: " + outNetworkApps.size());

//        Log.d("APP_SERVICE", "Start Update ------------------------------------");
//        for (Application app: outNetworkApps){
//            Log.d("APP_SERVICE_SUPER", app.toString());
//        }

        asyncTask.execute(conditions);
    }

    private void storeUpdatedList(){
        Log.d("APP_SERVICE", "Storing applicationState. Updated list: " + outNetworkApps.size());
        Utils.storeApplicationState(getApplicationContext(), outNetworkApps);
    }

    @Override
    public void onTaskCompleted(ArrayList<Application> outNetworkApps) {
        Log.d("APP_SERVICE", "Task completed");
        this.outNetworkApps = outNetworkApps;
//        Log.d("APP_SERVICE", "Update ended ------------------------------------");
//        for (Application app: outNetworkApps){
//            Log.d("APP_SERVICE_SUPER", app.toString());
//        }

        storeUpdatedList();
        globalVar.setServiceRunning(false);
        stopSelf(); // Stop the service from running.
        Log.d("APP_SERVICE", "Stopping service now...");
    }

    @Override
    public void propertyChange(PropertyChangeEvent pcEvent) {
        Log.d("APP_SERVICE", "Log change recorded! The " + pcEvent.getPropertyName() + " changed from " + pcEvent.getOldValue() + " to " + pcEvent.getNewValue());
        Utils.sendNotification(pcEvent);
    }
}
