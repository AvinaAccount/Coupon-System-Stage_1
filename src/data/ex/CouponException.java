package data.ex;

/**
 * Exception to throw message of general problem with coupons.
 */

public class CouponException extends Exception {
    public CouponException(String msg) {
        super(msg);
    }
}
