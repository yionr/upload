package cloud.yionr.Exception;

public class NotInTimeException extends Exception {
    private String message;

    public NotInTimeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
