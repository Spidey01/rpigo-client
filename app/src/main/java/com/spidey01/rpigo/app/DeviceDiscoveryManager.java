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

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;


/** Manages discovering devices.
 *
 * Note that this requires android.permission.INTERNET.
 */
public class DeviceDiscoveryManager
    implements NsdManager.DiscoveryListener, NsdManager.ResolveListener
{
    private static final String TAG = "DeviceDiscoveryManager";

    /** Service type used to find RPIGO servers. */
    protected static final String SERVICE_TYPE = "_raw._tcp";

    private NsdManager mNsdManager;


    public interface OnDeviceDiscoveredListener {
        /** Called when a device is discovered.
         *
         * Note this may be called from a background thread.
         *
         * @param deviceInfo
         */
        void onDeviceDiscovered(DeviceInfo deviceInfo);
    }

    private List<OnDeviceDiscoveredListener> mListeners = new LinkedList<OnDeviceDiscoveredListener>();


    public DeviceDiscoveryManager(Context context) {
        Log.d(TAG, "DeviceDiscoveryManager()");
        mNsdManager = (NsdManager)context.getSystemService(Context.NSD_SERVICE);
        Log.d(TAG, "mNsdManager => " + mNsdManager.toString());
    }


    public void registerListener(OnDeviceDiscoveredListener listener) {
        mListeners.add(listener);
    }


    public void unregisterListener(OnDeviceDiscoveredListener listener) {
        mListeners.remove(listener);
    }


    /** Launch discovery in the background.
     */
    public void startDiscovery() {
        Log.d(TAG, "startDiscovery()");
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, this);
    }


    public void stopDiscovery() {
        Log.d(TAG, "stopDiscovery()");
        mNsdManager.stopServiceDiscovery(this);
    }


    protected void logServiceInfo(NsdServiceInfo serviceInfo) {
        Log.d(TAG, "service name => "+serviceInfo.getServiceName());
        Log.d(TAG, "service type => "+serviceInfo.getServiceType());
        InetAddress host = serviceInfo.getHost();
        Log.d(TAG, "service hostname => "+(host == null ? "(null)" : host.getHostName()));
        Log.d(TAG, "service host ip => "+(host == null ? "(null)" : host.getHostAddress()));
        Log.d(TAG, "service port => "+serviceInfo.getPort());
    }


    /*
     * NsdManager.DiscoveryListener methods.
     */


    @Override
    public void onStartDiscoveryFailed(String serviceType, int errorCode) {
        Log.d(TAG, "onStartDiscoveryFailed():  serviceType=" + serviceType + ", errorCode=" + errorCode);
    }


    @Override
    public void onStopDiscoveryFailed(String serviceType, int errorCode) {
        Log.d(TAG, "onStopDiscoveryFailed(): serviceType="+serviceType+", errorCode="+errorCode);
    }


    @Override
    public void onDiscoveryStarted(String serviceType) {
        Log.d(TAG, "onDiscoveryStarted(): "+serviceType);
    }


    @Override
    public void onDiscoveryStopped(String serviceType) {
        Log.d(TAG, "onDiscoveryStopped(): "+serviceType);
    }


    @Override
    public void onServiceFound(NsdServiceInfo serviceInfo) {
        Log.d(TAG, "onServiceFound(): "+serviceInfo.toString());
        logServiceInfo(serviceInfo);
        mNsdManager.resolveService(serviceInfo, this);
    }


    @Override
    public void onServiceLost(NsdServiceInfo serviceInfo) {
        Log.d(TAG, "onServiceLost(): "+serviceInfo.toString());
    }


    /*
     * NsdManager.ResolveListener methods.
     */


    @Override
    public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
        Log.d(TAG, "onResolveFailed(): serviceInfo=" + serviceInfo.toString() + ", errorCode=" + errorCode);
        logServiceInfo(serviceInfo);
    }


    /** Called when a service is resolved by NsdManager.
     *
     * Note that this can lead to registered {@link }onDeviceDiscovered} instances being called back
     * on a background thread instead of the main UI thread.
     */
    @Override
    public void onServiceResolved(NsdServiceInfo serviceInfo) {
        Log.d(TAG, "onServiceResolved(): serviceInfo="+serviceInfo.toString());
        logServiceInfo(serviceInfo);

        for (OnDeviceDiscoveredListener listener : mListeners) {
            Log.v(TAG, "onServiceResolved(): notifying listener "+listener);
            listener.onDeviceDiscovered(new DeviceInfo(serviceInfo));
        }
    }
}
