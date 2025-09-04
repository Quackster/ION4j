package net.nillus.ion.HabboHotel.Client.Requests;

import net.nillus.ion.HabboHotel.Client.GameClient;
import net.nillus.ion.Net.Messages.ClientMessage;
import net.nillus.ion.Net.Messages.ServerMessage;

public class Global {
    /**
     * 9 - "@I"
     */
    public static void GETAVAILABLESETS(ClientMessage request, ServerMessage response, GameClient client) {
        response.initialize(8); // "@H"
        response.append("[100,105,110,115,120,125,130,135,140,145,150,155,160,165,170,175,176,177,178,180,185,190,195,200,205,206,207,210,215,220,225,230,235,240,245,250,255,260,265,266,267,270,275,280,281,285,290,295,300,305,500,505,510,515,520,525,530,535,540,545,550,555,565,570,575,580,585,590,595,596,600,605,610,615,620,625,626,627,630,635,640,645,650,655,660,665,667,669,670,675,680,685,690,695,696,700,705,710,715,720,725,730,735,740]");
        client.sendMessage(response);
    }

    /**
     * 41 - "@i"
     */
    public static void FINDUSER(ClientMessage request, ServerMessage response, GameClient client) {
        String[] szContent = response.getContentString().split(Character.toString((char)9));
        //string sUsername = szContent[0];
        //string sSystem = szContent[1];

        response.initialize(147); // "BS"
        // TODO: messenger user information etc

        client.sendMessage(response);
    }
    /**
     * 42 - "@j"
     */
    public static void APPROVENAME(ClientMessage request, ServerMessage response, GameClient client) {
        String sUsername = request.popFixedString();

        response.initialize(36); // "@d"
        response.appendInt32(0); // TODO: error code

        client.sendMessage(response);
    }
    /**
     * 49 - "@q"
     */
    public static void GDATE(ClientMessage request, ServerMessage response, GameClient client) {
        response.initialize(163); // "Bc"
        response.append("1-1-1970");
        client.sendMessage(response);
    }
}
