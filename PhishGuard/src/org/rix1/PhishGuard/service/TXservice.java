package org.rix1.PhishGuard.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import org.rix1.PhishGuard.GlobalClass;
import org.rix1.PhishGuard.StartActivity;

/**
 * Created by rikardeide on 18/09/14.
 * Desc:
 */

public class TXservice extends Service {

    private Alarm alarm = new Alarm();
    private final int ALARMCOUNT = 2;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate(){
        super.onCreate();
    }

    public void onDestroy(){
        super.onDestroy();
        final GlobalClass globalVar = (GlobalClass) getApplicationContext();

        globalVar.setServiceRunning(false);
        alarm.CancelAlarm(this);
        Toast.makeText(this, "onDestroy: Alarm canceled...", Toast.LENGTH_SHORT).show();

    }

    // TODO: Remember to call stopSelf();
    // Logic goes here:
    public int onStartCommand(Intent intent, int flags, int startId){
        final GlobalClass globalVar = (GlobalClass) getApplicationContext();
        Toast.makeText(this, "Service started...", Toast.LENGTH_SHORT).show();
        alarm.SetAlarm(TXservice.this);
        globalVar.setServiceRunning(true);



        return super.onStartCommand(intent, flags, START_STICKY);
    }

    public void onStart(Context context, Intent intent, int startId){
        alarm.SetAlarm(context);
    }

}
