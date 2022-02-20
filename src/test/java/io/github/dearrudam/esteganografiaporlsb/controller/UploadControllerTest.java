package io.github.dearrudam.esteganografiaporlsb.controller;

import jdk.jfr.ContentType;
import org.assertj.core.api.Assertions;
import org.hamcrest.Condition;
import org.hamcrest.Matchers;
import org.hamcrest.io.FileMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@EnableWebMvc
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
                .andExpect(content().string(Matchers.not(Matchers.blankString())))
                .andReturn();

        Assertions
                .assertThat(mvcResult.getResponse().getContentAsString())
                .isNotBlank();
    }

}
