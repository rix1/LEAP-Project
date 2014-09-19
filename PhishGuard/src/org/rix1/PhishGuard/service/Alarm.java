package org.rix1.PhishGuard.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;
import org.rix1.PhishGuard.ApplicationListActivity;
import org.rix1.PhishGuard.GlobalClass;
import org.rix1.PhishGuard.StartActivity;

/**
 * Created by rikardeide on 18/09/14.
 * Broadcast receiver to schedule the running of the TX service
 */

public class Alarm extends BroadcastReceiver {

    /**
     * This is the interval in which the alarm, and the service will be run.
     */
    private final long INTERVAL = 1000*10; // Milisec * seconds
    private static int instances = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        instances ++;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wakeLock.acquire();

        // Start service from here
        // TODO: Start service
        Intent serviceIntent = new Intent(context, TXservice.class);
        context.startService(serviceIntent);
        Toast.makeText(context, "Alarm!!!", Toast.LENGTH_SHORT).show();
        Log.d("APP_ALARM", "Alarm fired. Instances: " + instances);

        wakeLock.release();
    }

    public void SetAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), INTERVAL, pendingIntent);
    }

    public void CancelAlarm(Context context){
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
