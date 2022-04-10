package io.github.dearrudam.esteganografiaporlsb.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UploadControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testUpload() throws Exception {

        var arquivoBMP = Path.of("3f41a.BMP");

        MockMultipartFile requestBody = new MockMultipartFile(
                "file",
                Files.newInputStream(arquivoBMP));

        MvcResult mvcResult = mockMvc.perform(multipart("/upload")
                        .file(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();

        Assertions
                .assertThat(mvcResult.getResponse().getContentAsString())
                .isNotBlank();
    }

}
