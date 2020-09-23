package common.ex;

/**
 * General exception: Exception describing if there's a general problem with the system's functioning.
 */

public class SystemMalFunctionException extends Exception {
    public SystemMalFunctionException(String msg) {
        super(msg);
    }
}
