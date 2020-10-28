package cn.yionr.Exception;

public class SqlQueryException extends Exception {
    private String message;

    public SqlQueryException(String message) {
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
