package net.nillus.ion.Storage;

/**
 * Represents a storage database.
 */
public class Database {
    private final String mName;
    private final int mMinPoolSize;
    private final int mMaxPoolSize;

    /**
     * The name of the database to connect to.
     */
    public String getName() {
        return mName;
    }

    /**
     * The minimum connection pool size for the database.
     */
    public int getMinPoolSize() {
        return mMinPoolSize;
    }

    /**
     * The maximum connection pool size for the database.
     */
    public int getMaxPoolSize() {
        return mMaxPoolSize;
    }

    /**
     * Constructs a Database instance with given details.
     *
     * @param sName      The name of the database.
     * @param minPoolSize The minimum connection pool size for the database.
     * @param maxPoolSize The maximum connection pool size for the database.
     */
    public Database(String sName, int minPoolSize, int maxPoolSize) {
        if (sName == null || sName.length() == 0)
            throw new IllegalArgumentException(sName);

        mName = sName;
        mMinPoolSize = minPoolSize;
        mMaxPoolSize = maxPoolSize;
    }
}