
package com.win.ipdirection.headers;

import com.win.ipdirection.Enums;

import java.net.Inet4Address;
import java.nio.ByteBuffer;

import static com.win.ipdirection.Util.unsigned;
import static com.win.ipdirection.Util.zeroPadArray;
import static com.win.ipdirection.Enums.*;

public class Ipv4 extends Ip<Inet4Address> {

    public Ipv4(ByteBuffer raw) {
        super(raw);
        setSrcAddrOffset(12);
        setDstAddrOffset(16);
        setAddrLen(4);
    }

    @Override
    public int getHeaderLength() {
        return getIHL() * 4;
    }

    public void setVersion(int version) {
        raw.put(0, (byte) ((version << 4) | getIHL()));
    }

    public int getIHL() {
        return (raw.get(0) & 0x0F);
    }

    public void setIHL(int length) {
        byte first = (byte) (getVersion() << 4);
        byte second = (byte) (length & 0x0F);
        raw.put(0, (byte) (first | second));
    }


    public int getTotalLength() {
        return unsigned(raw.getShort(2));
    }

    public void setTotalLength(int length) {
        raw.putShort(2, (short) length);
    }

    public int getTTL() {
        return unsigned(raw.get(8));
    }

    public void setTTL(int ttl) {
        raw.put(8, (byte) ttl);
    }

    @Override
    public Protocol getNextHeaderProtocol() {
        return getProtocol();
    }

    public Protocol getProtocol() {
        return Protocol.fromValue(raw.get(9));
    }

    public void setProtocol(Enums.Protocol protocol) {
        raw.put(9, (byte) protocol.getValue());
    }

    public int getChecksum() {
        return unsigned(raw.getShort(10));
    }

    public void setChecksum(int cksum) {
        raw.putShort(10, (short) cksum);
    }

    public boolean is(Flag flag) {
        return getFlag(6, flag.ordinal() + 5);
    }

    public void set(Flag flag, boolean value) {
        setFlag(6, flag.ordinal() + 5, value);
    }


    public byte[] getOptions() {
        if (getHeaderLength() - 20 > 0)
            return getBytesAtOffset(20, getHeaderLength() - 20);
        return null;
    }

    public void setOptions(byte[] options) {
        int delta = getHeaderLength() - 20;
        if (delta <= 0) {
            throw new IllegalStateException("Packet is too short for options.");
        }
        setBytesAtOffset(20, delta, zeroPadArray(options, delta));
    }

    public int getID() {
        return unsigned(raw.getShort(4));
    }

    public void setID(int id) {
        raw.putShort(4, (short) id);
    }

    /**
     * Gets flags as a bitmask
     *
     * @return The bitmask representing flags
     */
    public int getFlags() {
        return ((raw.get(6) & 0x00FF) >> 5) & 0x00FF;
    }

    /**
     * Sets flags with a bitmask
     *
     * @param flags The bitmask representing flags
     */
    public void setFlags(int flags) {
        byte first = (byte) (flags << 5);
        byte second = (byte) (getFragmentOffset() & 0xFF00);
        raw.put(6, (byte) (first | second));
    }

    public int getFragmentOffset() {
        return raw.getShort(6) & 0x00001FFF;
    }

    public void setFragmentOffset(int fragOff) {
        int first = getFlags() << 13;
        int second = fragOff & 0x00001FFF;
        raw.putShort(6, (short) (first | second));
    }

    public int getDSCP() {
        return (raw.get(1) & 0xFC) >> 2;
    }

    public void setDSCP(int dscp) {
        raw.put(1, (byte) ((dscp << 2) | getECN()));
    }

    public int getECN() {
        return raw.get(1) & 0x03;
    }

    public void setECN(int ecn) {
        raw.put(1, (byte) ((getDSCP() << 2) | (ecn & 0x03)));
    }

    public int getDiffServ() {
        return getDSCP();
    }

    public void setDiffServ(int diffServ) {
        setDSCP(diffServ);
    }

    public int getTOS() {
        return raw.get(1) & 0x00FF;
    }

    public void setTOS(int tos) {
        raw.put(1, (byte) tos);
    }

    @Override
    public String toString() {
        StringBuilder flags = new StringBuilder();
        for (Flag flag : Flag.values()) {
            flags.append(flag.name()).append("=").append(is(flag)).append(", ");
        }
        return String.format("IPv4 {version=%d, srcAddr=%s, dstAddr=%s, IHL=%d, DSCP=%d, ECN=%d, length=%d, ID=%s " +
                        "%s" +
                        "fragOff=%d TTL=%d " +
                        "proto=%s, cksum=%s}"
                , getVersion()
                , getSrcAddrStr()
                , getDstAddrStr()
                , getIHL()
                , getDSCP()
                , getECN()
                , getTotalLength()
                , Integer.toHexString(getID())
                , flags
                , getFragmentOffset()
                , getTTL()
                , getProtocol()
                , Integer.toHexString(getChecksum())
        );
    }

    public enum Flag {
        RESERVED, DF, MF
    }
}
