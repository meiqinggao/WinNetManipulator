
package com.win.ipdirection.headers;

import java.nio.ByteBuffer;

import static com.win.ipdirection.Util.printHexBinary;

public class Icmpv4 extends Icmp {

    public Icmpv4(ByteBuffer raw, int start) {
        super(raw, start);
    }

    public byte[] getRestOfHeader() {
        return getBytesAtOffset(start + 4, 4);
    }

    public void setRestOfHeader(byte[] restOfHeader) {
        setBytesAtOffset(start + 4, restOfHeader.length, restOfHeader);
    }

    @Override
    public String toString() {
        return String.format("ICMPv4 {type=%d, code=%d, cksum=%s, restOfHdr=%s}"
                , getType()
                , getCode()
                , Integer.toHexString(getChecksum())
                , printHexBinary(getRestOfHeader())
        );
    }
}
