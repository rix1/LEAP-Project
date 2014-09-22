package org.rix1.PhishGuard;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import org.rix1.PhishGuard.service.Alarm;


/**
 * Created by Rikard Eide on 10/09/14.
 * Description: First activity to run when opening the application. Nothing special to see here...
 *
 **/

public class StartActivity extends Activity{

    private Switch aSwitch;
    private GlobalClass globalVars;
    private final Alarm alarm = new Alarm();

    //      Called when the activity is first created.

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final GlobalClass globalVars = (GlobalClass) getApplicationContext();

        Button showListbtn = (Button) findViewById(R.id.btn_showList);
        Button aboutbtn = (Button) findViewById(R.id.btn_about);
        aSwitch = (Switch) findViewById(R.id.switch_monitor);
        aSwitch.setChecked(globalVars.isMonitoring());

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                globalVars.setMonitoring(aSwitch.isChecked());
            }
        });

        showListbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, ApplicationListActivity.class);
                startActivity(i);
            }
        });


        aboutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInfoDialog();
            }
        });
    }

    private void displayInfoDialog(){
        String message = getString(R.string.android_security);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Android Security");
        builder.setMessage(message);
        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    protected  void onStart() {
        super.onStart();
//        Log.d("APP_START", "onStart called");
        globalVars = (GlobalClass) getApplicationContext();
        globalVars.setStartActivityRunning(true);

        // To avoid race conditions with the application state
        // in sharedPreferences, we turn off the monitoring
        // when the application starts.
        alarm.CancelAlarm(this);
//        Log.d("APP_START", "Alarm cancelled");

    }

    protected void onStop(){
        super.onStop();
        globalVars.setStartActivityRunning(false);
//        Log.d("APP_START", "onStop called. List activity: " + globalVars.isListActivityRunning());

        // Means that the application (UI) is no longer showing
        // and monitoring can begin again
        if(!globalVars.isListActivityRunning() && globalVars.isMonitoring()) {
//            Log.d("APP_START", " alarm set");
            alarm.SetAlarm(this);
        }
    }
}