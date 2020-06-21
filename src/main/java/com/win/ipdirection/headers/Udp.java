package com.win.ipdirection.headers;

import java.nio.ByteBuffer;

import static com.win.ipdirection.Util.printHexBinary;
import static com.win.ipdirection.Util.unsigned;

public class Udp extends Transport {

    public Udp(ByteBuffer raw, int start) {
        super(raw, start);
    }

    public int getLength() {
        return unsigned(raw.getShort(start + 4));
    }

    public void setLength(int length) {
        raw.putShort(start + 4, (short) length);
    }

    public int getChecksum() {
        return unsigned(raw.getShort(start + 6));
    }

    public void setChecksum(int cksum) {
        raw.putShort(start + 6, (short) cksum);
    }

    @Override
    public int getHeaderLength() {
        return 8;
    }

    public byte[] getData() {
        return getBytesAtOffset(start + getHeaderLength(), getLength() - getHeaderLength());
    }

    public void setData(byte[] data) {
        setBytesAtOffset(start + getHeaderLength(), data.length, data);
    }

    @Override
    public String toString() {
        return String.format("UDP {srcPort=%d, dstPort=%d, len=%d, cksum=%s, data=%s}"
                , getSrcPort()
                , getDstPort()
                , getLength()
                , Integer.toHexString(getChecksum())
                , printHexBinary(getData())
        );
    }
}
