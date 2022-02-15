package io.github.dearrudam.esteganografiaporlsb;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class Codificador {

    public void codifique(String mensagem, Path imagemBase, Path imagemDeDestino) throws IOException {

        try (FileInputStream input = new FileInputStream(imagemBase.toFile());
             FileOutputStream output = new FileOutputStream(imagemDeDestino.toFile())) {

            int byteDeInicio = -1;
            int byteAtual = 0;
            int pos = 0;
            int[] bitsDaMensagem = toArrayDeBits(mensagem);
            int posBitsDaMensagem = 0;
            while ((byteAtual = input.read()) > -1) {
                if (pos == 10) {
                    byteDeInicio = ((byte) byteAtual & 0xFF);
                }
                if (byteDeInicio > -1 && pos >= byteDeInicio) {
                    if (posBitsDaMensagem < bitsDaMensagem.length) {
                        int[] bitsDoByteAtual = toBits(byteAtual);
                        bitsDoByteAtual[bitsDoByteAtual.length - 1] = bitsDaMensagem[posBitsDaMensagem];
                        int novoByte = bitsDoByteAtual[7];
                        novoByte += bitsDoByteAtual[6] * 2;
                        novoByte += bitsDoByteAtual[5] * 4;
                        novoByte += bitsDoByteAtual[4] * 8;
                        novoByte += bitsDoByteAtual[3] * 16;
                        novoByte += bitsDoByteAtual[2] * 32;
                        novoByte += bitsDoByteAtual[1] * 64;
                        novoByte += bitsDoByteAtual[0] * 128;
                        byteAtual = novoByte;
                        posBitsDaMensagem++;
                    }
                }
                output.write(byteAtual);
                pos++;
            }
        }
    }

    private int[] toArrayDeBits(String texto) {
        byte[] textoBytes = texto.getBytes(StandardCharsets.UTF_8);
        var arrayDeBits = new int[textoBytes.length * 8];
        for (int i = 0; i < textoBytes.length; i++) {
            var bits = toBits(textoBytes[i]);
            for (int y = 0; y < bits.length; y++) {
                arrayDeBits[(8 * i) + y] = bits[y];
            }
        }
        return arrayDeBits;
    }

    private int[] toBits(int input) {
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
