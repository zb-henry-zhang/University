package unsw.blackout;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public abstract class Device extends Entity {
    private static final double JUPITER_RADIUS = 69911;

    public Device(String deviceId, Angle position, double maxRange) {
        super(deviceId, position, JUPITER_RADIUS, maxRange);
    }

    public boolean inRange(Entity entity) {
        if (!isSupportedType(entity))
            return false;

        Satellite satellite = (Satellite) entity;

        if (!super.isEntityWithinRange(satellite)) {
            return false;
        } else if (!MathsHelper.isVisible(satellite.getHeight(), satellite.getPosition(), this.getPosition())) {
            return false;
        }

        return true;
    }

    public boolean isSupportedType(Entity entity) {
        if (!(entity instanceof Satellite))
            return false;

        Satellite satellite = (Satellite) entity;

        if (satellite.getUnsupportedDevices().contains(this.getType())) {
            return false;
        }

        return true;
    }

    public void simulate() {

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int uploadPerFile() {
        return Integer.MAX_VALUE;
    }

    public int downloadPerFile() {
        return Integer.MAX_VALUE;
    }

    public boolean isMaxFileReached() {
        return false;
    }

    public boolean isMaxByteReached(int newFileSize) {
        return false;
    }

    public boolean isUploadFull() {
        return false;
    }

    public boolean isDownloadFull() {
        return false;
    }

}
