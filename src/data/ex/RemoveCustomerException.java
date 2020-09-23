package data.ex;

/**
 * Exception to throw message of problems with removing the customer from the db.
 */

public class RemoveCustomerException extends Throwable {
    public RemoveCustomerException(String msg) {
        super(msg);
    }
}
