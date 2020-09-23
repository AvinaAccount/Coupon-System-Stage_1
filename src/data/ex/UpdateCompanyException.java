package data.ex;

/**
 * Exception to throw message of problems with updating company on the db.
 */

public class UpdateCompanyException extends Exception {
    public UpdateCompanyException(String msg) {
        super(msg);
    }
}
