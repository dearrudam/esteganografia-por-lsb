package io.github.dearrudam.esteganografiaporlsb;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EsteganografiaPorLsbApplication {

    public static void main(String[] args) throws IOException {
        Path arquivoComAMensagem = Path.of("3f41a.BMP")
//          criarArquivoComMensagem(
//                Path.of("MARBLES.BMP"),
//                "Olá mundo.")
                ;

        String mensagemEscrita = lerMensagemDoArquivo(arquivoComAMensagem);

        System.out.println(mensagemEscrita.equals("Olá mundo."));
    }

    private static void testandoTextoParaBitsEBitsParaTexto() {
        String x1 = intToStringBinaryFormat(-61);
        System.out.println(x1);

        int[] bits = toArrayDeBits("Olá.");

        byte[] dadoEsperado = "Olá.".getBytes(StandardCharsets.UTF_8);


        int[] subBits = new int[8];
        int subBitsCount = 0;
        var data = new byte[bits.length / 8];
        for (int x = 0; x < bits.length; x++) {
            if (subBitsCount <= 6) {
                subBits[subBitsCount] = bits[x];
                subBitsCount++;
            } else {
                subBits[subBitsCount] = bits[x];
                int byteRecuperado = subBits[7];
                byteRecuperado += subBits[6] * 2;
                byteRecuperado += subBits[5] * 4;
                byteRecuperado += subBits[4] * 8;
                byteRecuperado += subBits[3] * 16;
                byteRecuperado += subBits[2] * 32;
                byteRecuperado += subBits[1] * 64;
                byteRecuperado += subBits[0] * 128;
                data[(x / 7) - 1] = (byte) byteRecuperado;
                subBitsCount = 0;
            }
        }

        System.out.println(new String(data));

        // [0,1,0,0,0,0,0,1]
        System.out.println(Arrays.toString(bits));
        // [0,1,0,0,0,0,0,1,0,1,0,0,0,0,0,1]
    }

    private static int[] toArrayDeBits(String texto) {
        byte[] textoBytes = texto.getBytes(StandardCharsets.UTF_8);
        var arrayDeBits = new int[textoBytes.length * 8];
        for (int i = 0; i < textoBytes.length; i++) {
            var bits = toArrayInt(textoBytes[i]);
            for (int y = 0; y < bits.length; y++) {
                arrayDeBits[(8 * i) + y] = bits[y];
            }
        }
        return arrayDeBits;
    }


    private static Path criarArquivoComMensagem(Path arquivoFonte, String mensagem) throws IOException {
        Path arquivoDestino = Path.of(UUID.randomUUID().toString().substring(0, 5) + ".BMP");

        try (FileInputStream input = new FileInputStream(arquivoFonte.toFile());
             FileOutputStream output = new FileOutputStream(arquivoDestino.toFile())) {

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
                    //
                    if (posBitsDaMensagem < bitsDaMensagem.length) {
                        int[] bitsDoByteAtual = toArrayInt(byteAtual);
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

        return arquivoDestino;
    }

    private static String lerMensagemDoArquivo(Path arquivoComAMensagem) {
        // TODO implementar
        return null;
    }

    private static void copyFile(Path arquivoOriginal, Path novoArquivo) throws IOException {
        try (FileInputStream input = new FileInputStream(arquivoOriginal.toFile());
             FileOutputStream output = new FileOutputStream(novoArquivo.toFile())) {
            int byteAtual = -1;
            while ((byteAtual = input.read()) > -1) {
                output.write(byteAtual);
            }
        }
    }

    private static int[] toArrayInt(int input) {
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

    private static void method1() {
        //        printAnaliseBMP(Path.of("MARBLES.BMP"));
//        printAnaliseBMP(Path.of("hidden_cat.bmp"));

        int displayMask = 1 << 7;
        System.out.println(displayMask + " = " + intToStringBinaryFormat(displayMask));
        System.out.println(1 + " = " + intToStringBinaryFormat(1));
        System.out.println('A' + " = " + intToStringBinaryFormat('A'));
        System.out.println((1 & displayMask));
    }

    private static void printAnaliseBMP(Path arquivoBMP) throws IOException {
        byte[] content = Files.readAllBytes(arquivoBMP);

        System.out.println("-".repeat(80));
        System.out.println("Arquivo BMP: %s".formatted(arquivoBMP));

        System.out.println("Tamanho real do arquivo: %s byte(s)".formatted(content.length));


        int byteQueIniciaOsDadosDeCores = (int) (content[0xA] & 0xFF);

//        System.out.println("Byte que inicia os dados no BMP : %s"
//                .formatted(byteQueIniciaOsDadosDeCores));

        int qtdeDeBytesDisponiveis = content.length - byteQueIniciaOsDadosDeCores;

        System.out.println("Quantidades de bytes disponíveis para o processamento : %s byte(s)"
                .formatted(qtdeDeBytesDisponiveis));

        int quantidadeDeCaracteresDisponiveis = qtdeDeBytesDisponiveis / 8;
        System.out.println("Quantidade de caracteres disponíveis para a mensagem oculta: %s caractere(s)"
                .formatted(quantidadeDeCaracteresDisponiveis));
        System.out.println("-".repeat(80));
    }

    private static void convertingArrayOfBytesToIntStream() {
        byte[] value = "ABC.".getBytes();
        var valueStream = IntStream.range(0, value.length).map(idx -> value[idx]);
        System.out.println(valueStream.mapToObj(EsteganografiaPorLsbApplication::intToStringBinaryFormat).collect(Collectors.joining(",")));
    }

    private static void printCharAsBinaryString() {
        byte input = 'A';

        System.out.println(input + " = " + intToStringBinaryFormat(input));

        input = '$';
        System.out.println(input + " = " + intToStringBinaryFormat(input));

        input = '.';
        System.out.println(input + " = " + intToStringBinaryFormat(input));
    }

    private static String intToStringBinaryFormat(int input) {
        int displayMask = 1 << 7;
        StringWriter content = new StringWriter();
        PrintWriter out = new PrintWriter(content);
        for (int bit = 1; bit <= 8; bit++) {
            out.print((input & displayMask) == 0 ? '0' : '1');
            input <<= 1;
            if (bit % 8 == 0)
                out.print(' ');
        }
        return content.toString();
    }

}
