package org.rix1.PhishGuard;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import org.rix1.PhishGuard.fragment.AllAppsFragment;

public class StartActivity extends Activity{

    /**
     * Called when the activity is first created.
     */

    private Button showListbtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        showListbtn = (Button)findViewById(R.id.btn_showList);

        showListbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, ApplicationListActivity.class);
                startActivity(i);
            }
        });



    }

    public void startService(){
        // TODO: Handle start and stop of service
    }

}
