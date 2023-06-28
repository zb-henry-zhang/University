package unsw.blackout;

public class File {
    private String fileName;
    private String data;
    private int fileSize;
    private boolean isFileComplete;
    private boolean isUpLoading;
    private boolean isTransient;

    public File(String fileName, String data, boolean isFileComplete, int fileSize) {
        this.fileName = fileName;
        this.data = data;
        this.fileSize = fileSize;
        this.isFileComplete = isFileComplete;
        this.isUpLoading = false;
        this.isTransient = false;
    }

    public boolean isTransient() {
        return isTransient;
    }

    public void setTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }

    public String getFileName() {
        return fileName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getFileSize() {
        return fileSize;
    }

    public boolean isFileComplete() {
        return isFileComplete;
    }

    public boolean isUpLoading() {
        return isUpLoading;
    }

    public void setUpLoading(boolean isUpLoading) {
        this.isUpLoading = isUpLoading;
    }

    public void setFileComplete(boolean isFileComplete) {
        this.isFileComplete = isFileComplete;
    }

}
