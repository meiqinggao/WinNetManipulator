
package com.win.ipdirection.headers;

import java.nio.ByteBuffer;

import static com.win.ipdirection.Util.printHexBinary;

public class Icmpv6 extends Icmp {

    public Icmpv6(ByteBuffer raw, int start) {
        super(raw, start);
    }

    public byte[] getMessageBody() {
        return getBytesAtOffset(start + 4, 4);
    }

    public void setMessageBody(byte[] messageBody) {
        setBytesAtOffset(start + 4, messageBody.length, messageBody);
    }

    @Override
    public String toString() {
        return String.format("ICMPv6 {type=%d, code=%d, cksum=%s, messageBody=%s}"
                , getType()
                , getCode()
                , Integer.toHexString(getChecksum())
                , printHexBinary(getMessageBody())
        );
    }
}
