package unsw.blackout;

import unsw.utils.Angle;

public class DesktopDevice extends Device {
    private static final double DESKTOP_DEVICE_RANGE = 200000;

    public DesktopDevice(String deviceId, Angle position) {
        super(deviceId, position, DESKTOP_DEVICE_RANGE);
    }
}
