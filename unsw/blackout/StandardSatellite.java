package unsw.blackout;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class StandardSatellite extends Satellite {
    private static final double STANDARD_SPEED = 2500;
    private static final double STANDARD_RANGE = 150000;

    private static final int STANDARD_TRANSFER_SPEED = 1;
    private static final int STANDARD_MAX_FILE = 3;
    private static final int STANDARD_MAX_BYTE = 80;

    public StandardSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, position, height, STANDARD_RANGE, MathsHelper.CLOCKWISE, STANDARD_SPEED,
                STANDARD_TRANSFER_SPEED, STANDARD_TRANSFER_SPEED, STANDARD_MAX_FILE, STANDARD_MAX_BYTE);
        super.addUnsupportedDevice("DesktopDevice");
    }

}
