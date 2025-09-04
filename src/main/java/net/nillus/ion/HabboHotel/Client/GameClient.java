package net.nillus.ion.HabboHotel.Client;

import net.nillus.ion.HabboHotel.Habbos.Habbo;
import net.nillus.ion.HabboHotel.Habbos.IncorrectLoginException;
import net.nillus.ion.HabboHotel.Habbos.ModerationBanException;
import net.nillus.ion.IonEnvironment;
import net.nillus.ion.Net.Connections.IonTcpConnection;
import net.nillus.ion.Net.Messages.ClientMessage;
import net.nillus.ion.Net.Messages.ServerMessage;
import net.nillus.ion.Specialized.Encoding.Base64Encoding;

import java.io.IOException;

public class GameClient {
    // Fields
    private final int mID;
    private ClientMessageHandler mMessageHandler;
    private Habbo mHabbo;
    private boolean mPonged;

    // Properties
    public int getID() {
        return mID;
    }

    public boolean isLoggedIn() {
        return (mHabbo != null);
    }

    public boolean isPingOK() {
        return mPonged;
    }

    public void setPingOK(boolean value) {
        mPonged = value;
    }

    // Constructors
    public GameClient(int clientID) {
        mID = clientID;
        mMessageHandler = new ClientMessageHandler(this);
    }

    // Methods
    public IonTcpConnection getConnection() {
        return IonEnvironment.getTcpConnections().GetConnection(mID);
    }

    public ClientMessageHandler getMessageHandler() {
        return mMessageHandler;
    }

    public Habbo getHabbo() {
        return mHabbo;
    }

    public void startConnection() throws IOException {
        IonTcpConnection pConnection = IonEnvironment.getTcpConnections().GetConnection(mID);
        if (pConnection != null) {
            // Register request handlers
            // mMessageHandler.registerGlobal();
            // mMessageHandler.registerPreLogin();

            // Send HELLO to client
            ServerMessage HELLO = new ServerMessage(0); // "@@"
            pConnection.sendMessage(HELLO);

            // Create data router and start waiting for data
            IonTcpConnection.RouteReceivedDataCallback dataRouter = this::handleConnectionData;
            pConnection.start(dataRouter);
        }
    }

    public void stop() {
        // Leave room
        // Update last online
        mHabbo = null;

        mMessageHandler.Destroy();
        mMessageHandler = null;
    }

    public void handleConnectionData(byte[] Data) {
        int pos = 0;
        while (pos < Data.length) {
            try {
                // Total length of message (without this): 3 Base64 bytes
                int messageLength = Base64Encoding.DecodeInt32(new byte[]{Data[pos++], Data[pos++], Data[pos++]});

                // ID of message: 2 Base64 bytes
                int messageID = Base64Encoding.DecodeInt32(new byte[]{Data[pos++], Data[pos++]});

                // Data of message: (messageLength - 2) bytes
                byte[] Content = new byte[messageLength - 2];
                for (int i = 0; i < Content.length; i++) {
                    Content[i] = Data[pos++];
                }

                // Create message object
                ClientMessage pMessage = new ClientMessage(messageID, Content);

                // Handle message object
                mMessageHandler.HandleRequest(pMessage);
            } catch (IndexOutOfBoundsException e) { // Bad formatting!
                IonEnvironment.getHabboHotel().getClients().stopClient(mID);
            } catch (Exception ex) {
                IonEnvironment.getLog().WriteUnhandledExceptionError("GameClient.HandleConnectionData", ex);
            }
        }
    }

    public void login(String sUsername, String sPassword) {
        try {
            // Try to login
            mHabbo = IonEnvironment.getHabboHotel().getAuthenticator().Login(sUsername, sPassword);

            // Authenticator has forced unique login now

            // Complete login
            ServerMessage pMessage = new ServerMessage(2); // "@B"
            pMessage.appendString("fuse_login");
            getConnection().sendMessage(pMessage);

            pMessage.initialize(3); // "@C"
            getConnection().sendMessage(pMessage);

            // mMessageHandler.unRegisterPreLogin();
            // mMessageHandler.registerUser();
        } catch (IncorrectLoginException exLogin) {
            sendClientError(exLogin.getMessage());
        }
    }

    public void sendClientError(String sError) {
        ServerMessage pMessage = new ServerMessage(33); // "@a"
        pMessage.append(sError);

        getConnection().sendMessage(pMessage);
    }

    public void sendBanMessage(String sText) {
        ServerMessage pMessage = new ServerMessage(35); // "@c"
        pMessage.append(sText);

        getConnection().sendMessage(pMessage);
    }
}