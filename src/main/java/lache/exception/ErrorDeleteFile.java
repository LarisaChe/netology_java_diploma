package lache.exception;

public class ErrorDeleteFile  extends RuntimeException {
    private int id;

    public ErrorDeleteFile(String message, int id) {
        super(message);
        this.id = id;
    }

    public int getId() {
        return id;
    }
    //500  Error delete file
}
