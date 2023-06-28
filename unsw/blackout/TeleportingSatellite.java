package unsw.blackout;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class TeleportingSatellite extends Satellite {
    private static final double TELEPORT_SPEED = 1000;
    private static final double TELEPORT_RANGE = 200000;

    private static final int TELEPORT_SEND_SPEED = 10;
    private static final int TELEPORT_RECEIVE_SPEED = 15;
    private static final int TELEPORT_MAX_BYTE = 200;

    private boolean teleported;

    public TeleportingSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, position, height, TELEPORT_RANGE, MathsHelper.ANTI_CLOCKWISE, TELEPORT_SPEED,
                TELEPORT_SEND_SPEED, TELEPORT_RECEIVE_SPEED, Integer.MAX_VALUE, TELEPORT_MAX_BYTE);
        this.teleported = false;
    }

    public boolean isTeleported() {
        return teleported;
    }

    public void setTeleported(boolean teleported) {
        this.teleported = teleported;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void nextPosition() {
        Angle current = this.getPosition();
        double currentDegree = current.toDegrees();

        int currentDirection = this.getDirection();

        Angle newAngle = null;

        if (currentDirection == MathsHelper.ANTI_CLOCKWISE) {
            newAngle = current.add(this.getAnglePerMinute());
        } else {
            newAngle = current.subtract(this.getAnglePerMinute());
        }

        double newAngleDegree = newAngle.toDegrees();
        if ((currentDegree <= 180 && newAngleDegree >= 180) || (currentDegree >= 180 && newAngleDegree <= 180)) {
            this.setDirection(currentDirection == MathsHelper.ANTI_CLOCKWISE ? MathsHelper.CLOCKWISE
                    : MathsHelper.ANTI_CLOCKWISE);

            newAngleDegree = 0;
            teleported = true;
        }
        this.setPosition(Angle.fromDegrees((newAngleDegree + 360) % 360));
    }
}
