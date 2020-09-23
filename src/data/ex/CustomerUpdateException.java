package data.ex;

/**
 * Exception to throw message of problems with updating the customer on the db.
 */

public class CustomerUpdateException extends Exception {

    public CustomerUpdateException(String msg) {
        super(msg);
    }
}
