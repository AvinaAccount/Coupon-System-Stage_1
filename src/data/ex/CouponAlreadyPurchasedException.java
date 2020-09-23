package data.ex;

/**
 * Exception to throw message if coupon already purchased.
 */

public class CouponAlreadyPurchasedException extends Exception {

    public CouponAlreadyPurchasedException(String msg) {
        super(msg);
    }
}
