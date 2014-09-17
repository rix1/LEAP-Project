package org.rix1.PhishGuard.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        cal.setTimeInMillis(timestamp*1000);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return dateFormat.format(cal.getTime());
    }

}
