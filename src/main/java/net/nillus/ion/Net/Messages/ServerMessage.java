package net.nillus.ion.Net.Messages;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import net.nillus.ion.IonEnvironment;
import net.nillus.ion.Specialized.Encoding.Base64Encoding;
import net.nillus.ion.Specialized.Encoding.WireEncoding;

public class ServerMessage implements IHabboMessage {
    // Fields
    private int mID;
    private List<Byte> mContent;

    // Properties
    public int getID() {
        return mID;
    }

    public String getHeader() {
        byte[] headerBytes = Base64Encoding.EncodeInt32(mID, 2);
        return new String(headerBytes, Charset.forName("UTF-8"));
    }

    public int getContentLength() {
        return mContent.size();
    }

    // Constructors
    public ServerMessage() {
        // Requires a call to Initialize before usage
    }

    public ServerMessage(int ID) {
        initialize(ID);
    }

    // Methods
    public void initialize(int ID) {
        mID = ID;
        mContent = new ArrayList<>();
    }

    public void clear() {
        mContent.clear();
    }

    public String getContentString() {
        byte[] contentBytes = new byte[mContent.size()];
        for (int i = 0; i < mContent.size(); i++) {
            contentBytes[i] = mContent.get(i);
        }
        return new String(contentBytes, Charset.forName("UTF-8"));
    }

    public void append(byte b) {
        mContent.add(b);
    }

    public void append(byte[] bzData) {
        if (bzData != null && bzData.length > 0) {
            for (byte data : bzData) {
                mContent.add(data);
            }
        }
    }

    public void append(String s) {
        append(s, null);
    }

    public void append(String s, Charset pEncoding) {
        if (s != null && !s.isEmpty()) {
            if (pEncoding == null) {
                pEncoding = IonEnvironment.getDefaultTextEncoding();
            }
            append(new String(s.getBytes()), pEncoding);
        }
    }

    public void append(int i) {
        append(String.valueOf(i), Charset.forName("ASCII"));
    }

    public void append(long i) {
        append((int) i);
    }

    public void appendBoolean(boolean b) {
        mContent.add(b ? WireEncoding.POSITIVE : WireEncoding.NEGATIVE);
    }

    public void appendInt32(int i) {
        byte[] encoded = WireEncoding.EncodeInt32(i);
        for (byte data : encoded) {
            mContent.add(data);
        }
    }

    public void appendUInt32(long i) {
        appendInt32((int) i);
    }

    public void appendString(String s) {
        appendString(s,(byte) 2);
    }

    public void appendString(String s, byte breaker) {
        append(s); // Append string with default encoding
        append(breaker); // Append breaker
    }

    public byte[] getBytes() {
        byte[] Data = new byte[getContentLength() + 3];
        
        byte[] Header = Base64Encoding.EncodeInt32(mID, 2);
        Data[0] = Header[0];
        Data[1] = Header[1];

        for (int i = 0; i < getContentLength(); i++) {
            Data[i + 2] = mContent.get(i); 
        }

        Data[Data.length - 1] = 1;

        return Data;
    }
}