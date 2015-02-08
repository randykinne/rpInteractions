package RandomPvP.Core.Punish;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
public class PunishmentDatabaseConnection {

    private DatabaseConnectionFactory factory;

    /**
     * Constructor for DatabaseConnection.
     *
     * @param factory DatabaseConnectionFactory for information
     */
    public PunishmentDatabaseConnection(DatabaseConnectionFactory factory) {
        this.factory = factory;
    }

    /**
     * Obtain the Connection from the MySQL
     *
     * @return Connection to MySQL
     * @throws java.sql.SQLException
     * @throws ClassNotFoundException
     */
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        try {
            return factory.build();
        } catch (Exception e) {
            System.out.println("--------------------------------------------");
            System.out.println("*FATAL* Failed to connect to MySQL! *FATAL*");
            System.out.println("--------------------------------------------");
        }
        return null;
    }

    /**
     * Prepare a statement from the connection
     * Used rather than getStatement to prevent SQL Injection attacks
     *
     * @param sql SQL For statement
     * @return new PreparedStatement
     * @throws java.sql.SQLException
     * @throws ClassNotFoundException
     */
    public PreparedStatement getPreparedStatement(String sql) throws SQLException, ClassNotFoundException {
        return getConnection().prepareStatement(sql);
    }

    /**
     * Used to build the credentials / information for the DatabaseConnection class
     */
    public static class DatabaseConnectionFactory {

        /**
         * Get the database connection factory builder
         *
         * @return DatabaseConnectionFactory new instance
         */
        public static DatabaseConnectionFactory builder() {
            return new DatabaseConnectionFactory();
        }

        private String host, databaseName, username, password;
        private int port = 3306;

        /**
         * Private constructor, not used
         */
        private DatabaseConnectionFactory() {

        }

        /**
         * Set the host of the factory
         *
         * @param host Host to set
         * @return Factory
         */
        public DatabaseConnectionFactory withHost(String host) {
            this.host = host;
            return this;
        }

        /**
         * Set the database of the factory
         *
         * @param database Database to set
         * @return Factory
         */
        public DatabaseConnectionFactory withDatabase(String database) {
            this.databaseName = database;
            return this;
        }

        /**
         * Set the username of the factory
         *
         * @param username Username to set
         * @return Factory
         */
        public DatabaseConnectionFactory withUsername(String username) {
            this.username = username;
            return this;
        }

        /**
         * Set the password of the factory
         *
         * @param password Password to set
         * @return Factory
         */
        public DatabaseConnectionFactory withPassword(String password) {
            this.password = password;
            return this;
        }

        /**
         * Set the port of the factory
         *
         * @param port Port to set
         * @return Factory
         */
        public DatabaseConnectionFactory withPort(int port) {
            this.port = port;
            return this;
        }

        /**
         * Build the connection factory
         *
         * @return Connection built
         * @throws Exception
         */
        public Connection build() throws Exception {
            if (this.host == null || this.host.isEmpty()) {
                throw new RuntimeException("ConnectionFactory 'host' is null or empty");
            }
            if (this.databaseName == null || this.databaseName.isEmpty()) {
                throw new RuntimeException("ConnectionFactory 'dbName' is null or empty");
            }
            if (this.username == null || this.username.isEmpty()) {
                throw new RuntimeException("ConnectionFactory 'username' is null or empty");
            }
            if (this.password == null || this.password.isEmpty()) {
                throw new RuntimeException("ConnectionFactory 'password' is null or empty");
            }

            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.databaseName, this.username, this.password);
        }
    }

}