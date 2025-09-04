package net.nillus.ion.Security.RC4;

import java.util.Random;
import java.lang.StringBuilder;
import java.nio.charset.StandardCharsets;

public class HabboHexRC4 {
    // Static fields
    private static final int[] PRIVATE_KEY = { 44, 214, 122, 91, 114, 79, 16, (int)141, 115, 110, (int)207, (int)216, (int)238, 65, 59, 50, (int)186, 70, 128, (int)248, 107, 12, 33, (int)247, 66, 79, 53, (int)216, 74, 81, (int)145, (int)249, (int)179, 111, (int)233, 56, 49, 92, 123, 162, 26, 46, (int)182, 96, (int)208, 93, 114, 170, (int)255, 19, (int)164, (int)208, 79, 91, (int)241, 128, (int)158, 25, (int)252, (int)194, (int)217, 20, 22, 44, 1, (int)253, 45, 91, 113, 89, (int)203, 80, 34, 112, 99, 82, (int)243, 8, 90, (int)240, 39, 17, (int)230, (int)232, 80, (int)180, (int)173, (int)164, 112, (int)163, (int)217, (int)155, 170, 41, (int)187, (int)156, (int)213, (int)199, (int)176, (int)180, (int)180, (int)236, (int)167, 128, 31, (int)155, (int)210, (int)208, 55, (int)198, 5, (int)243, 27, (int)208, 78, 13, (int)142, 64, 80, 21, 18, 19, 175, (int)252, 126, (int)194, 11, (int)190, 99, 94, (int)184, (int)248, (int)167, 77, 45, 5, (int)141, 128, 72, 42, 45, 107, 88, (int)140, (int)147, 30, (int)248, (int)243, (int)208, 82, 137, (int)181, 69, (int)177, 128, (int)216, 25, 3, (int)239, (int)179, (int)160, (int)159, 129, 135, 23, 62, 192, 90, 91, 172, 119, (int)255, 135, 39, 78, (int)216, 12, (int)188, 45, (int)204, 93, 54, 30, (int)165, 129, (int)178, 151, (int)253, 92, 31, (int)196, 126, 4, 72, (int)182, (int)180, (int)216, (int)144, 78, (int)255, (int)185, (int)228, 134, 92, 103, (int)141, 2, (int)144, 123, 161, 101, (int)187, (int)145, (int)187, (int)171, 62, 21, (int)244, 17, (int)231, (int)203, 120, 176, 87, (int)150, 89, (int)244, 7, 29, 21, (int)235, 165, 86, 125, 184, 90, (int)232, (int)232, (int)145, 15, (int)198, 165, 103, 12, (int)245, (int)177, 151, 29, 45, 26, 184, 91, 20, 16, (int)231, 174, (int)237, (int)207, 165, (int)251, 114, (int)185, (int)245, 68, 82, 116, (int)216, 0, (int)203, 89, (int)234, 174, 100, (int)220, 60, 42, 60, 103, 17, 93, (int)208, 72, (int)242, 116, (int)148, 84, (int)230, 115, 56, (int)138, 134, 107, 199, 17, 73, 58, 75, (int)187, 200, (int)253, (int)141, (int)249, (int)246, 74, (int)201, (int)166, (int)194, (int)156, 72, (int)221, 20, 6, 91, (int)191, (int)243, 100, 3, 113, 79, 59, 175, 94, 112, 81, 69, (int)166, 145, 89, 163, 111, (int)180, 110, 146, 156, 43, (int)206, (int)248, 22, (int)188, 27, 123, 152, 65, 136, (int)212, (int)185, 83, 104, 162, 69, 21, (int)208, 116, 78, 193, 2, (int)179, (int)222, 109, 66, 75, 56, 46, 21, 105, (int)140, (int)236, 13, 78, 58, 30, 55, 114, (int)228, 96, (int)156, 89, 179, 116, 30, 63, 7, 52, 10, (int)182, 25, 87, 29, (int)166, 75, 64, 89, 30, 110, 40, 50, 121, 107, 44, 151, (int)246, (int)147, 131, 39, 105, (int)227, 58, 66, 56, 82, 107, 73, 91, 133, (int)210, (int)202, 174, 56, 108, 29, 117, 109, 128, 103, (int)237, (int)227, 13, (int)138, (int)177, (int)180, 146, (int)142, 82, 83, 115, (int)194, (int)148, 62, 74, 92, (int)154, 95, (int)194, 104, (int)216, 2, (int)166, 59, (int)150, 137, (int)164, 49, (int)189, 33, (int)236, 46, 82, (int)169, 73, 77, (int)177, 81, 67, 98, (int)181, 116, 49, 76, 97, (int)204, (int)227, 29, (int)203, 113, 110, (int)242, (int)255, (int)140, 46, (int)204, (int)144, 39, (int)234, (int)167, 30, (int)150, 110, (int)219, (int)138, (int)136, 88, 12, (int)179, 71, 23, (int)150, (int)233, 80, (int)217, (int)244, (int)248, 111, 65, (int)255, 69, (int)217, 55, 49, 43, (int)228, (int)225, 10, 123, 71, 41, (int)173, 7, 15, (int)194, 8, 87, (int)209, 75, (int)212, (int)179, 144, 151, 48, 134, 47, 109, (int)212, 8, 24, 66, 102, (int)198, (int)211, 35, 184, (int)154, 76, (int)147, (int)170, 90, (int)247, 53, 31, (int)164, 5, (int)189, 12, (int)208, 99, (int)185, 52, 74, (int)154, 137, (int)235, 112, 132, 5, 16, 65, 124, 87, 109, 83, (int)170, 37, 20, 88, (int)134, 2, 86, (int)218, 169, (int)222, 128, (int)202, 28, 87, 81, (int)154, (int)199, 124, (int)239, 130, 47, 88, (int)219, 61, 97, 18, 95, 81, (int)144, 123, 64, 49, (int)239, 24, 87, (int)134, 24, 102, (int)230, 169, 145, 83, 11, 126, (int)166, 230, (int)149, 31, (int)164, 94, (int)197, 27, (int)225, 35, 17, 24, (int)241, (int)140, 17, 42, 10, 40, 124, (int)217, 114, 116, (int)252, (int)232, 55, 77, 88, 75, 5, 48, (int)180, (int)220, (int)218, 124, 97, (int)177, (int)184, (int)192, (int)205, 59, 54, 89, (int)152, 79, 6, 64, 29, (int)167, 155, 62, 14, (int)197, (int)181, 66, (int)142, (int)153, 91, (int)230, 43, 96, 110, 122, (int)187, (int)235, (int)209, (int)190, (int)241, 128, 50, 23, 53, 114, 43, 111, 106, 99, 15, (int)232, 115, 101, (int)210, (int)234, (int)245, (int)238, (int)164, 56, 123, 94, 125, (int)223, 97, (int)210, (int)151, 91, (int)204, 4, 72, (int)140, 41, (int)143, 19, 93, (int)212, (int)153, 102, 182, (int)243, 102, 93, (int)214, 32, 68, (int)236, (int)146, 92, (int)168, 99, 46, (int)150, (int)249, 34, (int)177, (int)203, 105, 126, 129, 43, (int)156, (int)166, 3, (int)168, 43, 81, (int)183, 131, (int)168, 111, 131, 157, 155, (int)195, (int)195, (int)177, 47, (int)180, 82, 61, (int)225, 62, (int)150, (int)176, (int)212, (int)191, 129, 117, 98, 72, (int)173, (int)192, 36, (int)203, 15, (int)224, (int)254, 52, 127, (int)174, (int)231, 38, (int)213, (int)239, 120, 52, (int)178, 101, 97, 132, 130, (int)144, (int)152, (int)251, (int)226, 90, 18, (int)233, 74, 41, 88, 28, 17, 58, (int)177, 84, (int)226, 119, (int)241, 25, (int)192, 7, (int)157, 125, (int)170, 188, (int)191, (int)186, 75, 97, (int)225, 115, 184, 100, (int)168, (int)133, 0, (int)220, 95, (int)160, (int)242, 14, (int)185, (int)219, (int)214, 108, (int)157, (int)142, 32, (int)135, 69, 86, 64, 90, (int)236, (int)179, 137, 64, 128, (int)214, 63, 132, 152, 177, 167, 158, 8, 122, 139, 89, 115, 11, 27, 85, 94, 45, 12, (int)164, 18, (int)169, (int)213, 74, (int)196, 61, 55, 60, (int)238, 33, 77, (int)181, 88, (int)166, 61, 96, 152, 139, 209, 42, (int)223, (int)203, (int)149, 25, 93, 71, 132, 40, 77, 31, (int)187, (int)168, 88, (int)210, 106, (int)251, (int)181, 29, 15, (int)158, (int)194, (int)183, (int)176, (int)230, 91, 2, 124, (int)174, 86, (int)165, 57, 108, (int)191, (int)227, 106, (int)164, (int)159, 110, 35, (int)205, (int)248, (int)254, 105, 129, 25, 77, 6, (int)164, 93, 176, (int)192, (int)205, 26, 96, 109, (int)191, 35, (int)239, 46, 124, 53, (int)208, (int)221, 175, (int)169, (int)246, 68, (int)228, (int)158, 39, (int)221, 66, (int)234, (int)170, (int)154, 6, (int)192, 132, 25, 6, (int)168, (int)169, 26, (int)251, (int)183, 23, (int)204, (int)192, 34, 96, 126, 20, (int)183, 135, 20, (int)223, 115, 137, (int)254, (int)247, 13, 71, 7, (int)176, (int)162, (int)184, (int)184, (int)255, 128, (int)229, (int)236, 107, 42, 80, 68, 112, 127, 4, 57, 89, 26, 78, (int)251, (int)177, 21, 151, (int)224, 26, (int)227, 112, 78, (int)240, 11, (int)247, 87, 103 };
    private static final char[] HEXALPHABET_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    // Instance fields
    private int i;
    private int j;
    private int[] mTable;

