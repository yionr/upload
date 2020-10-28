package cn.yionr.Exception;

public class FileAlreadyExsitsException extends Exception {
    private String message;

    public FileAlreadyExsitsException(String message) {
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
