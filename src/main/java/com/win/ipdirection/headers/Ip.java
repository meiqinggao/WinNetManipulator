
package com.win.ipdirection.headers;

import com.win.ipdirection.Enums;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import static com.win.ipdirection.Enums.Protocol;


public abstract class Ip<T extends InetAddress> extends Header {

    private int srcAddrOffset = 12;
    private int dstAddrOffset = 16;
    private int addrLen = 4;

    public Ip(ByteBuffer raw) {
        super(raw);
    }

    public static int getVersion(ByteBuffer raw) {
        return raw.get(0) >> 4;
    }

    public T getInetAddressAtOffset(int offset) throws UnknownHostException {
        return (T) InetAddress.getByAddress(getBytesAtOffset(offset, addrLen));
    }

    public void setInetAddressAtOffset(int offset, T address) {
        byte[] addressBytes = address.getAddress();
        setBytesAtOffset(offset, addressBytes.length, addressBytes);
    }

    public T getSrcAddr() {
        try {
            return getInetAddressAtOffset(srcAddrOffset);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public void setSrcAddr(T address) {
        setInetAddressAtOffset(srcAddrOffset, address);
    }

    public T getDstAddr() {
        try {
            return getInetAddressAtOffset(dstAddrOffset);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public void setDstAddr(T address) {
        setInetAddressAtOffset(dstAddrOffset, address);
    }

    public String getSrcAddrStr() {
        InetAddress address = getSrcAddr();
        return address != null ? address.getHostAddress() : null;
    }

    public void setSrcAddrStr(String srcAddr) throws UnknownHostException {
        setSrcAddr((T) InetAddress.getByName(srcAddr));
    }

    public String getDstAddrStr() {
        InetAddress address = getDstAddr();
        return address != null ? address.getHostAddress() : null;
    }

    public void setDstAddrStr(String dstAddr) throws UnknownHostException {
        setDstAddr((T) InetAddress.getByName(dstAddr));
    }

    public int getVersion() {
        return raw.get(0) >> 4;
    }

    public abstract Protocol getNextHeaderProtocol();

    /**
     * Set the source address field offset.
     * Only to ease test writing.
     *
     * @param srcAddrOffset The offset of the source address field.
     */
    public void setSrcAddrOffset(int srcAddrOffset) {
        this.srcAddrOffset = srcAddrOffset;
    }

    /**
     * Set the destination address field offset.
     * Only to ease test writing.
     *
     * @param dstAddrOffset The offset of the destination address field.
     */
    public void setDstAddrOffset(int dstAddrOffset) {
        this.dstAddrOffset = dstAddrOffset;
    }

    /**
     * Set the length of address fields.
     * Only to ease test writing.
     *
     * @param addrLen The length address field.
     */
    public void setAddrLen(int addrLen) {
        this.addrLen = addrLen;
    }
}
