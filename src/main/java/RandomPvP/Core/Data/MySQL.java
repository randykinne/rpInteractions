package RandomPvP.Core.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ***************************************************************************************
 * Copyright (c) Randomizer27 2014. All rights reserved.
 * All code contained within this document and any APIs assocated are
 * the sole property of Randomizer27. Please do not distribute/reproduce without
 * expressed explicit permission from Randomizer27. Not doing so will break the terms of
 * the license, and void any agreements with you, the third party.
 * Thanks.
 * ***************************************************************************************
 */
public class MySQL {

    private static Connection connection;

    public static synchronized Connection getConnection() throws SQLException {
        if (connection != null) {
            if (connection.isValid(1)) {
                return connection;
            } else {
                connection.close();
            }
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bhs112-67?autoReconnect=true", "bhs112-67", "29e771cddc");
        return connection;
    }

    public static String getAddress() {
        return "jdbc:mysql://localhost:3306/bhs112-67?autoReconnect=true";
    }

    public static int getPort() {
        return 3306;
    }

    public static String getDatabase() {
        return "bhs112-67";
    }

    public static String getUsername() {
        return "bhs112-67";
    }

    public static String getPassword() {
        return "29e771cddc";
    }
}
