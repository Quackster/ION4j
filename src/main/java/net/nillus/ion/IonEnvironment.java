package net.nillus.ion;

import net.nillus.ion.Configuration.ConfigurationModule;
import net.nillus.ion.Core.LogType;
import net.nillus.ion.Core.Logging;
import net.nillus.ion.HabboHotel.HabboHotel;
import net.nillus.ion.Net.Connections.IonTcpConnectionManager;
import net.nillus.ion.Storage.Database;
import net.nillus.ion.Storage.DatabaseManager;
import net.nillus.ion.Storage.DatabaseServer;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;

public class IonEnvironment {
    // Fields
    private static Logging mLog = new Logging();
    private static ConfigurationModule mConfig;
    private static Charset mTextEncoding = Charset.forName("ISO-8859-1"); // Latin1

    private static DatabaseManager mDatabaseManager;
    private static IonTcpConnectionManager mTcpConnectionManager;

    private static HabboHotel mHabboHotel;

    // Properties
    public static Logging getLog() {
        return mLog;
    }

    // Methods
    /**
     * Initializes the Ion server environment.
     */
    public static void initialize() {
        mLog.setMinimumLogImportancy(LogType.Debug);
        mLog.writeLine("Initializing Ion environment.");

        try {
            // Try to initialize configuration
            try {
                mConfig = ConfigurationModule.loadFromFile("settings");
            } catch (FileNotFoundException ex) {
                mLog.writeError("Failed to load configuration file, exception message was: " + ex.getMessage());
                IonEnvironment.destroy();
                return;
            }

            // Initialize database and test a connection by getting & releasing it
            DatabaseServer pDatabaseServer = new DatabaseServer(
                    IonEnvironment.getConfiguration().getProperty("db1.server.host"),
                    Integer.parseInt(IonEnvironment.getConfiguration().getProperty("db1.server.port")),
                    IonEnvironment.getConfiguration().getProperty("db1.server.uid"),
                    IonEnvironment.getConfiguration().getProperty("db1.server.pwd"));

            Database pDatabase = new Database(
                    IonEnvironment.getConfiguration().getProperty("db1.name"),
                    Integer.parseInt(IonEnvironment.getConfiguration().getProperty("db1.minpoolsize")),
                    Integer.parseInt(IonEnvironment.getConfiguration().getProperty("db1.maxpoolsize")));

            mDatabaseManager = new DatabaseManager(pDatabaseServer, pDatabase);
            mDatabaseManager.setClientAmount(2);
            mDatabaseManager.releaseClient(mDatabaseManager.getClient().getHandle());
            mDatabaseManager.startMonitor();

            // Initialize TCP listener
            mTcpConnectionManager = new IonTcpConnectionManager(
                    IonEnvironment.getConfiguration().getProperty("net.tcp.localip"),
                    Integer.parseInt(IonEnvironment.getConfiguration().getProperty("net.tcp.port")),
                    Integer.parseInt(IonEnvironment.getConfiguration().getProperty("net.tcp.maxcon")));
            mTcpConnectionManager.GetListener().start();

            // Try to initialize Habbo Hotel
            mHabboHotel = new HabboHotel();

            mLog.writeLine("Initialized Ion environment.");
        } catch (Exception ex) { // Catch all other exceptions
            mLog.writeError("Unhandled exception occurred during initialization of Ion environment. Exception message: " + ex.getMessage());
        }
    }

    /**
     * Destroys the Ion server environment and exits the application.
     */
    public static void destroy() {
        mLog.writeLine("Destroying Ion environment.");

        // Destroy Habbo Hotel 8-) (and all inner modules etc)
        if (getHabboHotel() != null) {
            getHabboHotel().Destroy();
        }

        // Clear connections
        if (getTcpConnections() != null) {
            IonEnvironment.getLog().writeLine("Destroying TCP connection manager.");
            getTcpConnections().GetListener().stop();
            getTcpConnections().GetListener().destroy();
            getTcpConnections().DestroyManager();
        }

        // Stop database
        if (getDatabase() != null) {
            IonEnvironment.getLog().writeLine("Destroying database " + getDatabase().toString());
            getDatabase().stopMonitor();
            getDatabase().destroyClients();
            getDatabase().destroyManager();
        }

        mLog.writeLine("Press a key to exit.");

        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(2);
    }

    /**
     * Returns the configuration module.
     *
     * @return ConfigurationModule holding configuration values for Ion.
     */
    public static ConfigurationModule getConfiguration() {
        return mConfig;
    }

    /**
     * Returns the default Charset for encoding and decoding text.
     *
     * @return Charset
     */
    public static Charset getDefaultTextEncoding() {
        return mTextEncoding;
    }

    public static DatabaseManager getDatabase() {
        return mDatabaseManager;
    }

    public static IonTcpConnectionManager getTcpConnections() {
        return mTcpConnectionManager;
    }

    public static HabboHotel getHabboHotel() {
        return mHabboHotel;
    }
}