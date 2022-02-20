package io.github.dearrudam.esteganografiaporlsb.utils;

public class ByteUtils {

    public static int[] toBits(int input) {

        var rawData = new int[8];
        int displayMask = 1 << 7;
        for (int idx = 1; idx <= 8; idx++) {
            if ((displayMask & input) == 0) {
                rawData[idx - 1] = 0;
            } else {
                rawData[idx - 1] = 1;
            }
            input <<= 1;
        }
        return rawData;
    }

}
