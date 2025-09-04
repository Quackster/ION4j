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
    private final int mID;
    private ClientMessageHandler mMessageHandler;
    private Habbo mHabbo;
    private boolean mPonged;

    public GameClient(int clientID) {
        mID = clientID;
        mMessageHandler = new ClientMessageHandler(this);
    }

    public int getID() {
        return mID;
    }

    public boolean isLoggedIn() {
        return mHabbo != null;
    }

    public void setPingOK(boolean ponged) {
        this.mPonged = ponged;
    }

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
        IonTcpConnection connection = getConnection();
        if (connection != null) {
            mMessageHandler.registerGlobal();
            mMessageHandler.registerPreLogin();

            ServerMessage helloMessage = new ServerMessage(0); // "@@"
            connection.sendMessage(helloMessage);

            IonTcpConnection.RouteReceivedDataCallback dataRouter = this::handleConnectionData;
            connection.start(dataRouter);
        }
    }

    public void stop() {
        mHabbo = null;
        mMessageHandler.Destroy();
        mMessageHandler = null;
    }

    public void handleConnectionData(byte[] data) {
        int pos = 0;
        while (pos < data.length) {
            try {
                int messageLength = Base64Encoding.decodeInt32(new byte[]{data[pos++], data[pos++], data[pos++]});
                int messageID = Base64Encoding.decodeInt32(new byte[]{data[pos++], data[pos++]});
                byte[] content = new byte[messageLength - 2];
                for (int i = 0; i < content.length; i++) {
                    content[i] = data[pos++];
                }

                ClientMessage message = new ClientMessage(messageID, content);
                mMessageHandler.handleRequest(message);
            } catch (IndexOutOfBoundsException e) {
                IonEnvironment.getHabboHotel().getClients().stopClient(mID);
            } catch (Exception e) {
                IonEnvironment.getLog().writeUnhandledExceptionError("GameClient.HandleConnectionData", e);
            }
        }
    }

    public void login(String username, String password) {
        try {
            mHabbo = IonEnvironment.getHabboHotel().getAuthenticator().login(username, password);

            ServerMessage message1 = new ServerMessage(2); // "@B"
            message1.appendString("fuse_login");
            getConnection().sendMessage(message1);

            ServerMessage message2 = new ServerMessage(3); // "@C"
            getConnection().sendMessage(message2);

            mMessageHandler.unregisterPreLogin();
            mMessageHandler.registerUser();
        } catch (IncorrectLoginException e) {
            sendClientError(e.getMessage());
        }
    }

    public void sendClientError(String sError) {
        ServerMessage message = new ServerMessage(33); // "@a"
        message.append(sError);
        getConnection().sendMessage(message);
    }

    public void sendBanMessage(String sText) {
        ServerMessage message = new ServerMessage(35); // "@c"
        message.append(sText);
        getConnection().sendMessage(message);
    }

    // Helper method added by Quackster
    public void sendMessage(ServerMessage response) {
        this.getConnection().sendMessage(response);
    }
}