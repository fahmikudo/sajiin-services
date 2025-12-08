package id.sajiin.sajiinservices.shared.exception;

public class GeneralException extends RuntimeException {
    public GeneralException(String message) {
        super(message);
    }
    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }
    public GeneralException(Throwable cause) {
        super(cause);
    }
}
