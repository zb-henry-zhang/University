package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;

import unsw.utils.Angle;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

import java.util.Arrays;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task1Tests {
    @Test
    public void testSatelliteList() {

        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createSatellite("Satellite2", "StandardSatellite", 200 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createSatellite("Satellite3", "StandardSatellite", 300 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1", "Satellite2", "Satellite3"),
                controller.listSatelliteIds());
    }

    @Test
    public void testListAfterDelete() {

        BlackoutController controller = new BlackoutController();

        controller.createDevice("DeviceA", "HandheldDevice", Angle.fromDegrees(30));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(180));
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));

        controller.removeDevice("DeviceB");
        controller.removeDevice("DeviceC");

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceA"), controller.listDeviceIds());
    }
}
