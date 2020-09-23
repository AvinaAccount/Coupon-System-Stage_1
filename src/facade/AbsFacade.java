package facade;


import common.LoginType;
import common.ex.SystemMalFunctionException;
import data.ex.InvalidLoginException;

import java.sql.SQLException;


public abstract class AbsFacade {

    /**
     * A helper method for logging a user into the system by getting its relevant facade class.
     *
     * @param email     To identify the user.
     * @param password  To identify the user.
     * @param loginType To identify the user type.
     * @return 'AbsFacade' object
     * @throws InvalidLoginException if one or more details is incorrect in the login user.
     */
    public static AbsFacade login(String email, String password, LoginType loginType)
            throws InvalidLoginException, SystemMalFunctionException, SQLException {
        switch (loginType) {
            case ADMIN:
                return AdminFacade.performLogin(email, password);
            case COMPANY:
                return CompanyFacade.performLogin(email, password);
            case CUSTOMER:
                return CustomerFacade.performLogin(email, password);
            default:
                throw new InvalidLoginException("Login type is not supported!");
        }
    }

}
