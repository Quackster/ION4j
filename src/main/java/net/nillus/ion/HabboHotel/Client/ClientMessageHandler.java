package net.nillus.ion.HabboHotel.Client;

import net.nillus.ion.IonEnvironment;
import net.nillus.ion.Net.Messages.ClientMessage;
import net.nillus.ion.Net.Messages.ServerMessage;

public class ClientMessageHandler {
    private static final int HIGHEST_MESSAGEID = 200; // "B]" : GETAVAILABLEBADGES
    private GameClient mSession;

    private ClientMessage Request;
    private ServerMessage Response;

    private interface RequestHandler {
        void handle();
    }
    private RequestHandler[] mRequestHandlers;

    public ClientMessageHandler(GameClient pSession) {
        mSession = pSession;
        mRequestHandlers = new RequestHandler[HIGHEST_MESSAGEID + 1];

        Response = new ServerMessage(0);
    }

    /// <summary>
    /// Destroys all the resources in the ClientMessageHandler.
    /// </summary>
    public void Destroy() {
        mSession = null;
        mRequestHandlers = null;

        Request = null;
        Response = null;
    }
    /// <summary>
    /// Invokes the matching request handler for a given ClientMessage.
    /// </summary>
    /// <param name="pRequest">The ClientMessage object to process.</param>
    public void HandleRequest(ClientMessage pRequest) {
        IonEnvironment.getLog().WriteLine("[" + mSession.getID() + "] --> " + pRequest.getHeader() + pRequest.getContentString());

        if (pRequest.getID() > HIGHEST_MESSAGEID)
            return; // Not in protocol
        if (mRequestHandlers[pRequest.getID()] == null)
            return; // Handler not registered

        // Handle request
        Request = pRequest;
        mRequestHandlers[pRequest.getID()].handle();
        Request = null;
    }
    /// <summary>
    /// Sends the current response ServerMessage on the stack.
    /// </summary>
    public void SendResponse() {
        if(Response.getID() > 0)
            mSession.getConnection().sendMessage(Response);
    }
}