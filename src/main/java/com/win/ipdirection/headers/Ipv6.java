
package com.win.ipdirection.headers;

import java.net.Inet6Address;
import java.nio.ByteBuffer;

import static com.win.ipdirection.Enums.Protocol;
import static com.win.ipdirection.Util.unsigned;

public class Ipv6 extends Ip<Inet6Address> {


    public Ipv6(ByteBuffer raw) {
        super(raw);
        setSrcAddrOffset(8);
        setDstAddrOffset(24);
        setAddrLen(16);
    }

    @Override
    public int getHeaderLength() {
        return 40;
    }

    public void setVersion(int version) {
        raw.put(0, (byte) ((version << 4)));
    }

    @Override
    public Protocol getNextHeaderProtocol() {
        return getNextHeader();
    }

    public int getPayloadLength() {
        return unsigned(raw.getShort(4));
    }

    public void setPayloadLength(short length) {
        raw.putShort(4, length);
    }

    public Protocol getNextHeader() {
        return Protocol.fromValue(raw.get(6));
    }

    public void setNextHeader(Protocol protocol) {
        raw.put(6, (byte) protocol.getValue());
    }

    public int getHopLimit() {
        return raw.get(7);
    }

    public void setHopLimit(int hopLimit) {
        raw.put(7, (byte) hopLimit);
    }

    @Override
    public String toString() {
        return String.format("IPv6 {version=%d, srcAddr=%s, dstAddr=%s, payloadLength=%d, nextHeader=%s, hopLimit=%d}"
                , getVersion()
                , getSrcAddrStr()
                , getDstAddrStr()
                , getPayloadLength()
                , getNextHeader()
                , getHopLimit()
        );
    }
}
