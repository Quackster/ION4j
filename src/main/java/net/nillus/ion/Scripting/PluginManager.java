package net.nillus.ion.Scripting;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PluginManager {

    // Fields
    private List<IonPlugin> mPlugins = new ArrayList<>();

    // Methods
    /**
     * Unloads the current plugins and clears the plugin collection.
     */
    public synchronized void clear() {
        for (IonPlugin pPlugin : mPlugins) {
            pPlugin.unload();
        }
        mPlugins.clear();
    }

    public List<IonPlugin> getPlugins(String sDirectory) {
        List<IonPlugin> pPlugins = new ArrayList<>();
        File directory = new File(sDirectory);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".dll"));
            if (files != null) {
                for (File file : files) {
                    IonPlugin pPlugin = this.instantiatePluginFromAssembly(file.getAbsolutePath());
                    if (pPlugin != null) {
                        pPlugins.add(pPlugin);
                    }
                }
            }
        }

        return pPlugins;
    }

    /**
     * Tries to instantiate and return an IonPlugin descendant by loading a .NET assembly file at a given path. Null is returned if the target file does not exist, is not a .NET assembly file or is not a IonPlugin descendant.
     */
    private IonPlugin instantiatePluginFromAssembly(String sAssemblyPath) {
        try {
            Class<?> pClass = Class.forName(sAssemblyPath);
            IonPlugin pPlugin = (IonPlugin) pClass.getDeclaredConstructor().newInstance();
            return pPlugin;
        } catch (Exception e) { // Either file not found, no CLI header or no IonPlugin deriver
            e.printStackTrace();
            return null;
        }
    }
}