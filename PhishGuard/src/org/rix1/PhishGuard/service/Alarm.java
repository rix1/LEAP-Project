package org.rix1.PhishGuard.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import org.rix1.PhishGuard.GlobalClass;

/**
 * Created by rikardeide on 18/09/14.
 * Broadcast receiver to schedule the running of the NetworkService
 */

public class Alarm extends BroadcastReceiver {

    private static int instances = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        instances ++;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wakeLock.acquire();

        // Start service from here
        Intent serviceIntent = new Intent(context, NetworkService.class);
        context.startService(serviceIntent);
//        Log.d("APP_ALARM", "Alarm fired. Instances: " + instances);

        wakeLock.release();
    }

    // THe GlobalClass.updateInterval is the interval in which the alarm, and the service will be run.
    public void SetAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
        long INTERVAL = GlobalClass.updateInterval;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), INTERVAL, pendingIntent);
    }

    public void CancelAlarm(Context context){
//        Log.d("APP_ALARM", "Alarm cancelled by: " + context.toString());
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
