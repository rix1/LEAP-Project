package org.rix1.PhishGuard.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.rix1.PhishGuard.Application;
import org.rix1.PhishGuard.R;
import org.rix1.PhishGuard.adapter.ApplicationAdapter;

import java.util.ArrayList;

/**
 * Created by rikardeide on 16/09/14.
 *
 */

public class WatchedAppsFragment extends AllAppsFragment {

    public void buildList() {
        ArrayList<Application> trackedApplications = new ArrayList<Application>();
        listRXapps = networkService.update(listRXapps);
        for (Application app : listRXapps) {
            if (app.isTracked()) {
                trackedApplications.add(app);
//            }
//            trackedApplications.add(new Application(123434, "gunnar.packet", "gunnar heter", 1234, 12334, null));
            }
            listAdapter = new ApplicationAdapter(context, R.layout.app_list_row, trackedApplications);
        }
    }
}