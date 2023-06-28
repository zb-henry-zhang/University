package unsw.blackout;

import unsw.utils.Angle;

public class HandheldDevice extends Device {
    private static final double HANDHELD_DEVICE_RANGE = 50000;

    public HandheldDevice(String deviceId, Angle position) {
        super(deviceId, position, HANDHELD_DEVICE_RANGE);
    }

}
