package org.rix1.PhishGuard.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @deprecated
 * Created by Rikard Eide on 16/09/14.
 * Description:
 */

public class TabsPagerAdapter extends FragmentPagerAdapter{

    public TabsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
//                return new AllAppsFragment();
                break;
            case 1:
//                return new WatchedAppsFragment();
                break;
            case 2:
//                return new CurrentAppFragment();
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        // Number of tabs
        return 3;
    }
}