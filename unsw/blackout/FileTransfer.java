package unsw.blackout;

public class FileTransfer {
    private File file;

    private Entity sender;
    private Entity receiver;
    private int bytesTransfered;

    public FileTransfer(File file, Entity sender, Entity receiver) {
        this.file = file;
        this.sender = sender;
        this.receiver = receiver;
        startTransfer();
    }

    public String getSenderId() {
        return sender.getId();
    }

    public Entity getSender() {
        return sender;
    }

    public String getReceiverId() {
        return receiver.getId();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void startTransfer() {
        receiver.addFile(file.getFileName(), file.getData().substring(0, bytesTransfered), false, file.getFileSize());
        file.setUpLoading(true);
    }

    public boolean isTransferComplete() {
        File receiverFile = receiver.getFile(file.getFileName());

        if (receiverFile == null) {
            return true;
        }
        return receiverFile.isFileComplete() && !receiverFile.isTransient();
    }

    public boolean isTransferPaused() {
        File receiverFile = receiver.getFile(file.getFileName());

        if (receiverFile == null) {
            return false;
        }

        return receiverFile.isTransient();
    }

    private void finishUpload() {
        receiver.setFileComplete(file.getFileName());
        file.setUpLoading(false);
    }

    private void cancelUpload() {
        receiver.removeFile(file.getFileName());
        file.setUpLoading(false);
    }

    private void pauseUpload(ElephantSatellite elephantSatellite) {
        File receiverFile = receiver.getFile(file.getFileName());
        receiver.setFileComplete(file.getFileName());
        receiverFile.setTransient(true);
        file.setUpLoading(false);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void simulate() {
        if (teleportFiles()) {
            return;
        }

        int senderBandWidth = sender.uploadPerFile();
        int receiverBandWidth = receiver.downloadPerFile();

        System.out.println(Math.min(senderBandWidth, receiverBandWidth));

        addByte(Math.min(senderBandWidth, receiverBandWidth));

        receiver.updateFile(file.getFileName(), file.getData().substring(0, bytesTransfered));

        if (bytesTransfered == file.getFileSize()) {
            finishUpload();
        }

    }

    public void outOfRange() {
        if (receiver instanceof ElephantSatellite) {
            ElephantSatellite elephantSatellite = (ElephantSatellite) receiver;
            pauseUpload(elephantSatellite);
            return;
        } else if (isTransferComplete()) {
            return;
        } else {
            cancelUpload();
        }

    }

    public void resume() {
        File receiverFile = receiver.getFile(file.getFileName());
        receiverFile.setFileComplete(false);
        receiverFile.setTransient(false);
        file.setUpLoading(true);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean teleportFiles() {
        if (sender instanceof TeleportingSatellite) {
            TeleportingSatellite satellite = (TeleportingSatellite) sender;
            if (satellite.isTeleported()) {
                teleportFile();
                satellite.setTeleported(false);
                return true;
            }
        }

        if (receiver instanceof TeleportingSatellite) {
            TeleportingSatellite satellite = (TeleportingSatellite) receiver;
            if (satellite.isTeleported() && sender instanceof Device) {
                teleportDeviceFile();
                satellite.setTeleported(false);
                return true;
            } else if (satellite.isTeleported()) {
                teleportFile();
                satellite.setTeleported(false);
                return true;
            }
        }

        return false;
    }

    private void teleportFile() {
        String modifiedSubstring = file.getData().substring(bytesTransfered).replace("t", "");
        receiver.updateFile(file.getFileName(), file.getData().substring(0, bytesTransfered) + modifiedSubstring);

        finishUpload();
    }

    private void teleportDeviceFile() {
        sender.updateFile(file.getFileName(), file.getData().replace("t", ""));
        cancelUpload();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void addByte(int newByte) {
        this.bytesTransfered = this.bytesTransfered + newByte;
        if (bytesTransfered > file.getFileSize()) {
            this.bytesTransfered = file.getFileSize();
        }
    }

}
