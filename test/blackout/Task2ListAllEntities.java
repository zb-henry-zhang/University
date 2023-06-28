package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.utils.Angle;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2ListAllEntities {
    private static final double RELAY_RANGE = 300000;
    private static final double STANDARD_RANGE = 150000;
    private static final double TELEPORT_RANGE = 200000;
    private static final double ELEPHANT_RANGE = 400000;

    private static final double HANDHELD_RANGE = 50000;
    private static final double LAPTOP_RANGE = 100000;
    private static final double DESKTOP_RANGE = 200000;

    @Test
    public void testRangeStandard() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "StandardSatellite", STANDARD_RANGE + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "StandardSatellite", STANDARD_RANGE + 1 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));

        controller.createDevice("Device1", "LaptopDevice", Angle.fromDegrees(320));
        controller.createDevice("Device2", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("Device3", "DesktopDevice", Angle.fromDegrees(320));

        assertListAreEqualIgnoringOrder(Arrays.asList("Device1", "Satellite2", "Device2"),
                controller.communicableEntitiesInRange("Satellite1"));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1"),
                controller.communicableEntitiesInRange("Satellite2"));

    }

    @Test
    public void testRangeRelay() {

        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "RelaySatellite", RELAY_RANGE + 1 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));

        controller.createDevice("Device1", "LaptopDevice", Angle.fromDegrees(320));
        controller.createDevice("Device2", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("Device3", "DesktopDevice", Angle.fromDegrees(320));

        assertListAreEqualIgnoringOrder(Arrays.asList(), controller.communicableEntitiesInRange("Satellite1"));

    }

    @Test
    public void testRangeTeleport() {

        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", TELEPORT_RANGE + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "TeleportingSatellite", TELEPORT_RANGE + 1 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));

        controller.createDevice("Device1", "LaptopDevice", Angle.fromDegrees(320));
        controller.createDevice("Device2", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("Device3", "DesktopDevice", Angle.fromDegrees(320));

        assertListAreEqualIgnoringOrder(Arrays.asList("Device1", "Satellite2", "Device2", "Device3"),
                controller.communicableEntitiesInRange("Satellite1"));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1"),
                controller.communicableEntitiesInRange("Satellite2"));

    }

    @Test
    public void testRangeElephant() {

        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "ElephantSatellite", ELEPHANT_RANGE + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "ElephantSatellite", ELEPHANT_RANGE + 1 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));

        controller.createDevice("Device1", "LaptopDevice", Angle.fromDegrees(320));
        controller.createDevice("Device2", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("Device3", "DesktopDevice", Angle.fromDegrees(320));

        assertListAreEqualIgnoringOrder(Arrays.asList("Device1", "Satellite2", "Device2"),
                controller.communicableEntitiesInRange("Satellite1"));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1"),
                controller.communicableEntitiesInRange("Satellite2"));

    }

    @Test
    public void testRangeDevices() {

        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", HANDHELD_RANGE + 1 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "TeleportingSatellite", LAPTOP_RANGE + 1 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));
        controller.createSatellite("Satellite3", "TeleportingSatellite", DESKTOP_RANGE + 1 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));

        controller.createDevice("Device1", "LaptopDevice", Angle.fromDegrees(320));
        controller.createDevice("Device2", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("Device3", "DesktopDevice", Angle.fromDegrees(320));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1"), controller.communicableEntitiesInRange("Device1"));

        assertListAreEqualIgnoringOrder(Arrays.asList(), controller.communicableEntitiesInRange("Device2"));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1", "Satellite2"),
                controller.communicableEntitiesInRange("Device3"));

    }

    @Test
    public void testRangeViaRelay() {

        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite",
                RELAY_RANGE + TELEPORT_RANGE + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Relay1", "RelaySatellite", RELAY_RANGE + RADIUS_OF_JUPITER, Angle.fromDegrees(320));

        controller.createDevice("Device1", "LaptopDevice", Angle.fromDegrees(320));

        assertListAreEqualIgnoringOrder(Arrays.asList("Relay1", "Device1"),
                controller.communicableEntitiesInRange("Satellite1"));

    }

    @Test
    public void testRangeViaTwoRelay() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite",
                RELAY_RANGE * 2 + TELEPORT_RANGE + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Relay2", "RelaySatellite", RELAY_RANGE * 2 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));
        controller.createSatellite("Relay1", "RelaySatellite", RELAY_RANGE + RADIUS_OF_JUPITER, Angle.fromDegrees(320));

        controller.createDevice("Device1", "LaptopDevice", Angle.fromDegrees(320));

        assertListAreEqualIgnoringOrder(Arrays.asList("Relay1", "Device1", "Relay2"),
                controller.communicableEntitiesInRange("Satellite1"));

    }

    @Test
    public void testRangeViaRelayWithRequirement() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "StandardSatellite",
                STANDARD_RANGE + TELEPORT_RANGE + RADIUS_OF_JUPITER, Angle.fromDegrees(320));

        controller.createSatellite("Relay1", "RelaySatellite", RELAY_RANGE + RADIUS_OF_JUPITER, Angle.fromDegrees(320));

        controller.createDevice("Device1", "DesktopDevice", Angle.fromDegrees(320));

        assertListAreEqualIgnoringOrder(Arrays.asList("Relay1"), controller.communicableEntitiesInRange("Satellite1"));

    }
}
