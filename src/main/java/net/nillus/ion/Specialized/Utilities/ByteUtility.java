package net.nillus.ion.Specialized.Utilities;

/**
 * Provides various common methods for working with bytes.
 */
public class ByteUtility {
    public static byte[] chompBytes(byte[] bzBytes, int Offset, int numBytes) {
        int End = (Offset + numBytes);
        if (End > bzBytes.length)
            End = bzBytes.length;

        int chunkLength = End - numBytes;
        if (numBytes > bzBytes.length)
            numBytes = bzBytes.length;
        if (numBytes < 0)
            numBytes = 0;

        byte[] bzChunk = new byte[numBytes];
        for (int x = 0; x < numBytes; x++) {
            bzChunk[x] = bzBytes[Offset++];
        }

        return bzChunk;
    }
}