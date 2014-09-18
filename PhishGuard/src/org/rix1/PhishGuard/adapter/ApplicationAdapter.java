package org.rix1.PhishGuard.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.rix1.PhishGuard.R;
import org.rix1.PhishGuard.Application;
import org.rix1.PhishGuard.utils.*;


import java.util.ArrayList;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */

public class ApplicationAdapter extends ArrayAdapter<Application>{

    private Context context;
    private PackageManager packageManager;
    private ArrayList<Application> outTXapps;

    public ApplicationAdapter(Context context, int textViewResourceID, ArrayList<Application> outTXApplications){
        super(context, textViewResourceID, outTXApplications);
        this.context = context;
        packageManager = context.getPackageManager();
        this.outTXapps = outTXApplications;

    }

    public int getCount(){
        return ((outTXapps != null) ? outTXapps.size(): 0);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.app_list_row, null);
        }

        Application app = outTXapps.get(position);

        if (app != null) {
            int count = (int) app.getStartTXBytes();
            String countString = "";

            TextView appName = (TextView) view.findViewById(R.id.application_name);
            TextView updateCount = (TextView) view.findViewById(R.id.application_update_count);
            TextView lastUpdate = (TextView) view.findViewById(R.id.application_last_update_date);
            ImageView appIcon = (ImageView) view.findViewById(R.id.app_icon);

            countString = (count > 1000) ? Integer.toString(count / 1000) + "k" : Integer.toString(count);

            if(count > 10000){
                countString = Integer.toString(count / 10000) + "k";
                if(count > 100000){
                    countString = Integer.toString(count / 100000) + "k";
                    if(count > 1000000){
                        countString = Integer.toString(count / 1000000) + "M";
                    }
                }
            }

            if(app.getDatalog().size() > 1){
                Log.d("APP_ADAPTER", "Datalog size: " + app.getDatalog().size());
                appName.setText(app.getApplicationName());
                appName.setTextColor(context.getResources().getColor(R.color.green));
            }else{
                appName.setText(app.getApplicationName());
                appName.setTextColor(context.getResources().getColor(R.color.darkgrey));
            }
            updateCount.setText(countString);
            lastUpdate.setText(Utils.formattedDate(app.getLatestStamp()));
            appIcon.setImageURI(Uri.parse(app.getIconUri()));

            if (app.isTracked()) {
                updateCount.setTextColor(context.getResources().getColor(R.color.red));
                updateCount.setText(countString);
            } else {
                updateCount.setTextColor(context.getResources().getColor(R.color.darkgrey));
                updateCount.setText(countString);
            }
        }
        return view;
    }
}