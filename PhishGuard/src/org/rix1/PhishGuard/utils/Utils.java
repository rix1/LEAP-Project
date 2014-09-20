package org.rix1.PhishGuard.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.google.gson.Gson;
import org.rix1.PhishGuard.*;

import java.beans.PropertyChangeEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */

public class Utils {

    /**
     * Formats an unix timestamp to a readable date on the form "dd.MM.yyyy HH:mm:ss"
     * @param timestamp Unix timestamp
     * @return A formatted string
     */

    public static String formattedDate(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return dateFormat.format(cal.getTime());
    }

    public static List<String> getPackageNames(ArrayList<Application> appList){
        List<String> returnList = new ArrayList<String>();

        for (Application app: appList){
            returnList.add(app.getPackageName());
        }
        return returnList;
    }

    public static ArrayList<Application> getApplicationState(Context context){
        final GlobalClass globalVars = (GlobalClass) context;
        ArrayList<Application> outNetworkApps = new ArrayList<Application>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Gson gson = new Gson();
        String json = prefs.getString(GlobalClass.APPLIST_NAME, "");

        // TODO: Surround with Try/Catch?
        if(!json.equals("")){
            outNetworkApps = gson.fromJson(json, GlobalClass.APPLIST_TYPE);
//            Log.d("APP_UTIL", "Data REstored. First application: " + outNetworkApps.get(0).toString());
//            Log.d("APP_UTIL", "Data REstored: " + json);
        }else Log.d("APP_UTIL", "JSON was empty");

        return outNetworkApps;
    }


    // Helper method to store boolean config values
    public static void storeBooleanApplicationState(Context context, String variableName, Boolean value){
        GlobalClass globalVars = (GlobalClass) context;
        SharedPreferences settings = context.getSharedPreferences(GlobalClass.PREFS_NAME, 0);
        settings.edit().putBoolean(variableName, value).commit();
    }

    public static boolean getApplicationBool(Context context, String variableName){
        GlobalClass globalVars = (GlobalClass) context;
        SharedPreferences settings = context.getSharedPreferences(GlobalClass.PREFS_NAME, 0);
        return settings.getBoolean(variableName, true);
    }


    public static void storeApplicationState(Context context, ArrayList<Application> outNetworkApps){
        final GlobalClass globalVars = (GlobalClass) context;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(outNetworkApps, GlobalClass.APPLIST_TYPE);
//        Log.d("APP_UTIL", "Data stored: " + json);

        prefsEditor.putString(GlobalClass.APPLIST_NAME, json);
        prefsEditor.commit();
    }


    public static void sendNotification(PropertyChangeEvent pcEvent){
        long dxBytes = ((Long) pcEvent.getNewValue() - (Long) pcEvent.getOldValue());
        int notificationID = 1;
        Intent viewIntent = new Intent(GlobalClass.getAppContext(), ApplicationListActivity.class);
        viewIntent.setAction(Intent.ACTION_MAIN);
        viewIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(GlobalClass.getAppContext(), 0, viewIntent, 0);

        String notificationText = pcEvent.getPropertyName() + " recently sent " + dxBytes + " bytes";
        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(GlobalClass.getAppContext())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Phishguard warning!")
                .setContentText(notificationText)
                .setContentIntent(viewPendingIntent)
                .setSound(alarmsound);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(GlobalClass.getAppContext());

        notificationManagerCompat.notify(notificationID, notificationBuilder.build());
    }


}
