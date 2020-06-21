package com.win.ipdirection;

import com.sun.jna.Memory;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.win.ipdirection.exceptions.WinDivertException;
import com.win.ipdirection.windivert.WinDivertAddress;
import com.win.ipdirection.windivert.WinDivertDLL;

import java.util.Arrays;
import java.util.List;


import static com.sun.jna.platform.win32.WinNT.HANDLE;
import static com.win.ipdirection.Enums.*;
import static com.win.ipdirection.exceptions.WinDivertException.throwExceptionOnGetLastError;

/**
 * A WinDivert handle that can be used to capture packets.<p>
 */
public class WinDivert {
    public static int DEFAULT_PACKET_BUFFER_SIZE = 1500;
    private WinDivertDLL dll = WinDivertDLL.INSTANCE;
    private String filter;
    private Layer layer;
    private int priority;
    private int flags;
    private HANDLE handle;

    /**
     * Create a new WinDivert instance based upon the given filter for
     */
    public WinDivert(String filter) {
        this(filter, Layer.NETWORK, 0, Flag.DEFAULT);
    }


    /**
     * Create a new WinDivert instance based upon the given parameters
     *
     */
    public WinDivert(String filter, Layer layer, int priority, Flag... flags) {
        this.filter = filter;
        this.layer = layer;
        this.priority = priority;
        this.flags = 0;
        List<Flag> flagList = Arrays.asList(flags);
        if (flagList.contains(Flag.SNIFF) && flagList.contains(Flag.DROP)) {
            throw new IllegalArgumentException(String.format("A filter cannot be set with flags %s and %s at same time.", Flag.SNIFF, Flag.DROP));
        }
        for (Flag flag : flags) {
            this.flags |= flag.getValue();
        }

    }

    /**
     * Opens a WinDivert handle for the given filter.<br>
     * Unless otherwise specified by flags, any packet that matches the filter will be diverted to the handle.<br>
     * Diverted packets can be read by the application with {@link #recv() recv}.
     */
    public WinDivert open() throws WinDivertException {
        if (isOpen()) {
            throw new IllegalStateException("The instance is already in open state");
        }
        handle = dll.WinDivertOpen(filter, layer.getValue(), (short) priority, flags);
        throwExceptionOnGetLastError();
        //Allow call chaining
        return this;
    }

    /**
     * Indicates if there is currently an open handle.
     */
    public boolean isOpen() {
        return handle != null;
    }

    /**
     * Closes the handle opened by {@link #open() open}.
     */
    public void close() {
        if (isOpen()) {
            dll.WinDivertClose(handle);
            handle = null;
        }
    }

    /**
     * Receives a diverted packet that matched the filter.<br>
     */
    public Packet recv() throws WinDivertException {
        return recv(DEFAULT_PACKET_BUFFER_SIZE);
    }

    /**
     * Receives a diverted packet that matched the filter.<br>
     * The return value is a {@link Packet packet}.
     */
    public Packet recv(int bufsize) throws WinDivertException {
        WinDivertAddress address = new WinDivertAddress();
        Memory buffer = new Memory(bufsize);
        IntByReference recvLen = new IntByReference();
        dll.WinDivertRecv(handle, buffer, bufsize, address.getPointer(), recvLen);
        throwExceptionOnGetLastError();
        byte[] raw = buffer.getByteArray(0, recvLen.getValue());
        return new Packet(raw, address);
    }

    /**
     * Injects a packet into the headers stack.<br>
     * Recalculates the checksum before sending.<br>
     * The return value is the number of bytes actually sent.<br>
     */
    public int send(Packet packet) throws WinDivertException {
        return send(packet, true);
    }

    /**
     * Injects a packet into the headers stack.<br>
     */
    public int send(Packet packet, boolean recalculateChecksum, CalcChecksumsOption... options) throws WinDivertException {
        if (recalculateChecksum) {
            packet.recalculateChecksum(options);
        }
        IntByReference sendLen = new IntByReference();
        byte[] raw = packet.getRaw();
        Memory buffer = new Memory(raw.length);

        buffer.write(0, raw, 0, raw.length);
        dll.WinDivertSend(handle, buffer, raw.length, packet.getWinDivertAddress().getPointer(), sendLen);
        throwExceptionOnGetLastError();
        return sendLen.getValue();
    }

    /**
     * Get a WinDivert parameter.
     */
    public long getParam(Param param) {
        if (!isOpen()) {
            throw new IllegalStateException("WinDivert handle not in OPEN state");
        }
        LongByReference value = new LongByReference();
        dll.WinDivertGetParam(handle, param.getValue(), value);
        return value.getValue();
    }

    /**
     * Set a WinDivert parameter
     */
    public void setParam(Param param, long value) {
        if (!isOpen()) {
            throw new IllegalStateException("WinDivert handle not in OPEN state");
        }
        if (param.getMin() > value || param.getMax() < value) {
            throw new IllegalArgumentException(String.format("%s must be in range %d, %d", param, param.getMin(), param.getMax()));
        }
        dll.WinDivertSetParam(handle, param.getValue(), value);
    }

    /**
     * Checks if the given flag is see
     */
    public boolean is(Flag flag) {
        return (flag.getValue() & flags) == flag.getValue();
    }

    /**
     * Returns the operational mode as a String
     */
    public String getMode() {
        StringBuilder mode = new StringBuilder();
        for (Flag flag : Flag.values()) {
            if (is(flag)) {
                mode.append(flag).append("|");
            }
        }
        mode.deleteCharAt(mode.length() - 1);
        return mode.toString();
    }

    @Override
    public String toString() {

        return String.format("WinDivert{handle=%s, dll=%s, filter=%s, layer=%s, priority=%d, mode=%s, state=%s}"
                , handle
                , dll
                , filter
                , layer
                , priority
                , getMode()
                , isOpen() ? "OPEN" : "CLOSED"
        );
    }
}
