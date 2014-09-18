package org.rix1.PhishGuard.utils;

import android.content.pm.ApplicationInfo;
import org.rix1.PhishGuard.Application;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rikardeide on 16/09/14.
 * Interface shared between the LoadApplications AsyncTask and the CustomListFragment
 */

public interface OnTaskCompleted {

    void onTaskCompleted(ArrayList<Application> outNetworkApps);
}
