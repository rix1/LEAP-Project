package org.rix1.PhishGuard;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.rix1.PhishGuard.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */

public class ApplicationAdapter extends ArrayAdapter<Application>{

    private Context context;
    private PackageManager packageManager;
    private ArrayList<Application> trackedApplications;

    public ApplicationAdapter(Context context, int textViewResourceID, ArrayList<Application> trackedApplications){
        super(context, textViewResourceID, trackedApplications);
        this.context = context;
        packageManager = context.getPackageManager();
        this.trackedApplications = trackedApplications;
    }

    public int getCount(){
        return ((trackedApplications != null) ? trackedApplications.size(): 0);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.app_list_row, null);
        }

        Application app = trackedApplications.get(position);

        if(app != null){
            int count = (int) app.getPacketsSent();

            TextView appName = (TextView) view.findViewById(R.id.application_name);
            TextView updateCount = (TextView) view.findViewById(R.id.application_update_count);
            TextView lastUpdate = (TextView) view.findViewById(R.id.application_last_update_date);
            ImageView appIcon = (ImageView) view.findViewById(R.id.app_icon);

            Application temp = getApplication(app.getPackageName());

            appName.setText(app.getApplicationName());


            String countString = (count >1000) ? Integer.toString(count/1000) + "k":Integer.toString(count);

            updateCount.setText(countString);
            lastUpdate.setText(Utils.formattedDate(app.getLatestStamp()));
            appIcon.setImageDrawable(app.getIcon());

            if(app.shouldWarn()){
                updateCount.setTextColor(context.getResources().getColor(R.color.red));
                updateCount.setText(countString);
            }else {
                updateCount.setTextColor(context.getResources().getColor(R.color.darkgrey));
                updateCount.setText(countString);
            }
        }
        return view;
    }

    // Warning! Might return null
    public Application getApplication(String packageName){
        for(Application app:trackedApplications){
            if(app.getPackageName().equals(packageName)){
                return app;
            }
        }
        return null;
    }

}
