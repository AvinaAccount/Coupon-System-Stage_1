package data.ex;

/**
 * Exception to throw if there's no such coupon in the DB.
 */

public class NoSuchCouponException extends Exception {
    public NoSuchCouponException(String msg) {
        super(msg);
    }
}
