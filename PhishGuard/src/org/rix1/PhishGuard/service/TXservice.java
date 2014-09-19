package org.rix1.PhishGuard.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import org.rix1.PhishGuard.Application;
import org.rix1.PhishGuard.GlobalClass;
import org.rix1.PhishGuard.utils.LoadApplications;
import org.rix1.PhishGuard.utils.OnTaskCompleted;
import org.rix1.PhishGuard.utils.Utils;

import java.util.ArrayList;

/**
 * Created by rikardeide on 18/09/14.
 * Desc:
 */

public class TXservice extends Service implements OnTaskCompleted{

    private Alarm alarm = new Alarm();
    private ArrayList<Application> outNetworkApps;
    private Object[] conditions = new Object[2];
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
        final GlobalClass globalVar = (GlobalClass) getApplicationContext();
        globalVar.setServiceRunning(false);
        Toast.makeText(this, "onDestroy: Alarm canceled...", Toast.LENGTH_SHORT).show();

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

        Toast.makeText(this, "Service started...", Toast.LENGTH_SHORT).show();
//        alarm.SetAlarm(TXservice.this); // I think we need this for booting..

        updateApplicationList();
        return super.onStartCommand(intent, flags, START_STICKY);
    }

    public void onStart(Context context, Intent intent, int startId){
//        alarm.SetAlarm(context);
    }

    private void updateApplicationList(){
        LoadApplications asyncTask = new LoadApplications(getPackageManager(),this,this);

        outNetworkApps = Utils.getApplicationState(getApplicationContext());
        Log.d("APP_RX", "Service started and loaded state. outNetworkApps: " + outNetworkApps.size());

        conditions[0] = true; // Because we only want to update, this should always be true
        conditions[1] = outNetworkApps;
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
        storeUpdatedList();
        globalVar.setServiceRunning(false);
        stopSelf(); // Stop the service from running.
        Log.d("APP_SERVICE", "This should crash? Service should have stopped ?");
    }
}
