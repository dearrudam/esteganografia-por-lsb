package io.github.dearrudam.esteganografiaporlsb;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.Queue;

public class LeitorDeMensagem {

    private final static int PONTO_FINAL = '.';
    private boolean terminou = false;
    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private LinkedList<Integer> byteAtual = new LinkedList<>();

    public void adicionarBit(int bit) {
        if (terminou)
            return;
        byteAtual.add(bit);
        if (byteAtual.size() % 8 == 0) {
            int novoByte = lerNovoByte(byteAtual);
            terminou = (PONTO_FINAL == novoByte);
            output.write(novoByte);
        }
    }

    private int lerNovoByte(final Queue<Integer> bits) {
        int novoByte = 0;
        for (var pos = 7; pos >= 0; pos--) {
            novoByte += bits.poll() * ((int) Math.pow(2, pos));
        }
        return novoByte;
    }

    public String pegarMensagem() {
        if (!byteAtual.isEmpty()) {
            while (byteAtual.size() < 8) {
                byteAtual.add(0);
            }
            int novoByte = lerNovoByte(byteAtual);
            output.write(novoByte);
        }
        return output.toString();
    }

}
