package common;

import common.ex.SystemMalFunctionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {

    public static final int MAX_CONNECTION = 10;
    private static ConnectionPool instance;
    private BlockingQueue<Connection> connections;

    private ConnectionPool() throws SystemMalFunctionException {
        connections = new LinkedBlockingQueue<>(); // Create the block.

        for (int i = 0; i < MAX_CONNECTION; i++) {
            try {
                connections.offer((creatConnection())); //Try to insert connection by "offer".
            } catch (SQLException e) {
                throw new SystemMalFunctionException("FATAL: Connection creation failed");
            }
        }
    }

    public static ConnectionPool getInstance() throws SystemMalFunctionException {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public static Connection creatConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/couponsystem?serverTimezone=UTC";
        String user = "root";
        String password = "1234";

        return DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() throws SystemMalFunctionException {
        try {
            return connections.take();
        } catch (InterruptedException e) {
            throw new SystemMalFunctionException("Unable to get a connection " + e.getMessage());
        }
    }

    public void returnConnection(Connection connection) throws SystemMalFunctionException {
        try {
            connections.put(connection);
        } catch (InterruptedException e) {
            throw new SystemMalFunctionException("Unable to return aconnection " + e.getMessage());
        }
    }

    public void closeAllConnections() throws SystemMalFunctionException {
        Connection connection;
        while ((connection = connections.poll()) != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new SystemMalFunctionException("FATAL: Unable to close all connections" + e.getMessage());
            }
        }

    }
}
