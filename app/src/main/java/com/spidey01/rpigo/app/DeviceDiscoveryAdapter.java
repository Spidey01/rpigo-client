/*
 * Copyright 2014 Terry Mathew Poulin <BigBoss1964@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spidey01.rpigo.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


/** Adapter for use with {@link com.spidey01.rpigo.app.DeviceDiscoveryManager}.
 */
public class DeviceDiscoveryAdapter
    extends ArrayAdapter<DeviceInfo>
    implements DeviceDiscoveryManager.OnDeviceDiscoveredListener
{
    private static final String TAG = "DeviceDiscoveryAdapter";


    @Override
    public void onDeviceDiscovered(final DeviceInfo deviceInfo) {
        Log.d(TAG, "onDeviceDiscovered(): ");

        /*
         * This let's use ensure we update the view on the main UI thread.
         * In terms of correctness, I think the correct solution would be to use
         * {@link android.os.Handler} to post a message but eh, this works for testing now.
         */
        Activity a = (Activity)getContext();
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                add(deviceInfo);
            }
        });
    }


    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceInfo deviceInfo = getItem(position);

        if (convertView == null) {
            // We're making a new view for this magic. Set it up.
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list_item, parent, false);
        }

        TextView deviceName = (TextView)convertView.findViewById(R.id.device_name);
        deviceName.setText(deviceInfo.getHostname());

        TextView ipAddress = (TextView)convertView.findViewById(R.id.ip_address);
        ipAddress.setText(deviceInfo.getIP());

        Log.v(TAG, "getView(): returning layout describing "
                + deviceInfo.getHostname()
                + "@"
                + deviceInfo.getIP());
        return convertView;
    }


    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public DeviceDiscoveryAdapter(Context context, int resource) {
        super(context, resource);
    }


    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     */
    public DeviceDiscoveryAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }


    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public DeviceDiscoveryAdapter(Context context, int resource, DeviceInfo[] objects) {
        super(context, resource, objects);
    }


    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects            The objects to represent in the ListView.
     */
    public DeviceDiscoveryAdapter(Context context, int resource, int textViewResourceId, DeviceInfo[] objects) {
        super(context, resource, textViewResourceId, objects);
    }


    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public DeviceDiscoveryAdapter(Context context, int resource, List<DeviceInfo> objects) {
        super(context, resource, objects);
    }


    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects            The objects to represent in the ListView.
     */
    public DeviceDiscoveryAdapter(Context context, int resource, int textViewResourceId, List<DeviceInfo> objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
