package lache.exception;

public class ErrorInputData  extends RuntimeException {
    private int id;

    public ErrorInputData(String message, int id) {
        super(message);
        this.id = id;
    }

    public int getId() {
        return id;
    }
    //400

}
