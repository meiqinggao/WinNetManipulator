
package com.win.ipdirection.headers;

import java.nio.ByteBuffer;

import static com.win.ipdirection.Util.unsigned;

public class Icmp extends Header {

    public Icmp(ByteBuffer raw, int start) {
        super(raw, start);
    }

    public byte getType() {
        return raw.get(start);
    }

    public void setType(byte type) {
        raw.put(start, type);
    }

    public byte getCode() {
        return raw.get(start + 1);
    }

    public void setCode(byte code) {
        raw.put(start + 1, code);
    }

    public int getChecksum() {
        return unsigned(raw.getShort(start + 2));
    }

    public void setChecksum(int cksum) {
        raw.putShort(start + 2, (short) cksum);
    }


    @Override
    public int getHeaderLength() {
        return 4;
    }
}
