package data.ex;

/**
 * Exception to throw message if coupon already exists.
 */

public class CustomerAlreadyExists extends Exception {
    public CustomerAlreadyExists(String msg) {
        super(msg);
    }
}
