package org.rix1.PhishGuard.utils;

import org.rix1.PhishGuard.Application;
import java.util.ArrayList;

/**
 * Created by rikardeide on 16/09/14.
 * Interface shared between the LoadApplications AsyncTask and its users. With this interface
 * the result set (the updated list) is returned from the AsyncTask after being updated.
 */

public interface OnTaskCompleted {

    void onTaskCompleted(ArrayList<Application> outNetworkApps);
}
