package org.rix1.PhishGuard.utils;

import org.rix1.PhishGuard.Application;

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

}
