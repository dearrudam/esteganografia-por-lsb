package io.github.dearrudam.esteganografiaporlsb.utils;

import java.nio.charset.StandardCharsets;

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

    public static int[] toBits(String texto) {
        byte[] textoBytes = texto.getBytes(StandardCharsets.UTF_8);
        var arrayDeBits = new int[textoBytes.length * 8];
        for (int i = 0; i < textoBytes.length; i++) {
            var bits = ByteUtils.toBits(textoBytes[i]);
            for (int y = 0; y < bits.length; y++) {
                arrayDeBits[(8 * i) + y] = bits[y];
            }
        }
        return arrayDeBits;
    }

}
