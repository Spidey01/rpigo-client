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
        setContentView(R.layout.activity_main);

        Log.d(TAG, "mConnected="+mConnected);
        if (mConnected) {
            // What do we want?
        } else {
            // Launch discovery!
            startActivity(new Intent(this, DeviceDiscoveryActivity.class));
        }
        Log.i(TAG, "onCreate(): mConnected is now "+mConnected);
    }
}
