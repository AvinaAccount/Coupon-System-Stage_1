package data.ex;

/**
 * Exception to throw message if Unable to return coupons.
 */

public class ReturnCouponsException extends Exception {
    public ReturnCouponsException(String msg) {
        super(msg);
    }
}
