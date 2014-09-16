package org.rix1.PhishGuard.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.rix1.PhishGuard.R;

/**
 * Created by rikardeide on 16/09/14.
 */
public class WatchedAppsFragment extends CustomListFragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_app_list, container, false);
        return rootView;
    }

}