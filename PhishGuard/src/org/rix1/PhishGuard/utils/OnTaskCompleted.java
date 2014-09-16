package org.rix1.PhishGuard.utils;

import android.content.pm.ApplicationInfo;
import java.util.List;

/**
 * Created by rikardeide on 16/09/14.
 * Interface shared between the LoadApplications AsyncTask and the CustomListFragment
 */

public interface OnTaskCompleted {

    void onTaskCompleted(List<ApplicationInfo> applicationInfos);
}