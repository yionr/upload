package cn.yionr.Exception;

public class IdNotMatchException extends Exception {
    private String message;

    public IdNotMatchException(String message) {
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
