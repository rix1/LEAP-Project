package org.rix1.PhishGuard;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikard Eide on 12/09/14.
 * Description:
 */

public class ApplicationAdapter extends ArrayAdapter<ApplicationInfo>{

    private Context context;
    private List<ApplicationInfo> appList;
    private PackageManager packageManager;
    private ArrayList<Application> trackedApplications;

    public ApplicationAdapter(Context context, int textViewResourceID, List<ApplicationInfo> appList, ArrayList<Application> trackedApplications){
        super(context, textViewResourceID, appList);
        this.context = context;
        this.appList = appList;
        packageManager = context.getPackageManager();
        this.trackedApplications = trackedApplications;
    }

    public int getCount(){
        return ((appList != null) ? appList.size(): 0);
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

        ApplicationInfo data = appList.get(position);

        if(data != null){
            TextView appName = (TextView) view.findViewById(R.id.application_name);
            TextView updateCount = (TextView) view.findViewById(R.id.application_update_count);
            TextView lastUpdate = (TextView) view.findViewById(R.id.application_last_update_date);
            ImageView appIcon = (ImageView) view.findViewById(R.id.app_icon);

            Application temp = getApplication(data.packageName);

            long timestamp = (temp != null)? temp.getLatestStamp():-1;
            int connectionsMade = (temp != null)? temp.getConnectionsMade():-1;

            appName.setText(data.loadLabel(packageManager));
            updateCount.setText(Integer.toString(connectionsMade));
            lastUpdate.setText(utils.formattedDate(timestamp));
            appIcon.setImageDrawable(data.loadIcon(packageManager));
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
