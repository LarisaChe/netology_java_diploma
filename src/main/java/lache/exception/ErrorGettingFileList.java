package lache.exception;

public class ErrorGettingFileList  extends RuntimeException {
    private int id;

    public ErrorGettingFileList(String message, int id) {
        super(message);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    //500  Error getting file list
}
