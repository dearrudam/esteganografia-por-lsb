package io.github.dearrudam.esteganografiaporlsb.model;

import io.github.dearrudam.esteganografiaporlsb.utils.ByteUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class Decodificador {
    public String decodifica(Path arquivoComAMensagem) {
        var geradorDeMensagem = new LeitorDeMensagem();

        try (
                var input = new FileInputStream(arquivoComAMensagem.toFile())
        ) {

            int byteAtual = 0;
            int posAtual = 0;
            int byteInicial = -1;
            while ((byteAtual = input.read()) > -1) {
                if (posAtual == 10) {
                    byteInicial = byteAtual;
                }
                if (posAtual >= 10 && byteInicial > -1 && posAtual >= byteInicial) {
                    int[] bits = ByteUtils.toBits(byteAtual);
                    geradorDeMensagem.adicionarBit(bits[bits.length - 1]);
                }
                posAtual++;
            }


        } catch (IOException e) {
            throw new RuntimeException("falha ao ler o arquivo" + e.getMessage(), e);
        }


        return geradorDeMensagem.pegarMensagem();
    }

}
