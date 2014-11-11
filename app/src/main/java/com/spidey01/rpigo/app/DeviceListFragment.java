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
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.util.Log;


import com.spidey01.rpigo.app.dummy.DummyContent;

/**
 * A fragment representing a list of Raspberry Pi's or more precisely: RPIGO servers.
 *
 * Activities containing this fragment MUST implement the {@link OnDeviceSelectedListener}
 * interface if they wish to listen for device selections.
 *
 * android.permission.INTERNET is also required for network access.
 */
public class DeviceListFragment
    extends ListFragment
{
    private static final String TAG = "DeviceListFragment";

    private DeviceDiscoveryManager mSearcher;

    private OnDeviceSelectedListener mListener;

    /** Cache our ListAdapter. */
    private DeviceDiscoveryAdapter mAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeviceListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(): savedInstanceState="+savedInstanceState);

        // TODO: Change Adapter to display your content
        // setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
        //         android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS));

        /* XXX
         *      Remember: onCreate() is called after onAttach(). So mAdapter=null in onAttach()!
         */
        Log.d(TAG, "onCreate(): configurize our ListAdapter.");
        mAdapter = new DeviceDiscoveryAdapter(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
        setListAdapter(mAdapter);

        Log.d(TAG, "onCreate(): Setting up discovery manager.");
        mSearcher = new DeviceDiscoveryManager(getActivity());
    }


    /** Called when this fragment has been associated with an Activity.
     *
     * The hosting Activity is expected to implement our listener interface to get the selected DeviceInfo.
     *
     * @throws java.lang.ClassCastException if activity does not implement {@link com.spidey01.rpigo.app.DeviceListFragment.OnDeviceSelectedListener}.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(TAG, "onAttach()");

        Log.d(TAG, "Setting our activity as our listener");
        try {
            mListener = (OnDeviceSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnDeviceSelectedListener");
        }
    }


    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link android.app.Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");

        /*
         * Here lies  the magic behind populating this list.
         */
        mSearcher.registerListener(mAdapter);
        mSearcher.startDiscovery();

        super.onResume();
    }


    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link android.app.Activity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");

        mSearcher.stopDiscovery();
        mSearcher.unregisterListener(mAdapter);

        /*
         *  Clear the adapter's data after pausing because when we resume, DeviceDiscoveryManager will
         *  start from the beginning again.
         *
         *  TODO: test if that's true or if NsdManager won't report previously found services a second time
         *        when doing code like this:
         *
         *              mNsdManager.discoverServices(...);
         *              mNsdManager.stopDiscovery(...);
         *              mNsdManager.discoverServices(...);
         *
         *        I assume it starts fresh.
         *
         */
        mAdapter.clear();

        super.onPause();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG, "onDetach()");
        mSearcher = null;
        mListener = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_list_fragment, container, false);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
       super.onListItemClick(l, v, position, id);
        Log.v(TAG, "onListItemClick()");

        assert mListener != null : TAG+".onListItemClick() no listener set.";
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.

            //mListener.onDeviceSelected(DummyContent.ITEMS.get(position).id);
            DeviceInfo deviceInfo = (DeviceInfo)getListView().getItemAtPosition(position);
            String debug = deviceInfo.getHostname() + "@" + deviceInfo.getIP() + ":" + deviceInfo.getPort();
            mListener.onDeviceSelected(debug);
        }
    }


    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnDeviceSelectedListener {
        // TODO: Update argument type and name
        public void onDeviceSelected(String id);
    }

}
