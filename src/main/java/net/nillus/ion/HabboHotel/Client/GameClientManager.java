package net.nillus.ion.HabboHotel.Client;

import net.nillus.ion.IonEnvironment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameClientManager {
    // Fields
    private Thread mConnectionChecker;
    private Map<Integer, GameClient> mClients;

    // Constructor
    public GameClientManager() {
        mClients = new HashMap<>();
    }

    // Methods
    public void Clear() {
        mClients.clear();
    }

    public void StartConnectionChecker() {
        if (mConnectionChecker == null) {
            mConnectionChecker = new Thread(this::TestClientConnections);
            mConnectionChecker.setPriority(Thread.NORM_PRIORITY - 1);

            mConnectionChecker.start();
        }
    }

    public void StopConnectionChecker() {
        if (mConnectionChecker != null) {
            try {
                mConnectionChecker.interrupt();
            } catch (Exception e) {
            }

            mConnectionChecker = null;
        }
    }

    private void TestClientConnections() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                /*
                // Prepare "@r" message data
                byte[] PINGDATA = new ServerMessage(50).getBytes(); // "@r"
                
                // Gather timed out clients and reset ping status for in-time clients
                List<Integer> pTimedOutClients = new ArrayList<>();
                synchronized (mClients) {
                    for (GameClient pClient : mClients.values()) {
                        if (pClient.isPingOK()) {
                            pClient.setPingOK(false);
                            pClient.getConnection().sendData(PINGDATA);
                        } else {
                            pTimedOutClients.add(pClient.getId());
                        }
                    }

                    // Stop the gathered timed out clients
                    for (int timedOutClientID : pTimedOutClients) {
                        stopClient(timedOutClientID);
                    }
                }
                */

                synchronized (mClients) { // Currently busy!
                    // Gather clients with dead connection
                    List<Integer> pDeadClients = new ArrayList<>();
                    for (Integer clientID : mClients.keySet()) {
                        if (!IonEnvironment.getTcpConnections().TestConnection(clientID)) {
                            pDeadClients.add(clientID);
                        }
                    }

                    // Stop the gathered dead clients
                    for (Integer clientID : pDeadClients) {
                        stopClient(clientID);
                    }
                }


                // Sleep for 30 seconds and repeat!
                Thread.sleep(30000);
            } catch (InterruptedException e) { // Nothing special!
                Thread.currentThread().interrupt();
            }
        }
    }

    public GameClient getClient(int clientID) {
        return mClients.get(clientID);
    }

    public boolean removeClient(int clientID) {
        return mClients.remove(clientID) != null;
    }

    public void startClient(int clientID) {
        GameClient pClient = new GameClient(clientID);
        mClients.put(clientID, pClient);

        try {
            pClient.startConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopClient(int clientID) {
        GameClient pClient = getClient(clientID);
        if (pClient != null) {
            // Stop & drop connection
            IonEnvironment.getTcpConnections().DropConnection(clientID);

            // Stop client
            pClient.stop();

            // Drop client
            mClients.remove(clientID);

            // Log event
            IonEnvironment.getLog().WriteInformation("Stopped client " + clientID);
        }
    }

    public GameClient getClientOfHabbo(int accountID) {
        synchronized (mClients) {
            for (GameClient pClient : mClients.values()) {
                if (pClient.getHabbo() != null && pClient.getHabbo().getID() == accountID) {
                    return pClient;
                }
            }
        }

        return null;
    }

    public int getClientIdOfHabbo(int accountID) {
        GameClient pClient = getClientOfHabbo(accountID);

        return (pClient != null) ? pClient.getID() : 0;
    }

    public void dropClientOfHabbo(int accountID) {
        int clientID = getClientIdOfHabbo(accountID);
        if (clientID > 0) {
            stopClient(clientID);
        }
    }
}