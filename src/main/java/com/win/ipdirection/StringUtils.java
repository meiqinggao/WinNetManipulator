package com.win.ipdirection;

public class StringUtils {
    public static String prependZero(String hexString) {
        int hexLen = hexString.length();
        if (hexLen >= 4) {
            return hexString;
        }
        StringBuilder preZero = new StringBuilder();
        int count = 4 - hexLen;
        while (count > 0) {
            preZero.insert(0, "0");
            count--;
        }
        preZero.append(hexString);
        return preZero.toString();
    }

    public static String hexToAscii(String hexStr) {

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < hexStr.length(); i += 2) {

            String str = hexStr.substring(i, i + 2);

            output.append((char) Integer.parseInt(str, 16));

        }

        return output.toString();
    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }



    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

}