    // Constructor
    public HabboHexRC4(String sPublicKey) {
        int iKeyHash = calculateKeyHash(sPublicKey);
        initialize(iKeyHash);
    }

    // Methods
    public void initialize(int iKeyHash) {
        // Reset indexes and table
        this.i = 0;
        this.j = 0;
        mTable = new int[256];

        int iKeyLength = (iKeyHash & 0xf8) / 8;
        if (iKeyLength < 20)
            iKeyLength += 20;
        int iKeyOffset = iKeyHash % PRIVATE_KEY.length;
        int iKeySkip = 0;
        int iPrevKey = 0;
        int m = 2;

        int[] w = new int[iKeyLength];
        for (int a = 0; a < iKeyLength; a++) {
            iKeySkip = iPrevKey % 29 - a % 6;
            m *= -1;
            w[a] = PRIVATE_KEY[(iKeyOffset + a * m + iKeySkip) % PRIVATE_KEY.length];
            iPrevKey = w[a];
        }

        int[] bzKey = new int[256];
        for (int a = 0; a <= 255; a++) {
            bzKey[a] = w[a % iKeyLength];
            mTable[a] = (int)a;
        }

        int b = 0;
        for (int a = 0; a <= 255; a++) {
            b = (int)((b + mTable[a] + bzKey[a]) % 256);

            // Swap table[a] and table[b]
            int bSwap = mTable[a];
            mTable[a] = mTable[b];
            mTable[b] = bSwap;
        }
    }

