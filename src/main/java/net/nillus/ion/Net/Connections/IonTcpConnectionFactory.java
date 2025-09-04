package net.nillus.ion.Net.Connections;

import java.net.Socket;

import net.nillus.ion.IonEnvironment;

public class IonTcpConnectionFactory {
    // A 32 bit unsigned integer that is incremented every time a connection is added.
    private int mConnectionCounter;

    // Gets the total amount of created connections.
    public int getCount() {
        return mConnectionCounter;
    }

    /**
     * Creates an IonTcpConnection instance for a given socket and assigns it a unique ID.
     *
     * @param pSocket The java.net.Socket object to base the connection on.
     * @return IonTcpConnection
     */
    public IonTcpConnection createConnection(Socket pSocket) {
        if (pSocket == null)
            return null;

        IonTcpConnection pConnection = new IonTcpConnection(++mConnectionCounter, pSocket);
        IonEnvironment.getLog().WriteInformation(String.format("Created IonTcpConnection [%d] for %s.", pConnection.getID(), pConnection.getIpAddress()));

        return pConnection;
    }
}