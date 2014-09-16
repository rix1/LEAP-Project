package org.rix1.PhishGuard.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import org.rix1.PhishGuard.fragment.AllAppsFragment;
import org.rix1.PhishGuard.fragment.CurrentAppFragment;
import org.rix1.PhishGuard.fragment.WatchedAppsFragment;

/**
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
                return new AllAppsFragment();
            case 1:
                return new WatchedAppsFragment();
            case 2:
                return new CurrentAppFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // Number of tabs
        return 3;
    }
}
