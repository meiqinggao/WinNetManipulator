
package com.win.ipdirection.headers;

import java.nio.ByteBuffer;

import static com.win.ipdirection.Util.unsigned;

public abstract class Transport extends Header {

    public Transport(ByteBuffer raw, int offset) {
        super(raw, offset);
    }

    public int getSrcPort() {
        return unsigned(raw.getShort(start));
    }

    public void setSrcPort(int port) {
        raw.putShort(start, (short) port);
    }

    public int getDstPort() {
        return unsigned(raw.getShort(start + 2));
    }

    public void setDstPort(int port) {
        raw.putShort(start + 2, (short) port);
    }

}
