package hr.algebra.exceptions;



public class TheaterInputException extends RuntimeException {
    public TheaterInputException(String message) {
        super(message);
    }

    public TheaterInputException(String message, Throwable cause){super(message,cause);}

}
