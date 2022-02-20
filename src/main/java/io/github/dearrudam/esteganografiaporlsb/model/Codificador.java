package io.github.dearrudam.esteganografiaporlsb.model;

import io.github.dearrudam.esteganografiaporlsb.utils.ByteUtils;

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
            int[] bitsDaMensagem = ByteUtils.toBits(mensagem);
            int posBitsDaMensagem = 0;
            while ((byteAtual = input.read()) > -1) {
                if (pos == 10) {
                    byteDeInicio = ((byte) byteAtual & 0xFF);
                }
                if (byteDeInicio > -1 && pos >= byteDeInicio) {
                    if (posBitsDaMensagem < bitsDaMensagem.length) {
                        int[] bitsDoByteAtual = ByteUtils.toBits(byteAtual);
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
}
