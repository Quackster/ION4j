package net.nillus.ion.HabboHotel.Client.Requests;

import net.nillus.ion.HabboHotel.Client.GameClient;
import net.nillus.ion.HabboHotel.Client.Utilities.UserPropertiesDecoder;
import net.nillus.ion.HabboHotel.Habbos.Habbo;
import net.nillus.ion.IonEnvironment;
import net.nillus.ion.Net.Messages.ClientMessage;
import net.nillus.ion.Net.Messages.ServerMessage;
import net.nillus.ion.Security.RC4.HabboHexRC4;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PreLogin {
    /// <summary>
    /// 4 - "@D"
    /// </summary>
    public static void TRY_LOGIN(ClientMessage request, ServerMessage response, GameClient client) {
        String sUsername = request.popFixedString();
        String sPassword = request.popFixedString();

        client.login(sUsername, sPassword);
    }

    /// <summary>
    /// 5 - "@E"
    /// </summary>
    public static void CHK_VERSION(ClientMessage request, ServerMessage response, GameClient client) {
        // No longer static! :D
        String sPublicKey = HabboHexRC4.generatePublicKeyString(); //"55wfe030o2b17933arq9512j5u111105ckp230c81rp3m61ew9er3y0d523";
        client.getConnection().setEncryption(sPublicKey);

        response.initialize(1); // "@A"
        response.append(sPublicKey);
        client.sendMessage(response);

        response.initialize(50); // "@r"
        client.sendMessage(response);
    }

    /// <summary>
    /// 6 - "@F"
    /// </summary>
    public static void SET_UID(ClientMessage request, ServerMessage response, GameClient client) {
        String sMachineID = request.popFixedString();

        response.initialize(139); // "BK"
        response.append("Your machine ID is: ");
        response.append(sMachineID);
        response.append("<br>Don't worry, this is not logged yet! :-D");
        client.sendMessage(response);
    }

    /// <summary>
    /// 43 - "@k"
    /// </summary>
    public static void REGISTER(ClientMessage request, ServerMessage response, GameClient client) {
        // Prepare user object
        Habbo NEWUSER = new Habbo();

        // Gather properties
        UserPropertiesDecoder props = new UserPropertiesDecoder(request);
        NEWUSER.setUsername(props.getProperty(2));
        NEWUSER.setPassword(props.getProperty(3));
        NEWUSER.setFigure(props.getProperty(4));
        NEWUSER.setGender(props.getProperty(5).toCharArray()[0]);
        NEWUSER.setMotto(props.getProperty(6));
        NEWUSER.setEmail(props.getProperty(7));
        NEWUSER.setDateOfBirth(props.getProperty(8));
        NEWUSER.setSignedUp(Date.valueOf(LocalDate.now()));

        // Validate properties

        // Store user
        IonEnvironment.getDatabase().INSERT(NEWUSER);

        // Login user
        client.login(NEWUSER.getUsername(), NEWUSER.getPassword());
    }
}