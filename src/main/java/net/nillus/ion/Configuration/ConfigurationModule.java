package net.nillus.ion.Configuration;

import net.nillus.ion.IonEnvironment;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationModule {
    // Fields
    private Map<String, String> mContent = new HashMap<>();

    // Properties
    public String get(String sKey) {
        return this.getValue(sKey);
    }

    public String getProperty(String sKey) { return this.get(sKey); }

    public void set(String sKey, String value) {
        this.setValue(sKey, value);
    }

    // Methods
    public String getValue(String sKey) {
        if (!mContent.containsKey(sKey))
            return "";

        return mContent.get(sKey);
    }

    public void setValue(String sKey, String sValue) {
        if (mContent.containsKey(sKey))
            mContent.put(sKey, sValue);
        else
            mContent.put(sKey, sValue);
    }

    public boolean tryParseInt32(String sField, IntHolder i) {
        try {
            i.value = Integer.parseInt(get(sField));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public int tryParseInt32(String sField) {
        IntHolder i = new IntHolder();
        tryParseInt32(sField, i);
        return i.value;
    }

    // LoadFromFile method
    public static ConfigurationModule loadFromFile(String sPath) throws FileNotFoundException {
        if (!new File(sPath).exists())
            throw new FileNotFoundException("File at path \"" + sPath + "\" does not exist.");

        ConfigurationModule pConfig = new ConfigurationModule();
        try (BufferedReader pReader = new BufferedReader(new FileReader(sPath))) {
            String sLine;
            while ((sLine = pReader.readLine()) != null) {
                if (sLine.length() == 0 || sLine.charAt(0) == '#')
                    continue; // This line is empty/a comment

                int indexOfDelimiter = sLine.indexOf('=');
                if (indexOfDelimiter != -1) {
                    String sKey = sLine.substring(0, indexOfDelimiter);
                    if (!pConfig.mContent.containsKey(sKey)) {
                        String sValue = sLine.substring(indexOfDelimiter + 1);
                        pConfig.mContent.put(sKey, sValue);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pConfig;
    }

    // Helper classes for unsigned int parsing
    public static class IntHolder {
        public int value = 0;
    }

    public static class UIntHolder {
        public long value = 0; // Using long to represent unsigned int in Java
    }
}