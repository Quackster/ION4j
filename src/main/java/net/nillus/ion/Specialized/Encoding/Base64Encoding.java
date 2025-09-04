package net.nillus.ion.Specialized.Encoding;

/**
 * Provides methods for encoding and decoding integers to byte arrays. This class is static.
 */
public class Base64Encoding {
    // Fields
    public static final byte NEGATIVE = 64; // '@'
    public static final byte POSITIVE = 65; // 'A'

    // Methods
    /**
     * Encodes a 32 bit integer to a Base64 byte array of a given length.
     *
     * @param i        The integer to encode.
     * @param numBytes The length of the byte array.
     * @return A byte array holding the encoded integer.
     */
    public static byte[] EncodeInt32(int i, int numBytes) {
        byte[] bzRes = new byte[numBytes];
        for (int j = 1; j <= numBytes; j++) {
            int k = ((numBytes - j) * 6);
            bzRes[j - 1] = (byte) (0x40 + ((i >> k) & 0x3f));
        }

        return bzRes;
    }

    /**
     * Base64 decodes a byte array to a 32 bit integer.
     *
     * @param bzData The input byte array to decode.
     * @return The decoded 32 bit integer.
     */
    public static int decodeInt32(byte[] bzData) {
        int i = 0;
        int j = 0;
        for (int k = bzData.length - 1; k >= 0; k--) {
            int x = bzData[k] - 0x40;
            if (j > 0)
                x *= (int) Math.pow(64.0, (double) j);

            i += x;
            j++;
        }

        return i;
    }
}