package com.win.ipdirection.windivert;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the "address" of a captured or injected packet. The address includes the packet's headers interfaces and the packet direction.
 */
public class WinDivertAddress extends Structure {
    public WinDef.UINT IfIdx;
    public WinDef.UINT SubIfIdx;
    public WinDef.USHORT Direction;

    @Override
    protected List getFieldOrder() {
        return Arrays.asList("IfIdx",
                "SubIfIdx",
                "Direction");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WinDivertAddress that = (WinDivertAddress) o;

        return IfIdx.intValue() == that.IfIdx.intValue() &&
                SubIfIdx.intValue() == that.SubIfIdx.intValue() &&
                Direction.intValue() == that.Direction.intValue();

    }

    @Override
    public int hashCode() {
        int result = 31 * IfIdx.hashCode();
        result = 31 * result + SubIfIdx.hashCode();
        result = 31 * result + Direction.hashCode();
        return result;
    }
}