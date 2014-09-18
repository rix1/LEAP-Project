package org.rix1.PhishGuard;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.rix1.PhishGuard.service.TXservice;


public class StartActivity extends Activity{

    /**
     * Called when the activity is first created.
     */

    private Button showListbtn;
    private Button startServicebtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final GlobalClass globalVars = (GlobalClass) getApplicationContext();

        showListbtn = (Button)findViewById(R.id.btn_showList);
        startServicebtn = (Button)findViewById(R.id.btn_startStopService);
        startServicebtn.setText("Start service");

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
                if(!globalVars.isServiceRunning()) {
                    Intent i = new Intent(StartActivity.this, TXservice.class);
                    i.putExtra("name", "gunnar");
                    startService(i);
                    globalVars.setServiceRunning(true);
                    startServicebtn.setText("Stop service");
                    Log.d("APP_START", "Service started");

                }else {
                    Intent i = new Intent(StartActivity.this, TXservice.class);
                    stopService(i);
                    globalVars.setServiceRunning(false);
                    startServicebtn.setText("Start service");
                    Log.d("APP_START", "Service stopped");
                }
            }
        });
    }



    public void startService(){
        // TODO: Handle start and stop of service
    }

}
