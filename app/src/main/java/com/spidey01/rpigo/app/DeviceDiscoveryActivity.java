package com.spidey01.rpigo.app;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class DeviceDiscoveryActivity
    extends Activity
    implements DeviceListFragment.OnDeviceSelectedListener
{
    private final static String TAG = "DeviceDiscoveryActivity";

    public final static int FIND_DEVICE = RESULT_FIRST_USER + 1;

    /** Result string used to return the chosen device. */
    public static final String RESULT_STRING = DeviceDiscoveryActivity.class.getCanonicalName()+".RESULT_STRING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate()");

        setContentView(R.layout.activity_device_discovery);

        if (findViewById(R.id.fragment_container) == null) {
            String m = "No fragment_container in view.";
            Log.wtf(TAG, "Oncreate(): "+m);
            // superfurious.
            throw new RuntimeException(m);
        }

        if (savedInstanceState == null) {
            Log.v(TAG, "onCreate(): savedInstanceState == null");
        }

        createDeviceList(R.id.fragment_container);
    }


    /** Populate the container with a {@link DeviceListFragment}.
     *
     * @param container id of the container to populate.
     * @return result of FragmentTransaction.commit().
     */
    private int createDeviceList(int container) {
        FragmentTransaction t = getFragmentManager().beginTransaction();
        DeviceListFragment deviceListFragment = new DeviceListFragment();

        final ArrayList<String> devices = new ArrayList<String>();
        devices.add("rpi0000"); devices.add("rpi0001");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devices);
        deviceListFragment.setListAdapter(adapter);

        t.add(container, deviceListFragment);
        return t.commit();
    }


    @Override
    public void onDeviceSelected(String device) {
        Log.d(TAG, "onDeviceSelected(): " + device);

        Intent intent = new Intent();
        intent.putExtra(RESULT_STRING, device);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device_discovery, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
