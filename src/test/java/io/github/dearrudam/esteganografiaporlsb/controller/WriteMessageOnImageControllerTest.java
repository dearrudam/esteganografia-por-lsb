package io.github.dearrudam.esteganografiaporlsb.controller;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WriteMessageOnImageControllerTest {

    static Path arquivoValido;

    Path arquivoDestino;

    @BeforeAll
    static void criarArquivoValido() throws IOException {
        Files.copy(
                Path.of("3f41a.BMP"),
                arquivoValido = Files.createTempFile("", ""),
                StandardCopyOption.REPLACE_EXISTING);
    }

    @AfterAll
    static void apagarArquivoValido() {
        arquivoValido.toFile().delete();
    }

    @BeforeEach
    void criarArquivoTemporario() throws IOException {
        Files.copy(
                Path.of("3f41a.BMP"),
                arquivoDestino = Files.createTempFile("", ""),
                StandardCopyOption.REPLACE_EXISTING);
    }

    @AfterEach
    void apagarArquivoTemporario() {
        arquivoDestino.toFile().delete();
    }

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("caso de sucesso")
    void test() throws Exception {
        mockMvc.perform(
                        post("/write-message-on-image")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content("""
                                        {
                                            "image" : "%s",
                                            "message" : "%s"
                                        }
                                        """.formatted(
                                        this.arquivoDestino.getFileName(),
                                        "Esse chat do nossa live é muito massa.")
                                ))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("file", not(blankOrNullString())));

    }


    @ParameterizedTest(name = "{index} {0}")
    @MethodSource("casosInvalidos")
    public void testarCasosInvalidos(
            String cenario,
            String payload,
            int statusCode
    ) throws Exception {
        mockMvc.perform(
                        post("/write-message-on-image")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(payload))
                .andExpect(status().is(statusCode));
    }

    public static Stream<Arguments> casosInvalidos(){
        var faker=new Faker();
        return Stream.of(
                arguments(
                        "não deve aceitar requisições onde o atributo image for omitido",
                        """
                        {
                             "message" : "%s"
                        }        
                        """.formatted(faker.lorem().characters(40)),
                        400
                ),
                arguments(
                        "não deve aceitar requisições onde o atributo image não existir",
                        """
                        {
                            "image" : "%s",
                            "message" : "%s."
                        }        
                        """.formatted(
                                faker.file().fileName(),
                                faker.lorem().characters(40)),
                        400
                ),
                arguments(
                        "não deve aceitar requisições onde o atributo message for omitido",
                        """
                        {
                            "image" : "%s"
                        }        
                        """.formatted(arquivoValido.toFile().getName()),
                        400
                ),
                arguments(
                        "não deve aceitar requisições onde o atributo message estiver em vazio",
                        """
                        {
                            "image" : "%s",
                            "message" : ""
                        }        
                        """.formatted(arquivoValido.toFile().getName()),
                        400
                ),
                arguments(
                        "não deve aceitar requisições onde o atributo message estiver em branco",
                        """
                        {
                            "image" : "%s",
                            "message" : " "
                        }        
                        """.formatted(arquivoValido.toFile().getName()),
                        400
                ),
                arguments(
                        "não deve aceitar requisições onde o atributo message estiver em branco",
                        """
                        {
                            "image" : "%s",
                            "message" : " "
                        }        
                        """.formatted(arquivoValido.toFile().getName()),
                        400
                )
        );
    }
}
