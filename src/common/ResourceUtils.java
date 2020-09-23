package common;

import common.ex.SystemMalFunctionException;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ResourceUtils {
    /**
     * Method that designed to close the statement.
     *
     * @param statement
     * @throws SystemMalFunctionException If unable to close the statement.
     */
    public static void close(PreparedStatement statement) throws SystemMalFunctionException {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new SystemMalFunctionException("Unable to close statement: " + e.getMessage());
            }
        }
    }
}
