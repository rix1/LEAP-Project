package org.rix1.PhishGuard.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rikardeide on 18/09/14.
 * Desc: Broadcast receiver that gets notified when the device
 * boots, and starts the network service.
 */

public class BootBroadcast extends BroadcastReceiver {

    Alarm alarm = new Alarm();

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            alarm.SetAlarm(context);
            //  context.startService(new Intent(context, TXservice.class));
        }
    }
}
