package com.kt.iot.mobile.utils;

import com.kt.gigaiot_sdk.data.Device;

/**
 * Created by ceoko on 15. 6. 3..
 */
public class ModifyDeviceMgr {

    public static Device modifyDevice;

    public static Device getModifyDevice() {
        return modifyDevice;
    }

    public static void setModifyDevice(Device modifyDevice) {
        ModifyDeviceMgr.modifyDevice = modifyDevice;
    }
}
