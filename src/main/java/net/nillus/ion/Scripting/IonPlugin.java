package net.nillus.ion.Scripting;

/**
 * Represents a stub for Ion plugins. This class cannot be instantiated.
 */
public abstract class IonPlugin {
    protected String mName;
    protected String mDescription;
    protected String mAuthor;
    protected Version mVersion;
    protected IonPluginHost mHost;

    /**
     * Gets the name of this plugin.
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets the description of this plugin.
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Gets the author of this plugin.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Gets the System.Version object representing the version of this plugin.
     */
    public Version getVersion() {
        return mVersion;
    }

    /**
     * Gets or sets the IonPluginHost of this plugin. The plugin will call back to the host during runtime.
     */
    public IonPluginHost getHost() {
        return mHost;
    }

    public void setHost(IonPluginHost value) {
        this.mHost = value;
    }

    /**
     * This method is called when the plugin is loaded by the host.
     */
    public abstract void load();

    /**
     * This method is called when the plugin is unloaded by the host.
     */
    public abstract void unload();
}