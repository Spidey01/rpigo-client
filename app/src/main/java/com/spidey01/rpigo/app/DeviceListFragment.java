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
 */
public class DeviceListFragment
    extends ListFragment
{
    private static final String TAG = "DeviceListFragment";

    private OnDeviceSelectedListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeviceListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // TODO: Change Adapter to display your content
        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS));
    }


    /** Called when this fragment has been associated with an Activity.
     *
     * @throws java.lang.ClassCastException if activity does not implement {@link com.spidey01.rpigo.app.DeviceListFragment.OnDeviceSelectedListener}.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(TAG, "onAttach()");
        try {
            mListener = (OnDeviceSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnDeviceSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_list_fragment, container, false);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG, "onDetach()");
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
       super.onListItemClick(l, v, position, id);
        Log.v(TAG, "onListItemClick()");

        assert mListener != null : TAG+".onListItemClick() no listener set.";
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onDeviceSelected(DummyContent.ITEMS.get(position).id);
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
