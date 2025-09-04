package net.nillus.ion.Net.Connections;

import net.nillus.ion.IonEnvironment;

import java.util.HashMap;
import java.util.Map;

public class IonTcpConnectionManager {
    // Fields
    private final int mMaxSimultaneousConnections;
    private Map<Integer, IonTcpConnection> mConnections;
    private IonTcpConnectionListener mListener;

    // Constructors
    public IonTcpConnectionManager(String sLocalIP, int Port, int maxSimultaneousConnections) {
        int initialCapacity = maxSimultaneousConnections;
        if (maxSimultaneousConnections > 4)
            initialCapacity /= 4; // Set 1/4 of max connections as initial capacity to avoid too much resizing

        mConnections = new HashMap<>(initialCapacity);
        this.mMaxSimultaneousConnections = maxSimultaneousConnections;

        mListener = new IonTcpConnectionListener(sLocalIP, Port, this);
    }

    // Methods
    public void DestroyManager() {
        mConnections.clear();
        mConnections = null;
        mListener = null;
    }

    public boolean ContainsConnection(int clientID) {
        return mConnections.containsKey(clientID);
    }

    public IonTcpConnection GetConnection(int clientID) {
        try {
            return mConnections.get(clientID);
        } catch (Exception e) {
            return null;
        }
    }

    public IonTcpConnectionListener GetListener() {
        return mListener;
    }

    public void HandleNewConnection(IonTcpConnection pConnection) {
        // Add connection to collection
        mConnections.put((int) pConnection.getID(), pConnection);

        // Create session for new client
        IonEnvironment.getHabboHotel().getClients().startClient(pConnection.getID());
    }

    public void DropConnection(int clientID) {
        IonTcpConnection pConnection = GetConnection(clientID);
        if (pConnection != null) {
            System.out.println("Dropped OkapiTcpConnection [" + clientID + "] of " + pConnection.getIpAddress());

            pConnection.stop();
            mConnections.remove(clientID);
        }
    }

    public boolean TestConnection(int clientID) {
        IonTcpConnection pConnection = GetConnection(clientID);
        if (pConnection != null)
            return pConnection.testConnection(); // Try to send data

        return false; // Connection not here!
    }
}