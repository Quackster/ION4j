package net.nillus.ion.HabboHotel.Habbos;

import net.nillus.ion.IonEnvironment;

/**
 * Manages service users ('Habbo's') and provides methods for updating and retrieving accounts.
 */
public class HabboManager {
    public Habbo getHabbo(int ID) {
        Habbo pHabbo = new Habbo();
        try {
            if (pHabbo.loadByID(IonEnvironment.getDatabase().getClient().getConnection(), ID))
                return pHabbo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
    public Habbo getHabbo(String sUsername) {
        Habbo pHabbo = new Habbo();
        try {
            if (pHabbo.loadByUsername(IonEnvironment.getDatabase().getClient().getConnection(), sUsername))
                return pHabbo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}