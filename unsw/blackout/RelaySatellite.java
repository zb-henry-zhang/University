package unsw.blackout;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class RelaySatellite extends Satellite {
    private static final double RELAY_SPEED = 1500;
    private static final double RELAY_RANGE = 300000;

    public RelaySatellite(String satelliteId, double height, Angle position) {

        super(satelliteId, position, height, RELAY_RANGE, MathsHelper.CLOCKWISE, RELAY_SPEED, 0, 0, 0, 0);

        double angleDegree = position.toDegrees();

        calculateInitialDirection(angleDegree);

    }

    private void calculateInitialDirection(double angleDegree) {
        if (angleDegree >= 345 || angleDegree < 140) {
            this.setDirection(MathsHelper.ANTI_CLOCKWISE);
        }
    }

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

        if ((currentDegree <= 190 && newAngleDegree >= 190) || (currentDegree >= 140 && newAngleDegree <= 140)) {
            this.setDirection(currentDirection == MathsHelper.ANTI_CLOCKWISE ? MathsHelper.CLOCKWISE
                    : MathsHelper.ANTI_CLOCKWISE);
        }
        this.setPosition(Angle.fromDegrees((newAngleDegree + 360) % 360));
    }

}
