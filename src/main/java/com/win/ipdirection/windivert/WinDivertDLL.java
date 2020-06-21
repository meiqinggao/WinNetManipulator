package com.win.ipdirection.windivert;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

import static com.sun.jna.platform.win32.WinDef.BOOL;
import static com.sun.jna.platform.win32.WinNT.HANDLE;

/**
 * <p>
 * DLL methods cannot declare "throws LastErrorException since 997 (Overlapped I/O is in progress) will be considered as
 * an error and will interrupt the call.
 * </p>
 */
public interface WinDivertDLL extends Library {
//    WinDivertDLL INSTANCE = DeployHandler.deploy();
//    WinDivertDLL INSTANCE = (WinDivertDLL) Native.loadLibrary(Platform.is64Bit() ? "WinDivert64" : "WinDivert32", WinDivertDLL.class);
    WinDivertDLL INSTANCE = Native.loadLibrary(Platform.is64Bit() ? "WinDivert64" : "WinDivert32", WinDivertDLL.class);

    HANDLE WinDivertOpen(
            String filter,
            int layer,
            short priority,
            long flags
    );

    BOOL WinDivertRecv(
            HANDLE handle,
            Pointer pPacket,
            int packetLen,
            Pointer pAddr,
            IntByReference recvLen
    );

    BOOL WinDivertSend(
            HANDLE handle,
            Pointer pPacket,
            int packetLen,
            Pointer pAddr,
            IntByReference sendLen
    );

    BOOL WinDivertSetParam(
            HANDLE handle,
            int param,
            long value);

    BOOL WinDivertGetParam(
            HANDLE handle,
            int param,
            LongByReference pValue);

    BOOL WinDivertClose(
            HANDLE handle
    );

    int WinDivertHelperCalcChecksums(
            Pointer pPacket,
            int packetLen,
            long flags
    );
}
