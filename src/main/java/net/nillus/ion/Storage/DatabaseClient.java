package net.nillus.ion.Storage;

import java.sql.*;
import java.util.Date;
import java.util.Properties;

public class DatabaseClient implements AutoCloseable {
    // Fields
    private int mHandle;
    private Date mLastActivity;

    private DatabaseManager mManager;
    private Connection mConnection;
    private PreparedStatement mCommand;

    // Properties
    public int getHandle() {
        return mHandle;
    }

    public boolean isAnonymous() {
        return (mHandle == 0);
    }

    public Date getLastActivity() {
        return mLastActivity;
    }

    public int getInactivity() {
        return (int) ((new Date()).getTime() - mLastActivity.getTime()) / 1000;
    }

    public ConnectionState getState() {
        try {
            return (mConnection != null && !mConnection.isClosed()) ? ConnectionState.OPEN : ConnectionState.CLOSED;
        } catch (SQLException e) {
            return ConnectionState.ERROR;
        }
    }

    // Constructor
    public DatabaseClient(int Handle, DatabaseManager pManager) throws SQLException {
        if (pManager == null)
            throw new IllegalArgumentException("pManager");

        mHandle = Handle;
        mManager = pManager;

        /*
        Properties connectionProps = new Properties();
        connectionProps.put("user", mManager.getUsername());
        connectionProps.put("password", mManager.getPassword());

        mConnection = DriverManager.getConnection(mManager.getJdbcUrl(), connectionProps);
        mCommand = mConnection.prepareStatement("");

         */

        mConnection = DriverManager.getConnection(mManager.createConnectionString());

        updateLastActivity();
    }

    // Methods
    public void connect() throws SQLException {
        if (mConnection == null)
            throw new SQLException("Connection instance of database client " + mHandle + " holds no value.");
        if (mConnection.isClosed())
            throw new SQLException("Connection instance of database client " + mHandle + " requires to be closed before it can open again.");

        try {
            mConnection.createStatement();
        } catch (SQLException e) {
            throw new SQLException("Failed to open connection for database client " + mHandle + ", exception message: " + e.getMessage());
        }
    }

    public void disconnect() throws SQLException {
        if (!mConnection.isClosed()) {
            try {
                mConnection.close();
            } catch (SQLException e) {
                // Handle the exception
            }
        }
    }

    public void destroy() {
        try {
            disconnect();
        } catch (SQLException e) {
            // Handle the exception
        }

        try {
            if (mCommand != null) {
                mCommand.close();
                mCommand = null;
            }
        } catch (SQLException e) {
            // Handle the exception
        }

        mManager = null;
    }

    public void updateLastActivity() {
        mLastActivity = new Date();
    }

    public DatabaseManager getManager() {
        return mManager;
    }

    public void addParamWithValue(int sParam, Object val) throws SQLException {
        mCommand.setObject(sParam, String.valueOf(val));
    }

    public void executeQuery(String sQuery) throws SQLException {
        mCommand = mConnection.prepareStatement(sQuery);
        mCommand.execute();
        mCommand.close();
    }

    public ResultSet readDataSet(String sQuery) throws SQLException {
        Statement stmt = mConnection.createStatement();
        return stmt.executeQuery(sQuery);
    }

    public ResultSet readDataTable(String sQuery) throws SQLException {
        Statement stmt = mConnection.createStatement();
        return stmt.executeQuery(sQuery);
    }

    public ResultSet readDataRow(String sQuery) throws SQLException {
        ResultSet rs = readDataTable(sQuery);
        if (rs.next())
            return rs;

        return null;
    }

    public String readString(String sQuery) throws SQLException {
        ResultSet rs = readDataTable(sQuery);
        if (rs.next())
            return rs.getString(1);

        return null;
    }

    public int readInt32(String sQuery) throws SQLException {
        ResultSet rs = readDataTable(sQuery);
        if (rs.next())
            return rs.getInt(1);

        return 0;
    }

    // AutoCloseable members
    @Override
    public void close() {
        try {
            if (!isAnonymous()) { // No disposing for this client yet! Return to the manager!
                mCommand.close();
                mManager.releaseClient(mHandle);
            } else { // Anonymous client, dispose this right away!
                destroy();
            }
        } catch (SQLException e) {
            // Handle the exception
        }
    }

    public Connection getConnection() {
        return mConnection;
    }
}