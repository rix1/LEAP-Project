package org.rix1.PhishGuard;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_list);
        Intent i = new Intent(StartActivity.this, ApplicationListActivity.class);
        startActivity(i);
    }

    public void startService(){
        // TODO: Handle start and stop of service

    }
}
