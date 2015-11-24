package org.azure.utilities;

public class HabboEncoding {
    public static int decodeInt(byte[] v) {
        if ((v[0] | v[1] | v[2] | v[3]) < 0)
            return -1;
        return ((v[0] << 24) + (v[1] << 16) + (v[2] << 8) + (v[3]));
    }

    public static short decodeShort(byte[] v) {
        if ((v[0] | v[1]) < 0)
            return -1;
        int result = ((v[0] << 8) + (v[1]));
        return (short) result;
    }
}
