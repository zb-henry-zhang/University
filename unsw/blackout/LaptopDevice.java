package unsw.blackout;

import unsw.utils.Angle;

public class LaptopDevice extends Device {
    private static final double LAPTOP_DEVICE_RANGE = 100000;

    public LaptopDevice(String deviceId, Angle position) {
        super(deviceId, position, LAPTOP_DEVICE_RANGE);
    }
}
