package unsw.blackout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public abstract class Entity {
    private String id;
    private Angle position;
    private double height;
    private double maxRange;

    private String type;

    private List<File> files;

    public Entity(String id, Angle position, double height, double maxRange) {
        this.id = id;
        this.position = position;
        this.height = height;
        this.files = new ArrayList<File>();
        this.maxRange = maxRange;
        this.type = this.getClass().getSimpleName();

    }

    public Angle getPosition() {
        return position;
    }

    public void setPosition(Angle position) {
        this.position = position;
    }

    public double getHeight() {
        return height;
    }

    public String getId() {
        return id;
    }

    public List<File> getFiles() {
        return files;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public void setFileUpload(String filename, boolean isUpLoading) {
        getFile(filename).setUpLoading(isUpLoading);
    }

    public void setFileComplete(String filename) {
        getFile(filename).setFileComplete(true);
    }

    public String getType() {
        return type;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public File getFile(String fileName) {
        return files.stream().filter(file -> file.getFileName() == fileName).findFirst().orElse(null);
    }

    public int numFiles() {
        return (int) getFiles().stream().count();
    }

    public void addFile(String filename, String content, boolean isFileComplete, int fileSize) {
        File file = new File(filename, content, isFileComplete, fileSize);
        files.add(file);
    }

    public void updateFile(String filename, String content) {
        getFile(filename).setData(content);
    }

    public void removeFile(String filename) {
        setFileComplete(filename);
        files.removeIf(file -> file.getFileName().equals(filename));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public EntityInfoResponse getInfo() {
        Map<String, FileInfoResponse> returnFile = files.stream()
                .collect(Collectors.toMap(File::getFileName, file -> new FileInfoResponse(file.getFileName(),
                        file.getData(), file.getFileSize(), file.isFileComplete())));
        System.err.println(returnFile);
        System.err.println(this);
        return new EntityInfoResponse(id, position, height, type, returnFile);
    }

    public abstract void simulate();

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean isEntityWithinRange(Entity entity) {
        return distance(this, entity) <= this.maxRange;
    }

    private double distance(Entity entity1, Entity entity2) {
        if (entity1 instanceof Satellite && entity2 instanceof Satellite) {
            return MathsHelper.getDistance(entity1.getHeight(), entity1.getPosition(), entity2.getHeight(),
                    entity2.getPosition());
        } else if (entity1 instanceof Satellite ^ entity2 instanceof Satellite) {
            if (entity1 instanceof Satellite) {
                return MathsHelper.getDistance(entity1.getHeight(), entity1.getPosition(), entity2.getPosition());
            } else {
                return MathsHelper.getDistance(entity2.getHeight(), entity2.getPosition(), entity1.getPosition());
            }
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    public abstract boolean isSupportedType(Entity entity);

    public abstract boolean inRange(Entity entity);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int numFileUploading() {
        return (int) files.stream().filter(File::isUpLoading).count();
    }

    public int numFileDownloading() {
        return (int) files.stream().filter(file -> !file.isFileComplete()).count();
    }

    public abstract int uploadPerFile();

    public abstract int downloadPerFile();

    public abstract boolean isMaxFileReached();

    public abstract boolean isMaxByteReached(int newFileSize);

    public abstract boolean isUploadFull();

    public abstract boolean isDownloadFull();
}
