package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public abstract class Satellite extends Entity implements Move {
    private List<String> unsupportedDevices = new ArrayList<String>();

    private int sendSpeed;
    private int receiveSpeed;

    private int maxFileNum;
    private int maxByte;

    private int direction;
    private double linearSpeed;
    private double angularSpeed;

    public Satellite(String satelliteId, Angle position, double height, double maxRange, int direction,
            double linearSpeed, int sendSpeed, int receiveSpeed, int maxFileNum, int maxByte) {
        super(satelliteId, position, height, maxRange);
        this.sendSpeed = sendSpeed;
        this.receiveSpeed = receiveSpeed;
        this.maxFileNum = maxFileNum;
        this.maxByte = maxByte;
        this.direction = direction;
        this.linearSpeed = linearSpeed;
        this.angularSpeed = linearSpeed / height;
    }

    public Angle getAnglePerMinute() {
        return Angle.fromRadians(angularSpeed);
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public double getLinearSpeed() {
        return linearSpeed;
    }

    public List<String> getUnsupportedDevices() {
        return unsupportedDevices;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void addUnsupportedDevice(String device) {
        unsupportedDevices.add(device);
    }

    public boolean inRange(Entity entity) {
        if (!super.isEntityWithinRange(entity)) {
            return false;
        }
        if (entity instanceof Satellite) {
            if (!MathsHelper.isVisible(this.getHeight(), this.getPosition(), entity.getHeight(),
                    entity.getPosition())) {
                return false;
            }
        } else {
            if (!MathsHelper.isVisible(this.getHeight(), this.getPosition(), entity.getPosition())) {
                return false;
            }

            if (!isSupportedType(entity))
                return false;
        }

        return true;
    }

    public boolean isSupportedType(Entity entity) {
        if (this.getUnsupportedDevices().contains(entity.getType())) {
            return false;
        }

        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int uploadPerFile() {
        if (numFileUploading() == 0) {
            return 0;
        }
        return sendSpeed / numFileUploading();
    }

    public int downloadPerFile() {
        if (numFileDownloading() == 0) {
            return 0;
        }
        return receiveSpeed / numFileDownloading();
    }

    public boolean isUploadFull() {
        return sendSpeed / (numFileUploading() + 1) < 1;
    }

    public boolean isDownloadFull() {
        return sendSpeed / (numFileDownloading() + 1) < 1;
    }

    public boolean isMaxFileReached() {
        return (numFiles() + 1) > maxFileNum;
    }

    public boolean isMaxByteReached(int newFileSize) {
        List<File> files = getFiles();
        return files.stream().mapToInt(File::getFileSize).sum() + newFileSize > maxByte;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void simulate() {
        nextPosition();
    }

    public void nextPosition() {
        Angle current = this.getPosition();
        Angle newAngle = current.subtract(this.getAnglePerMinute());
        double newAngleDegree = newAngle.toDegrees();

        this.setPosition(Angle.fromDegrees((newAngleDegree + 360) % 360));
    }
}
