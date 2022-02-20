package io.github.dearrudam.esteganografiaporlsb;

import io.github.dearrudam.esteganografiaporlsb.model.Codificador;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class CodificadorTest {

    Codificador codificador;

    @BeforeEach
    public void setup() {
        codificador = new Codificador();
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            A galera na TwitchTV hoje é massa demais.;MARBLES.BMP;3f41a.BMP
            """, delimiterString = ";")
    void testCodifica(String mensagem, String imagemBase, String imagemEsperada) throws IOException {
        var arquivoTemporario = Path.of(UUID.randomUUID().toString().substring(0, 5) + ".BMP");
        try {

            codificador.codifique(mensagem, Path.of(imagemBase), arquivoTemporario);

            var bytesGerados = Files.readAllBytes(arquivoTemporario);
            var bytesEsperados = Files.readAllBytes(Path.of(imagemEsperada));

            Assertions.assertArrayEquals(
                    bytesGerados,
                    bytesEsperados);

        } finally {
            arquivoTemporario.toFile().delete();
        }
    }
}
