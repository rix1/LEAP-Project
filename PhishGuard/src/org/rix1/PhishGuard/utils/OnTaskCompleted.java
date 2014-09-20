package org.rix1.PhishGuard.utils;

import org.rix1.PhishGuard.Application;
import java.util.ArrayList;

/**
 * Created by rikardeide on 16/09/14.
 * Interface shared between the LoadApplications AsyncTask and the CustomListFragment
 */

public interface OnTaskCompleted {

    void onTaskCompleted(ArrayList<Application> outNetworkApps);
}
