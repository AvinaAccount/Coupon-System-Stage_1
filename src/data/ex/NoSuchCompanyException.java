package data.ex;

/**
 * Exception to throw if there's no such company in the DB.
 */

public class NoSuchCompanyException extends Exception {

    public NoSuchCompanyException(String msg){
       super(msg);
    }
}