    public int mixTable() {
        // Re-calculate table fields
        this.i = (int)((this.i + 1) % 256);
        this.j = (int)((j + mTable[i]) % 256);

        // Swap table fields
        int bSwap = mTable[this.i];
        mTable[this.i] = mTable[this.j];
        mTable[this.j] = bSwap;

        return mTable[(mTable[this.i] + mTable[this.j]) % 256];
    }

    public int[] encipher(int[] Data) {
        int[] Result = new int[Data.length * 2];

        for (int a = 0, b = 0; a < Data.length; a++, b += 2) {
            int k = mixTable();
            int c = Data[a] & 0xff ^ k;
            if (c > 0) {
                Result[b] = (int)HEXALPHABET_CHARS[c >> 4 & 0xf];
                Result[b + 1] = (int)HEXALPHABET_CHARS[c & 0xf];
            }
        }

        return Result;
    }

    public String encipher(String sData) {
        StringBuilder sbResult = new StringBuilder(sData.length() * 2);
        for (int a = 0; a < sData.length(); a++) {
            int k = mixTable();
            int c = sData.charAt(a) & 0xff ^ k;
            if (c > 0) {
                sbResult.append(HEXALPHABET_CHARS[c >> 4 & 0xf]);
                sbResult.append(HEXALPHABET_CHARS[c & 0xf]);
            } else {
                sbResult.append("00");
            }
        }

        return sbResult.toString();
    }

