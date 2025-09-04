package net.nillus.ion.Net.Connections;

import net.nillus.ion.IonEnvironment;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

/**
 * Listens for TCP connections at a given port, asynchronously accepting connections and optionally insert them in the Ion environment connection manager.
 */
public class IonTcpConnectionListener {
    /** The maximum length of the connection request queue for the listener as an integer. */
    private static final int LISTENER_CONNECTIONREQUEST_QUEUE_LENGTH = 1;
    private InetAddress pIP;
    private int Port;

    /** A ServerSocket that listens for connections. */
    private ServerSocket mListener = null;
    private boolean mIsListening = false;

    private IonTcpConnectionManager mManager;
    /** An IonTcpConnectionFactory instance that is capable of creating IonTcpConnections. */
    private IonTcpConnectionFactory mFactory;

    /** Gets whether the listener is listening for new connections or not. */
    public boolean isListening() {
        return mIsListening;
    }

    /**
     * Constructs an IonTcpConnection listener and binds it to a given local IP address and TCP port.
     * @param sLocalIP The IP address string to parse and bind the listener to.
     * @param Port The TCP port number to parse the listener to.
     */
    public IonTcpConnectionListener(String sLocalIP, int Port, IonTcpConnectionManager pManager) {
        pIP = null;
        try {
            pIP = InetAddress.getByName(sLocalIP);
        } catch (IOException e) {
            pIP = InetAddress.getLoopbackAddress();
            IonEnvironment.getLog().WriteWarning(String.format("Connection listener was unable to parse the given local IP address '%s', now binding listener to '%s'.", sLocalIP, pIP.getHostAddress()));
        }

        mFactory = new IonTcpConnectionFactory();
        mManager = pManager;

    }

    /**
     * Starts listening for connections.
     */
    public void start() {
        if (mIsListening)
            return;

        try {
            mListener = new ServerSocket(Port, LISTENER_CONNECTIONREQUEST_QUEUE_LENGTH, pIP);
            IonEnvironment.getLog().WriteLine(String.format("IonTcpConnectionListener initialized and bound to %s:%d.", pIP.getHostAddress(), Port));
            mIsListening = true;
            waitForNextConnection();
        } catch (IOException e) {
            IonEnvironment.getLog().WriteError("Failed to bind the server socket: " + e.getMessage());
        }
    }

    /**
     * Stops listening for connections.
     */
    public void stop() {
        if (!mIsListening)
            return;

        mIsListening = false;
        try {
            mListener.close();
        } catch (IOException e) {
            IonEnvironment.getLog().WriteError("Failed to close the server socket: " + e.getMessage());
        }
    }

    /**
     * Destroys all resources in the connection listener.
     */
    public void destroy() {
        stop();

        mListener = null;
        mManager = null;
        mFactory = null;
    }

    /**
     * Waits for the next connection request in it's own thread.
     */
    private void waitForNextConnection() {
        if (mIsListening) {
            new Thread(() -> {
                try {
                    Socket pSocket = mListener.accept();
                    // TODO: IP blacklist

                    IonTcpConnection pConnection = mFactory.createConnection(pSocket);
                    if (pConnection != null) {
                        mManager.HandleNewConnection(pConnection);
                    }
                } catch (IOException e) {
                    IonEnvironment.getLog().WriteError("Failed to accept a connection: " + e.getMessage());
                } finally {
                    if (mIsListening)
                        waitForNextConnection(); // Re-start the process for next connection
                }
            }).start();
        }
    }
}