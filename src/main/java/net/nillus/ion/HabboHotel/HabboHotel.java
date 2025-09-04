package net.nillus.ion.HabboHotel;

import net.nillus.ion.Configuration.ConfigurationModule;
import net.nillus.ion.HabboHotel.Client.GameClientManager;
import net.nillus.ion.HabboHotel.Habbos.HabboAuthenticator;
import net.nillus.ion.HabboHotel.Habbos.HabboManager;
import net.nillus.ion.IonEnvironment;

public class HabboHotel {
    /// <summary>
    /// Represents a multiuser, virtual hotel where users can create avatars, to chat with other users in spaces set up as 'rooms'. Users have the ability to create their own 'guestroom' as well, and they are able to decorate it with virtual furniture.
    /// </summary>
    private int mVersion;

    // Modules
    private GameClientManager mClientManager = null;
    private HabboManager mHabboManager = null;
    private HabboAuthenticator mAuthenticator = null;

    /// <summary>
    /// Gets the version of the game client as a 32 bit unsigned integer.
    /// </summary>
    public int getVersion() {
        return mVersion;
    }

    /// <summary>
    /// Constructs a Habbo Hotel environment and tries to initialize it.
    /// </summary>
    public HabboHotel() {
        // Try to parse version
        var intHolder = new ConfigurationModule.IntHolder();
        IonEnvironment.getConfiguration().tryParseInt32("projects.habbo.client.version", intHolder);
        mVersion = intHolder.value;

        // Initialize HabboHotel project modules
        mClientManager = new GameClientManager();
        mHabboManager = new HabboManager();
        mAuthenticator = new HabboAuthenticator();

        // Start connection checker for clients
        mClientManager.StartConnectionChecker();

        // Print that we are done!
        IonEnvironment.getLog().WriteLine(String.format("Initialized project 'Habbo Hotel' for version %d.", mVersion));
    }

    public void Destroy() {
        // Clear clients
        if (getClients() != null) {
            getClients().Clear();
            getClients().StopConnectionChecker();
        }

        IonEnvironment.getLog().WriteLine(String.format("Destroyed project 'Habbo Hotel' for version %d.", mVersion));
    }

    /// <summary>
    /// Returns the game client manager.
    /// </summary>
    public GameClientManager getClients() {
        return mClientManager;
    }
    /// <summary>
    /// Returns the Habbo manager.
    /// </summary>
    public HabboManager getHabbos() {
        return mHabboManager;
    }
    /// <summary>
    /// Returns the Habbo authenticator.
    /// </summary>
    public HabboAuthenticator getAuthenticator() {
        return mAuthenticator;
    }
}