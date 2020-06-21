package com.win.ipdirection.exceptions;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32Util;

public class WinDivertException extends Exception {
    protected int code;
    protected String message;
    protected LastErrorException lee;

    public WinDivertException(int code) {
        this(code, Kernel32Util.formatMessage(code));
    }

    public WinDivertException(int code, String message) {
        this(code, message, null);
    }

    public WinDivertException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * Throw a WinDivertException whenever GetLastError returned a code different from
     * <ul>
     * <li>0 (Success)</li>
     * <li>997 (Overlapped I/O is in progress)</li>
     *</ul>
     * @return The GetLastError code.
     * @throws WinDivertException When {@code GetLastError} returns a code different from 0 or 997, {@link WinDivertException} is thrown.
     */
    public static int throwExceptionOnGetLastError() throws WinDivertException {
        int lastError = Native.getLastError();
        if (lastError != 0 && lastError != 997) {
            throw new WinDivertException(lastError);
        }
        return lastError;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "WinDivertException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
