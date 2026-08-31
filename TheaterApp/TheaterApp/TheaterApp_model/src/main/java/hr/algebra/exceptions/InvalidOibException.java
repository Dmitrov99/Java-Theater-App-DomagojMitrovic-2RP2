package hr.algebra.exceptions;

public class InvalidOibException extends RuntimeException {
    public InvalidOibException(String message){
        super(message);
    }
    public InvalidOibException(String message,Throwable cause){
        super(message, cause);
    }

}
