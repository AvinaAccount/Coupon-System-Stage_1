package data.ex;

/**
 * Exception to throw message if one or more details is incorrect in the login user.
 */

public class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) {
        super(message);
    }
}
