package org.rix1.PhishGuard;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import org.rix1.PhishGuard.service.Alarm;
import org.rix1.PhishGuard.service.TXservice;
import org.rix1.PhishGuard.utils.Utils;


public class StartActivity extends Activity{

    /**
     * Called when the activity is first created.
     */

    private Button showListbtn;
    private Button startServicebtn;
    private Switch aSwitch;
    private GlobalClass globalVars;
    private Alarm alarm = new Alarm();
    private boolean monitor;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final GlobalClass globalVars = (GlobalClass) getApplicationContext();

        showListbtn = (Button) findViewById(R.id.btn_showList);
        startServicebtn = (Button) findViewById(R.id.btn_startStopService);
        startServicebtn.setText("Stop service and alarm");
        aSwitch = (Switch) findViewById(R.id.switch_monitor);
        aSwitch.setChecked(globalVars.isMonitoring());

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                globalVars.setMonitoring(aSwitch.isChecked());
                Log.d("APP_START", "Switch!");
            }
        });

        showListbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, ApplicationListActivity.class);
                startActivity(i);
            }
        });


        startServicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent i = new Intent(StartActivity.this, TXservice.class);
                 stopService(i);
                 globalVars.setServiceRunning(false);
                 alarm.CancelAlarm(getApplicationContext());
                 Log.d("APP_START", "Service stopped");
            }
        });
    }

    protected  void onStart() {
        super.onStart();
        Log.d("APP_START", "onStart called");
        globalVars = (GlobalClass) getApplicationContext();
        globalVars.setStartActivityRunning(true);

        // To avoid race conditions with the application state
        // in sharedPreferences, we turn off the monitoring
        // when the application starts.
        alarm.CancelAlarm(this);
        Log.d("APP_START", "Alarm cancelled");

    }

    protected void onStop(){
        super.onStop();
        globalVars.setStartActivityRunning(false);
        Log.d("APP_START", "onStop called. List activity: " + globalVars.isListActivityRunning());

        // Means that the application (UI) is no longer showing
        // and monitoring can begin again
        if(!globalVars.isListActivityRunning() && globalVars.isMonitoring()) {
            Log.d("APP_START", " alarm set");
            alarm.SetAlarm(this);
        }
    }
}
