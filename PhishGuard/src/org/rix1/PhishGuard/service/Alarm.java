package org.rix1.PhishGuard.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;
import org.rix1.PhishGuard.StartActivity;

/**
 * Created by rikardeide on 18/09/14.
 * Broadcast receiver to schedule the running of the TX service
 */

public class Alarm extends BroadcastReceiver {

    private final long INTERVAL = 1000*10; // Milisec * seconds

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wakeLock.acquire();

        // Start service from here
        Intent startService = new Intent(context, TXservice.class);
        // TODO: Start service
        Toast.makeText(context, "Alarm!!!", Toast.LENGTH_SHORT).show();

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
