package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import static blackout.TestHelpers.createString;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2SendFileException {
    @Test
    public void testVirtualFileNotFoundException() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertThrows(FileTransferException.VirtualFileNotFoundException.class,
                () -> controller.sendFile("NonExistentFile", "DeviceC", "Satellite1"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));

        assertThrows(FileTransferException.VirtualFileNotFoundException.class,
                () -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));

    }

    @Test
    public void testVirtualFileAlreadyExistsException() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";

        controller.addFileToDevice("DeviceC", "FileAlpha", msg);

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));

        controller.simulate(msg.length());

        assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class,
                () -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));

        assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class,
                () -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));

    }

    @Test
    public void testVirtualFileNoBandwidthException() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";

        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        controller.addFileToDevice("DeviceC", "FileBeta", msg);
        controller.addFileToDevice("DeviceC", "FileTheta", msg);

        // Standard satellite receiving more than its bandwidth

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));

        controller.simulate(msg.length());

        assertDoesNotThrow(() -> controller.sendFile("FileBeta", "DeviceC", "Satellite1"));

        assertThrows(FileTransferException.VirtualFileNoBandwidthException.class,
                () -> controller.sendFile("FileTheta", "DeviceC", "Satellite1"));

        controller.simulate(msg.length());

        // Standard satellite sending more that its band width
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));

        assertThrows(FileTransferException.VirtualFileNoBandwidthException.class,
                () -> controller.sendFile("FileBeta", "Satellite1", "DeviceB"));

    }

    @Test
    public void testVirtualFileNoStorageSpaceException() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "TeleportingSatellite", 6000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";

        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        controller.addFileToDevice("DeviceC", "FileBeta", msg);
        controller.addFileToDevice("DeviceC", "FileTheta", msg);
        controller.addFileToDevice("DeviceC", "FileSigma", msg);

        // Standard satellite receiving more than its bandwidth

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));

        controller.simulate(msg.length());

        assertDoesNotThrow(() -> controller.sendFile("FileBeta", "DeviceC", "Satellite1"));

        controller.simulate(msg.length());

        assertDoesNotThrow(() -> controller.sendFile("FileTheta", "DeviceC", "Satellite1"));

        controller.simulate(msg.length());

        assertThrows(FileTransferException.VirtualFileNoStorageSpaceException.class,
                () -> controller.sendFile("FileSigma", "DeviceC", "Satellite1"));

        controller.addFileToDevice("DeviceC", "LongString", createString(201));

        assertThrows(FileTransferException.VirtualFileNoStorageSpaceException.class,
                () -> controller.sendFile("LongString", "DeviceC", "Satellite2"));
    }
}
