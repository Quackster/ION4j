package net.nillus.ion.Storage;

/**
 * Represents a database server and holds information about the database host, port and access credentials.
 */
public class DatabaseServer {
    // Fields
    private final String mHost;
    private final int mPort;  // Using int instead of uint as Java does not have unsigned types

    private final String mUser;
    private final String mPassword;

    // Properties
    /**
     * The network host of the database server, eg 'localhost' or '127.0.0.1'.
     */
    public String getHost() {
        return mHost;
    }

    /**
     * The network port of the database server as an unsigned 32 bit integer.
     */
    public int getPort() { // Using int instead of uint
        return mPort;
    }

    /**
     * The username to use when connecting to the database.
     */
    public String getUser() {
        return mUser;
    }

    /**
     * The password to use in combination with the username when connecting to the database.
     */
    public String getPassword() {
        return mPassword;
    }

    // Constructor
    /**
     * Constructs a DatabaseServer object with given details.
     *
     * @param sHost     The network host of the database server, eg 'localhost' or '127.0.0.1'.
     * @param Port      The network port of the database server as an unsigned 32 bit integer.
     * @param sUser     The username to use when connecting to the database.
     * @param sPassword The password to use in combination with the username when connecting to the database.
     */
    public DatabaseServer(String sHost, int Port, String sUser, String sPassword) { // Using int instead of uint
        if (sHost == null || sHost.length() == 0)
            throw new IllegalArgumentException("sHost");
        if (sUser == null || sUser.length() == 0)
            throw new IllegalArgumentException("sUser");

        mHost = sHost;
        mPort = Port;

        mUser = sUser;
        mPassword = (sPassword != null) ? sPassword : "";
    }

    // Methods
    @Override
    public String toString() {
        return mUser + "@" + mHost;
    }
}