    public int[] decipher(int[] Data, int Length) throws HabboRc4Exception {
        if (Length % 2 != 0)
            throw new HabboRc4Exception("Invalid input data, input data is not hexadecimal.");

        int[] Result = new int[Length / 2];
        for (int a = 0, b = 0; a < Length; a += 2, b++) {
            int c = convertTwoHexintsToint((int)Data[a], (int)Data[a + 1]);

            Result[b] = (int)(c ^ mixTable());
        }

        return Result;
    }

    public String decipher(String sData) throws HabboRc4Exception {
        if (sData.length() % 2 != 0)
            throw new HabboRc4Exception("Invalid input data, input data is not hexadecimal.");

        StringBuilder sbResult = new StringBuilder(sData.length());
        for (int a = 0, b = 0; a < sData.length(); a += 2, b++) {
            int c = convertTwoHexintsToint((int)sData.charAt(a), (int)sData.charAt(a + 1));

            sbResult.append((char)(c ^ mixTable()));
        }

        return sbResult.toString();
    }

    // Static methods
    public static String generatePublicKeyString() {
        int keyLength = new Random(System.currentTimeMillis()).nextInt(64 - 52) + 52;
        Random v = new Random(System.currentTimeMillis() + System.currentTimeMillis() / 1000 + keyLength);
        StringBuilder sb = new StringBuilder(keyLength);

        for (int i = 0; i < keyLength; i++) {
            int j = 0;
            if (v.nextInt(2) == 1)
                j = v.nextInt(123 - 97) + 97;
            else
                j = v.nextInt(58 - 48) + 48;
            sb.append((char)j);
        }

        return sb.toString();
    }

    public static int calculateKeyHash(String sPublicKey) {
        int iHash = 0;
        String sTable = sPublicKey.substring(0, sPublicKey.length() / 2);
        String sKey = sPublicKey.substring(sPublicKey.length() / 2);

        for (int i = 0; i < sTable.length(); i++) {
            int iIndex = sTable.indexOf(sKey.charAt(i));
            if (iIndex % 2 == 0)
                iIndex *= 2;
            if (i % 3 == 0)
                iIndex *= 3;
            if (iIndex < 0)
                iIndex = sTable.length() % 2;

            iHash += iIndex;
            iHash ^= iIndex << (i % 3) * 8;
        }

        return iHash;
    }

    private static int convertTwoHexintsToint(int A, int B) {
        int C = 0; // The output value
        int D = 0; // Counter used for determining hex value

        while (D < HEXALPHABET_CHARS.length) {
            if (HEXALPHABET_CHARS[D] == (A & 0xff)) {
                C = (D << 4);
                break;
            }
            D++;
        }

        D = 0;
        while (D < HEXALPHABET_CHARS.length) {
            if (HEXALPHABET_CHARS[D] == (B & 0xff)) {
                C += D;
                break;
            }
            D++;
        }

        return (int)C;
    }
}