package org.rix1.PhishGuard.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.gson.Gson;
import org.rix1.PhishGuard.Application;
import org.rix1.PhishGuard.GlobalClass;

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
        String json = prefs.getString(globalVars.APPLIST_NAME, "");

        // TODO: Surround with Try/Catch?
        if(!json.equals("")){
            outNetworkApps = gson.fromJson(json, globalVars.APPLIST_TYPE);
            Log.d("APP_LIST", "Data REstored2: " + outNetworkApps.get(0).toString());
            Log.d("APP_LIST", "Data REstored: " + json);
        }else Log.d("APP_LIST", "JSON was empty");

        return outNetworkApps;
    }


    public static void storeApplicationState(Context context, ArrayList<Application> outNetworkApps){
        final GlobalClass globalVars = (GlobalClass) context;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(outNetworkApps, globalVars.APPLIST_TYPE);
        Log.d("APP_LIST", "Data stored: " + json);

        prefsEditor.putString(globalVars.APPLIST_NAME, json);
        prefsEditor.commit();
    }
}
