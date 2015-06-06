package RandomPvP.Core.Data;

import RandomPvP.Core.Util.NetworkUtil;

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
        } catch (ClassNotFoundException ex) {
            NetworkUtil.handleError(ex);
        }

        connection = DriverManager.getConnection("jdbc:mysql://panel.minerack.org:3306/mc3663?autoReconnect=true", "mc3663", "c4298b2866");
        return connection;
    }

    public static String getAddress() {
        return "jdbc:mysql://panel.minerack.org:3306/mc3663?autoReconnect=true";
    }

    public static int getPort() {
        return 3306;
    }

    public static String getDatabase() {
        return "mc3663";
    }

    public static String getUsername() {
        return "mc3663";
    }

    public static String getPassword() {
        return "c4298b2866";
    }
}
