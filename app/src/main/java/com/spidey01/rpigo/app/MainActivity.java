package com.spidey01.rpigo.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    /** Used to check if we've hooked up to a device yet. */
    private boolean mConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        Log.d(TAG, "mConnected="+mConnected);
        if (mConnected) {
            // What do we want?
        } else {
            // Launch discovery!
            // startActivity(new Intent(this, DeviceDiscoveryActivity.class));

            Intent discovery = new Intent(this, DeviceDiscoveryActivity.class);
            startActivityForResult(discovery, DeviceDiscoveryActivity.FIND_DEVICE, savedInstanceState);
        }
        Log.i(TAG, "onCreate(): mConnected is now "+mConnected);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume(): mConnected is now "+mConnected);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DeviceDiscoveryActivity.FIND_DEVICE) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra(DeviceDiscoveryActivity.RESULT_STRING);
                // set this here for now.
                Log.d(TAG, "We'd want to connect to "+name+" now.");
                mConnected = true;
            }
        }
    }
}
