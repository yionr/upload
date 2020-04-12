package cloud.yionr.Exception;

public class PermissionDeniedException extends Exception {
    private String message;

    public PermissionDeniedException(String message) {
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
