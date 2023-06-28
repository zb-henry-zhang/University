package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2SatelliteMovement {
    @Test
    public void testRelayMovement() {
        BlackoutController controller = new BlackoutController();

        // If the staring position of the satellite is greater or equal to 345, it
        // travels in positive direction
        controller.createSatellite("Satellite1", "RelaySatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(344));
        controller.createSatellite("Satellite2", "RelaySatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(345));

        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(342.77), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));

        assertEquals(new EntityInfoResponse("Satellite2", Angle.fromDegrees(346.2275737993976), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite2"));

    }

    @Test
    public void testTeleportingChangeDirection() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(181));

        // ensure it moves anti-clock wise
        controller.simulate();
        Angle clockwiseOnFirstMovement = controller.getInfo("Satellite1").getPosition();
        controller.simulate();
        Angle clockwiseOnSecondMovement = controller.getInfo("Satellite1").getPosition();
        assertTrue(clockwiseOnSecondMovement.compareTo(clockwiseOnFirstMovement) == 1);

        // Since it started on 181, it have to loop around once to commence first
        // teleport
        controller.simulate(499);

        // Verify that Satellite1 is now at theta=0
        assertTrue(controller.getInfo("Satellite1").getPosition().toDegrees() % 360 == 0);

        controller.simulate();

        // Change direction on the first teleport
        Angle changeDirectionOnFirst = controller.getInfo("Satellite1").getPosition();

        controller.simulate();

        Angle changeDirectionOnSecond = controller.getInfo("Satellite1").getPosition();
        assertTrue(changeDirectionOnSecond.compareTo(changeDirectionOnFirst) == -1);

        // Second teleport
        controller.simulate(250);

        controller.simulate();

        // Changes direction on second teleport
        Angle changeDirectionAgainOnFirst = controller.getInfo("Satellite1").getPosition();

        controller.simulate();

        Angle changeDirectionAgainOnSecond = controller.getInfo("Satellite1").getPosition();
        assertTrue(changeDirectionAgainOnSecond.compareTo(changeDirectionAgainOnFirst) == 1);

    }
}
