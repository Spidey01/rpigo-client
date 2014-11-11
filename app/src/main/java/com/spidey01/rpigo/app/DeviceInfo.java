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

import android.net.nsd.NsdServiceInfo;

import java.net.InetAddress;


/** Provides info on a device running an RPIGO server.
 */
public class DeviceInfo {
    private static final String TAG = "DeviceInfo";

    private final NsdServiceInfo mServiceInfo;

    DeviceInfo(NsdServiceInfo serviceInfo) {
        mServiceInfo = serviceInfo;
    }


    public int getPort() {
        return mServiceInfo.getPort();
    }


    public String getHostname() {
        return mServiceInfo.getHost().getHostName();
    }


    public InetAddress getAddress() {
        return mServiceInfo.getHost();
    }


    public String getIP() {
        return mServiceInfo.getHost().getHostAddress();
    }
}
