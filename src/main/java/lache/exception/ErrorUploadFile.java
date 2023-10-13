package lache.exception;

public class ErrorUploadFile  extends RuntimeException {
    private int id;

    public ErrorUploadFile(String message, int id) {
        super(message);
        this.id = id;
    }

    public int getId() {
        return id;
    }
     //500  Error upload file
}
