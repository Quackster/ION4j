package net.nillus.ion.Storage;

import net.nillus.ion.IonEnvironment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private DatabaseServer server;
    private Database database;

    private List<DatabaseClient> clients = new ArrayList<>();
    private List<Boolean> clientAvailable = new ArrayList<>();
    private int clientStarvationCounter;

    private Thread clientMonitor;

    public DatabaseManager(DatabaseServer pServer, Database pDatabase) {
        this.server = pServer;
        this.database = pDatabase;
    }

    public DatabaseManager(String sServer, int Port, String sUser, String sPassword, String sDatabase, int minPoolSize, int maxPoolSize) {
        this.server = new DatabaseServer(sServer, Port, sUser, sPassword);
        this.database = new Database(sDatabase, minPoolSize, maxPoolSize);

        clientMonitor = new Thread(this::monitorClientsLoop);
        clientMonitor.setPriority(Thread.MIN_PRIORITY);

        clientMonitor.start();
    }

    public void startMonitor() {
        clientMonitor = new Thread(this::monitorClientsLoop);
        clientMonitor.setPriority(Thread.MIN_PRIORITY);

        clientMonitor.start();
    }

    public void stopMonitor() {
        if (clientMonitor != null) {
            clientMonitor.interrupt();
        }
    }

    public void destroyClients() {
        synchronized (this) {
            for (DatabaseClient client : clients) {
                client.destroy();
            }
            clients.clear();
        }
    }

    public void destroyManager() {
        server = null;
        database = null;
        clients = null;
        clientAvailable = null;

        clientMonitor = null;
    }

    private void monitorClientsLoop() {
        while (true) {
            try {
                synchronized (this) {
                    long now = System.currentTimeMillis();
                    for (int i = 0; i < clients.size(); i++) {
                        DatabaseClient client = clients.get(i);
                        if (!client.getConnection().isClosed()) {
                            if (client.getInactivity() >= 60 * 1000) { // Not used in the last %x% seconds
                                client.disconnect(); // Temporarily close connection

                                IonEnvironment.getLog().writeInformation("Disconnected database client #" + client.getHandle());
                            }
                        }
                    }
                }

                Thread.sleep(10000); // 10 seconds
            } catch (InterruptedException ex) {
                break;
            } catch (Exception ex) {
                IonEnvironment.getLog().writeError(ex.getMessage());
            }
        }
    }

    public String createConnectionString() {
        StringBuilder connectionString = new StringBuilder();

        connectionString.append("jdbc:mysql://")
                .append(server.getHost())
                .append(":")
                .append(server.getPort())
                .append("/")
                .append(database.getName())
                .append("?user=")
                .append(server.getUser())
                .append("&password=")
                .append(server.getPassword());

        return connectionString.toString();
    }
    /*
    public String createConnectionString() {
        StringBuilder connectionString = new StringBuilder();

        // Server
        connectionString.append("server=").append(server.getHost()).append(";")
                        .append("port=").append(server.getPort()).append(";")
                        .append("user=").append(server.getUser()).append(";")
                        .append("password=").append(server.getPassword());

        // Database
        connectionString.append(";database=").append(database.getName()).append(";")
                        .append("minPoolSize=").append(database.getMinPoolSize()).append(";")
                        .append("maxPoolSize=").append(database.getMaxPoolSize());

        return connectionString.toString();
    }*/

    public DatabaseClient getClient() throws SQLException {
        synchronized (this) {
            for (int i = 0; i < clients.size(); i++) {
                if (clientAvailable.get(i)) {
                    /*if (clients.get(i).getState() == ConnectionState.ERROR) {
                        clients.add(i, new DatabaseClient((int) (i + 1), this)); // Create new client
                    } else */
                    if (clients.get(i).getState() == ConnectionState.CLOSED) {
                        try {
                            clients.get(i).connect();
                            IonEnvironment.getLog().writeInformation("Opening connection for database client #" + clients.get(i).getHandle());
                        } catch (Exception e) {
                            // Handle exception
                        }
                    } else if (clients.get(i).getState() == ConnectionState.OPEN) {
                        IonEnvironment.getLog().writeLine("Handed out client #" + (i + 1));
                        clientAvailable.set(i, false);
                        return clients.get(i);
                    }
                }
            }

            // No clients?
            clientStarvationCounter++;

            // Are we having a structural lack of clients?
            if (clientStarvationCounter >= ((clients.size() + 1) / 2)) // Database hungry much?
            {
                // Heal starvation
                clientStarvationCounter = 0;

                // Increase client amount by 0.3
                try {
                    setClientAmount((int)(clients.size() + 1 * 1.3f));
                } catch (SQLException e) {

                }

                // Re-enter this method
                return getClient();
            }

            DatabaseClient pAnonymous = new DatabaseClient(0, this);
                pAnonymous.connect();

            IonEnvironment.getLog().writeInformation("Handed out anonymous client.");

            return pAnonymous;
        }
    }

    public void releaseClient(int handle) {
        if (clients.size() >= handle - 1) {
            clientAvailable.set(handle - 1, true);
            IonEnvironment.getLog().writeInformation("Released client #" + handle);
        }
    }

    public void setClientAmount(int amount) throws SQLException {
        synchronized (this) {
            if (clients.size() == amount)
                return;

            if (amount < clients.size()) {
                for (int i = amount; i < clients.size(); i++) {
                    clients.get(i).destroy();
                }
            }

            List<DatabaseClient> pClients = new ArrayList<>(amount);
            List<Boolean> pClientAvailable = new ArrayList<>(amount);
            for (int i = 0; i < amount; i++) {
                if (i < clients.size()) {
                    pClients.add(clients.get(i));
                    pClientAvailable.add(clientAvailable.get(i));
                } else {
                    DatabaseClient client = new DatabaseClient(i + 1, this);
                    pClients.add(client);
                    pClientAvailable.add(true); // Elegant?
                }
            }

            clients = pClients;
            clientAvailable = pClientAvailable;
        }
    }

    public void INSERT(IDataObject obj) {
        try (DatabaseClient dbClient = getClient()) {
            obj.INSERT(dbClient);
        } catch (Exception e) {
            IonEnvironment.getLog().writeError(e.getMessage());
        }
    }

    public void DELETE(IDataObject obj) {
        try (DatabaseClient dbClient = getClient()) {
            obj.DELETE(dbClient);
        } catch (Exception e) {
            IonEnvironment.getLog().writeError(e.getMessage());
        }
    }

    public void UPDATE(IDataObject obj) {
        try (DatabaseClient dbClient = getClient()) {
            obj.UPDATE(dbClient);
        } catch (Exception e) {
            IonEnvironment.getLog().writeError(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return server.toString() + ":" + database.getName();
    }
}