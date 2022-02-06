package io.github.dearrudam.esteganografiaporlsb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LeitorDeMensagemTest {


    @ParameterizedTest()
    @MethodSource("testArgs")
    void testAdicionarBitABit(List<Integer> bits, String textoEsperado) {

        LeitorDeMensagem gerador = new LeitorDeMensagem();

        bits.forEach(gerador::adicionarBit);

        Assertions.assertEquals(textoEsperado, gerador.pegarMensagem());


    }

    public static Stream<Arguments> testArgs() {
        return Stream.of(
                Arguments.arguments(
                        List.of(0, 1, 0, 0, 0, 0, 0, 1),
                        "A"
                ),
                Arguments.arguments(
                        toArrayDeBits("Olá mundo."),
                        "Olá mundo."
                ),
                Arguments.arguments(
                        toArrayDeBits("Olá mundo. Não devo aparecer"),
                        "Olá mundo."
                )
        );
    }

    private static LinkedList<Integer> toArrayDeBits(String texto) {
        byte[] textoBytes = texto.getBytes(StandardCharsets.UTF_8);
        var arrayDeBits = new LinkedList<Integer>();
        for (int i = 0; i < textoBytes.length; i++) {
            var bits = toArrayInt(textoBytes[i]);
            for (int y = 0; y < bits.length; y++) {
                arrayDeBits.add(bits[y]);
            }
        }
        return arrayDeBits;
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
}
