package unsw.blackout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public class BlackoutController {
    private List<Entity> entities = new ArrayList<Entity>();
    private List<FileTransfer> transferingFiles = new ArrayList<FileTransfer>();

    public BlackoutController() {

    }

    public void createDevice(String deviceId, String type, Angle position) {

        Device device = null;

        switch (type) {
        case "LaptopDevice":
            device = new LaptopDevice(deviceId, position);
            break;
        case "HandheldDevice":
            device = new HandheldDevice(deviceId, position);
            break;
        case "DesktopDevice":
            device = new DesktopDevice(deviceId, position);
            break;
        default:
        }

        if (device != null) {
            entities.add(device);
        }

    }

    public void removeDevice(String deviceId) {
        entities.removeIf(entity -> entity.getId().equals(deviceId));
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        Satellite satellite = null;

        switch (type) {
        case "StandardSatellite":
            satellite = new StandardSatellite(satelliteId, height, position);
            break;
        case "TeleportingSatellite":
            satellite = new TeleportingSatellite(satelliteId, height, position);
            break;
        case "RelaySatellite":
            satellite = new RelaySatellite(satelliteId, height, position);
            break;
        case "ElephantSatellite":
            satellite = new ElephantSatellite(satelliteId, height, position);
            break;
        default:
        }

        if (satellite != null) {
            entities.add(satellite);
        }

    }

    public void removeSatellite(String satelliteId) {
        entities.removeIf(entity -> entity.getId().equals(satelliteId));
    }

    public List<String> listDeviceIds() {
        return entities.stream().filter(entity -> entity instanceof Device).map(entity -> entity.getId())
                .collect(Collectors.toList());
    }

    public List<String> listSatelliteIds() {
        return entities.stream().filter(entity -> entity instanceof Satellite).map(entity -> entity.getId())
                .collect(Collectors.toList());
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        findEntity(deviceId).addFile(filename, content, true, content.length());
    }

    public EntityInfoResponse getInfo(String id) {
        return findEntity(id).getInfo();
    }

    public void simulate() {
        entities.forEach(Entity::simulate);
        transferingFiles.forEach(FileTransfer::simulate);
        archieveFileTranfers();
    }

    /**
     * Simulate for the specified number of minutes. You shouldn't need to modify
     * this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public List<String> communicableEntitiesInRange(String id) {
        Entity startEntity = findEntity(id);

        MapDFS mapDFS = new MapDFS(entities);
        Set<Entity> communicable = mapDFS.dfs(startEntity);
        List<String> communicableEntities = communicable.stream().filter(entity -> !entity.equals(startEntity))
                .map(entity -> entity.getId()).collect(Collectors.toList());

        return communicableEntities;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {

        Entity sender = findEntity(fromId);
        Entity receiver = findEntity(toId);

        File fileSender = sender.getFile(fileName);
        File fileReceiver = receiver.getFile(fileName);

        if (fileSender == null || !fileSender.isFileComplete()) {
            throw new FileTransferException.VirtualFileNotFoundException(fileName);
        } else if (fileReceiver != null) {
            throw new FileTransferException.VirtualFileAlreadyExistsException(fileName);
        } else if (sender.isUploadFull()) {
            throw new FileTransferException.VirtualFileNoBandwidthException(sender.getId());
        } else if (receiver.isDownloadFull()) {
            throw new FileTransferException.VirtualFileNoBandwidthException(receiver.getId());
        } else if (receiver.isMaxFileReached()) {
            throw new FileTransferException.VirtualFileNoStorageSpaceException("Max Files Reached");
        } else if (receiver.isMaxByteReached(fileSender.getFileSize())) {
            throw new FileTransferException.VirtualFileNoStorageSpaceException("Max Storage Reached");
        }

        FileTransfer fileTransfer = new FileTransfer(fileSender, sender, receiver);

        transferingFiles.add(fileTransfer);

    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // If you are not completing Task 3 you can leave this method blank :)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Entity findEntity(String id) {
        return entities.stream().filter(entity -> entity.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean isEntitiesInRange(String entity1Id, String entity2Id) {
        Entity entity1 = findEntity(entity1Id);
        Entity entity2 = findEntity(entity2Id);
        List<String> commutable = communicableEntitiesInRange(entity1.getId());
        return commutable.contains(entity2.getId());
    }

    public void archieveFileTranfers() {
        transferingFiles.stream()
                .filter(transfer -> !isEntitiesInRange(transfer.getSenderId(), transfer.getReceiverId()))
                .forEach(file -> file.outOfRange());

        transferingFiles.stream().filter(transfer -> isEntitiesInRange(transfer.getSenderId(), transfer.getReceiverId())
                && transfer.isTransferPaused()).forEach(file -> file.resume());

        transferingFiles = transferingFiles.stream().filter(file -> !file.isTransferComplete())
                .collect(Collectors.toList());
    }

}
