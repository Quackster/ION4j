package net.nillus.ion.Net.Messages;

import net.nillus.ion.IonEnvironment;
import net.nillus.ion.Specialized.Encoding.Base64Encoding;
import net.nillus.ion.Specialized.Encoding.WireEncoding;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class ClientMessage implements IHabboMessage {
    // Fields
    private final int mID;
    private final byte[] mContent;
    private int mContentCursor;

    // Properties
    public int getID() {
        return mID;
    }

    public String getHeader() {
        return new String(Base64Encoding.EncodeInt32(mID, 2), IonEnvironment.getDefaultTextEncoding());
    }

    public String getMethodName() {
        return String.format("TODO:GETMETHOD%d", mID);
    }

    public int getContentLength() {
        return mContent.length;
    }

    public int getRemainingContent() {
        return (mContent.length - mContentCursor);
    }

    // Constructors
    public ClientMessage(int ID, byte[] bzContent) {
        if (bzContent == null) {
            bzContent = new byte[0];
        }

        mID = ID;
        mContent = bzContent;
        mContentCursor = 0;
    }

    // Methods
    public void reset() {
        mContentCursor = 0;
    }

    public void advance(int n) {
        mContentCursor += n;
    }

    public String getContentString() {
        return new String(mContent, IonEnvironment.getDefaultTextEncoding());
    }

    private byte[] readBytes(int numBytes) {
        if (numBytes > this.getRemainingContent()) {
            numBytes = this.getRemainingContent();
        }

        byte[] bzData = new byte[numBytes];
        for (int x = 0; x < numBytes; x++) {
            bzData[x] = mContent[mContentCursor++];
        }

        return bzData;
    }

    private byte[] readBytesFreezeCursor(int numBytes) {
        if (numBytes > this.getRemainingContent()) {
            numBytes = this.getRemainingContent();
        }

        byte[] bzData = new byte[numBytes];
        for (int x = 0, y = mContentCursor; x < numBytes; x++, y++) {
            bzData[x] = mContent[y];
        }

        return bzData;
    }

    private byte[] readFixedValue() {
        int Length = Base64Encoding.decodeInt32(this.readBytes(2));
        return this.readBytes(Length);
    }

    public boolean popBase64Boolean() {
        return (this.getRemainingContent() > 0 && mContent[mContentCursor++] == Base64Encoding.POSITIVE);
    }

    public int popInt32() {
        return Base64Encoding.decodeInt32(this.readBytes(2));
    }

    public long popUInt32() {
        return (long) this.popInt32();
    }

    public String popFixedString(String pEncoding) {
        if (pEncoding == null) {
            pEncoding = StandardCharsets.UTF_8.name();
        }

        try {
            return new String(this.readFixedValue(), pEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String popFixedString() {
        String pEncoding = StandardCharsets.UTF_8.name();
        return this.popFixedString(pEncoding);
    }

    public int popFixedInt32() {
        int i;
        String s = this.popFixedString(StandardCharsets.US_ASCII.name());
        if (s == null || !s.matches("-?\\d+")) {
            i = 0; // Default value if parsing fails
        } else {
            i = Integer.parseInt(s);
        }

        return i;
    }

    public long popFixedUInt32() {
        return (long) this.popFixedInt32();
    }

    public boolean popWiredBoolean() {
        return (this.getRemainingContent() > 0 && mContent[mContentCursor++] == WireEncoding.POSITIVE);
    }

    public int popWiredInt32() {
        if (this.getRemainingContent() == 0) {
            return 0;
        }

        byte[] bzData = this.readBytesFreezeCursor(WireEncoding.MAX_INTEGER_BYTE_AMOUNT);
        int totalBytes = bzData[0] >> 3 & 7;
        int i = WireEncoding.DecodeInt32(bzData);
        mContentCursor += totalBytes;

        return i;
    }

    public long popWireduint() {
        return (long) this.popWiredInt32();
    }

    @Override
    public String toString() {
        return this.getHeader() + getContentString();
    }
}