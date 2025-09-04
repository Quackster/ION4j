package net.nillus.ion.HabboHotel.Client.Utilities;

import net.nillus.ion.Net.Messages.ClientMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Decodes a ClientMessage whose body consists out of structured user properties.
 */
public class UserPropertiesDecoder {
    private final Map<Integer, String> mProperties;

    /**
     * Gets a user property with a given ID. If there is no property with that ID decoded, null is returned.
     *
     * @param propID The ID of the property to get.
     * @return The value of the property or null if not found.
     */
    public String getProperty(int propID) {
        return mProperties.get(propID);
    }

    /**
     * Decodes a ClientMessage body to user properties and puts them in the constructed object.
     *
     * @param pMessage The ClientMessage to decode the body with user properties of.
     */
    public UserPropertiesDecoder(ClientMessage pMessage) {
        if (pMessage == null)
            throw new IllegalArgumentException("pMessage cannot be null");

        mProperties = new HashMap<>();
        while (pMessage.getRemainingContent() > 0) {
            int propID = pMessage.popInt32();

            if (propID == 10) { // Spam me yes/no property
                // Weird exception on protocol due to Base64 boolean
                // Skip 7 bytes and ignore this property

                pMessage.advance(7);
                continue;
            }

            String propVal = pMessage.popFixedString();
            mProperties.put(propID, propVal);
        }
    }
}