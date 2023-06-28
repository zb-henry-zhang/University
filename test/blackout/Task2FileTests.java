package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2FileTests {
    private static final double HANDHELD_RANGE = 50000;

    @Test
    public void testOutRange() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "StandardSatellite", HANDHELD_RANGE + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "I LOVE COMP";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        controller.simulate(2);

        assertEquals(null, controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

    }

    @Test
    public void testOutRangeComplete() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        controller.simulate(250);

        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

    }

    @Test
    public void testTeleportMessage() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(0));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(180));

        // It should take 252 simulations to reach theta = 180.
        // Simulate until Satellite1 reaches theta=180
        controller.simulate(251);

        String msg = "tom took ten tasty tomatoes";

        // Verify that Satellite1 is now at theta=0

        controller.addFileToDevice("DeviceC", "FileAlpha", msg);

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));

        controller.simulate();

        assertEquals(new FileInfoResponse("FileAlpha", "om ook en asy omaoes", msg.length(), true),
                controller.getInfo("DeviceC").getFiles().get("FileAlpha"));
    }

    @Test
    public void testReceiveTeleportMessage() {
        BlackoutController controller = new BlackoutController();

        String msg = "tom took ten tasty tomatoes today";

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(0));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(180));
        controller.createDevice("DeviceD", "HandheldDevice", Angle.fromDegrees(0));

        // Behaves like a standard satellit when not moving
        controller.addFileToDevice("DeviceD", "FileAlpha", msg);

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceD", "Satellite1"));

        // It should take 252 simulations to reach theta = 180.
        // Simulate until Satellite1 reaches theta=180
        controller.simulate(250);

        assertEquals(new FileInfoResponse("FileAlpha", "tom took ten tasty tomatoes today", msg.length(), true),
                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceC"));

        // Teleports
        controller.simulate(2);

        System.out.println(controller.getInfo("DeviceC").getFiles().get("FileAlpha"));

        assertEquals(new FileInfoResponse("FileAlpha", "tom took ten asy omaoes oday", msg.length(), true),
                controller.getInfo("DeviceC").getFiles().get("FileAlpha"));
    }

    @Test
    public void testFileTeleportTwice() {
        BlackoutController controller = new BlackoutController();

        String msg = "tom took ten tasty tomatoes";

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(0));

        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(180));

        controller.simulate(251);

        controller.addFileToDevice("DeviceC", "FileAlpha", msg);

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));

        controller.simulate();

        assertEquals(new FileInfoResponse("FileAlpha", "om ook en asy omaoes", msg.length(), true),
                controller.getInfo("DeviceC").getFiles().get("FileAlpha"));

        // Travel to 180 to teleport for the second time
        controller.simulate(251);

        controller.addFileToDevice("DeviceC", "FileBeta", msg);

        assertDoesNotThrow(() -> controller.sendFile("FileBeta", "DeviceC", "Satellite1"));

        controller.simulate();

        assertEquals(new FileInfoResponse("FileBeta", "om ook en asy omaoes", msg.length(), true),
                controller.getInfo("DeviceC").getFiles().get("FileBeta"));

    }

    @Test
    public void testBandWidthChange() {
        BlackoutController controller = new BlackoutController();

        String msg = "abcdeabcdeabcdeabcdeabcdeabcde";

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(0));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(0));

        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        controller.addFileToDevice("DeviceC", "FileBeta", msg);
        controller.addFileToDevice("DeviceC", "FileTheta", msg);

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));

        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        controller.simulate();

        assertEquals(new FileInfoResponse("FileAlpha", "abcdeabcdeabcde", msg.length(), false),
                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        assertDoesNotThrow(() -> controller.sendFile("FileBeta", "DeviceC", "Satellite1"));

        controller.simulate();

        assertEquals(new FileInfoResponse("FileAlpha", "abcdeabcdeabcdeabcdeab", msg.length(), false),
                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        assertEquals(new FileInfoResponse("FileBeta", "abcdeab", msg.length(), false),
                controller.getInfo("Satellite1").getFiles().get("FileBeta"));

        assertDoesNotThrow(() -> controller.sendFile("FileTheta", "DeviceC", "Satellite1"));

        controller.simulate();

        assertEquals(new FileInfoResponse("FileAlpha", "abcdeabcdeabcdeabcdeabcdeab", msg.length(), false),
                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        assertEquals(new FileInfoResponse("FileBeta", "abcdeabcdeab", msg.length(), false),
                controller.getInfo("Satellite1").getFiles().get("FileBeta"));

        assertEquals(new FileInfoResponse("FileTheta", "abcde", msg.length(), false),
                controller.getInfo("Satellite1").getFiles().get("FileTheta"));

    }
}
