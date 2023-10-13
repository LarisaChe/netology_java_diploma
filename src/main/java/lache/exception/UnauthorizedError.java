package lache.exception;

public class UnauthorizedError  extends RuntimeException {
    private int id;

    public UnauthorizedError(String message, int id) {
        super(message);
        this.id = id;
    }

    public int getId() {
        return id;
    }
    //401 Unauthorized
}
