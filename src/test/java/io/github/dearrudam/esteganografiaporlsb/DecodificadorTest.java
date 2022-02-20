package io.github.dearrudam.esteganografiaporlsb;

import io.github.dearrudam.esteganografiaporlsb.model.Decodificador;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Path;

public class DecodificadorTest {

    @ParameterizedTest
    @CsvSource(textBlock = """
            3f41a.BMP;A galera na TwitchTV hoje é massa demais.
            """, delimiterString = ";")
    void testDecodifica(String arquivoBMP, String mensagemEsperada) {

        Decodificador decodificador = new Decodificador();

        String mensagemLida = decodificador.decodifica(Path.of(arquivoBMP));

        Assertions.assertThat(mensagemLida).isEqualTo(mensagemEsperada);

    }

}
