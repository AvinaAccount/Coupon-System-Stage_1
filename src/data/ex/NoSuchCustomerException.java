package data.ex;

/**
 * Exception to throw if there's no such customer in the DB.
 */

public class NoSuchCustomerException extends Exception {

    public NoSuchCustomerException(String msg) {
        super(msg);
    }
}
