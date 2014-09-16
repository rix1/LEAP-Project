package org.rix1.PhishGuard.fragment;


import org.rix1.PhishGuard.R;
import org.rix1.PhishGuard.adapter.ApplicationAdapter;

/**
 * Created by rikardeide on 16/09/14.
 *
 */

public class CurrentAppFragment extends AllAppsFragment{

    public void buildList(){

        listAdapter = new ApplicationAdapter(context, R.layout.app_list_row, listRXapps);
    }


}