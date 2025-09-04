package net.nillus.ion.Net.Connections;

import net.nillus.ion.IonEnvironment;
import net.nillus.ion.Net.Messages.ServerMessage;
import net.nillus.ion.Security.RC4.HabboHexRC4;
import net.nillus.ion.Security.RC4.HabboRc4Exception;
import net.nillus.ion.Specialized.Utilities.ByteUtility;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Date;

public class IonTcpConnection {
    private static final int RECEIVEDATA_BUFFER_SIZE = 512;
    private static int RECEIVEDATA_MILLISECONDS_DELAY = 0;

    private final int mID;
    private final Date mCreatedAt;
    private Socket mSocket = null;
    private ByteBuffer mDataBuffer = ByteBuffer.allocate(RECEIVEDATA_BUFFER_SIZE);
    private RouteReceivedDataCallback mRouteReceivedDataCallback;
    private boolean mEncryptionStarted;
    private HabboHexRC4 mRc4;

    public interface RouteReceivedDataCallback {
        void routeData(byte[] data);
    }

    public IonTcpConnection(int ID, Socket pSocket) {
        mID = ID;
        mSocket = pSocket;
        mCreatedAt = new Date();
    }

    public int getID() {
        return mID;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public int getAgeInSeconds() {
        long diffInMillis = new Date().getTime() - mCreatedAt.getTime();
        return (int) Math.max(0, diffInMillis / 1000);
    }

    public String getIpAddress() {
        if (mSocket == null)
            return "";

        InetSocketAddress address = (InetSocketAddress) mSocket.getRemoteSocketAddress();
        return address.getAddress().getHostAddress();
    }

    public boolean isAlive() {
        return mSocket != null;
    }

    public void start(RouteReceivedDataCallback dataRouter) throws IOException {
        mRouteReceivedDataCallback = dataRouter;
        waitForData();
    }

    public void stop() {
        if (!isAlive())
            return;

        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSocket = null;
        mDataBuffer = null;
        mRc4 = null;
    }

    public boolean testConnection() {
        try {
            mSocket.getOutputStream().write(0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void connectionDead() {
        IonEnvironment.getHabboHotel().getClients().stopClient(mID);
    }

    public void sendData(byte[] data) {
        if (isAlive()) {
            try {
                mSocket.getOutputStream().write(data);
            } catch (IOException e) {
                connectionDead();
            }
        }
    }

    public void sendData(String sData) {
        sendData(sData.getBytes());
    }

    public void sendMessage(ServerMessage pMessage) {
        System.out.println(" [" + mID + "] <-- " + pMessage.getHeader() + pMessage.getContentString());

        sendData(pMessage.getBytes());
    }

    public void setEncryption(String sPublicKey) {
        mRc4 = new HabboHexRC4(sPublicKey);
        mEncryptionStarted = true;
    }

    private void waitForData() throws IOException {
        if (isAlive()) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                try {
                    byte[] buffer = new byte[RECEIVEDATA_BUFFER_SIZE];
                    int numReceivedBytes = mSocket.getInputStream().read(buffer);
                    if (numReceivedBytes > 0) {
                        byte[] dataToProcess = ByteUtility.chompBytes(buffer, 0, numReceivedBytes);

                        if (mEncryptionStarted) {
                            dataToProcess = intArrayToByteArray(mRc4.decipher(byteArrayToIntArray(dataToProcess), numReceivedBytes));
                        }

                        routeData(dataToProcess);
                    }
                } catch (IOException | HabboRc4Exception e) {
                    connectionDead();
                }
            });
        }
    }

    public static int[] byteArrayToIntArray(byte[] bytes) {
        int intLength = bytes.length;
        int[] ints = new int[intLength];
        for (int i = 0; i < intLength; i++) {
            ints[i] = bytes[i];
        }
        return ints;
    }

    public static byte[] intArrayToByteArray(int[] ints) {
        byte[] bytes = new byte[ints.length];
        for (int i = 0; i < ints.length; i++) {
            bytes[i]= (byte) ints[i];
        }
        return bytes;
    }


    private void routeData(byte[] data) {
        if (mRouteReceivedDataCallback != null) {
            mRouteReceivedDataCallback.routeData(data);
        }
    }
}