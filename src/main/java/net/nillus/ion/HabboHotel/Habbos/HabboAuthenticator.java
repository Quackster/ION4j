package net.nillus.ion.HabboHotel.Habbos;

import net.nillus.ion.IonEnvironment;

public class HabboAuthenticator {

    // Constructor region

    // End of constructor region

    // Methods region
    public Habbo Login(String sUsername, String sPassword) throws IncorrectLoginException {
        // Do not use HabboManager.GetHabbo(String) here, as caching is planned to be implemented there
        Habbo pHabbo = new Habbo();
        try {
            if (!pHabbo.loadByUsername(IonEnvironment.getDatabase().getClient().getConnection(), sUsername))
                throw new IncorrectLoginException("login incorrect: Wrong username");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!pHabbo.getPassword().equals(sPassword))
            throw new IncorrectLoginException("login incorrect: Wrong password");

        // Drop old client (if logged in via other connection)
        IonEnvironment.getHabboHotel().getClients().dropClientOfHabbo(pHabbo.getID());

        return pHabbo;
    }
    // End of methods region
}