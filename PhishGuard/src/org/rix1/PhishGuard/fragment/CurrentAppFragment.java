package org.rix1.PhishGuard.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.rix1.PhishGuard.R;

/**
 * Created by rikardeide on 16/09/14.
 */
public class CurrentAppFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_current_app, container, false);

        return rootView;
    }


}