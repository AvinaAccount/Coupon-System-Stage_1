package data.ex;

/**
 * Exception to throw if there's no coupons to sale.
 */

public class ZeroCouponAmountException extends Exception {
    public ZeroCouponAmountException(String msg) {
        super(msg);
    }
}
