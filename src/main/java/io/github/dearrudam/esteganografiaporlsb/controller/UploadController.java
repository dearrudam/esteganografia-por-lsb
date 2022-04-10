package io.github.dearrudam.esteganografiaporlsb.controller;

import io.github.dearrudam.esteganografiaporlsb.config.WorkDir;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@RestController
public class UploadController {

    private final Path workdir;

    public UploadController(
            @WorkDir
            Path workdir) {
        this.workdir = workdir;
    }

    @PostMapping(
            path = "/upload",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> upload(@RequestPart MultipartFile file) throws IOException {

        var arquivoTemporario = Path.of(
                this.workdir.toString(),
                UUID.randomUUID().toString());

        file.transferTo(arquivoTemporario);

        System.out.println("arquivo salvo em: " + arquivoTemporario.toString());

        return ResponseEntity.ok(arquivoTemporario.toFile().getName());
    }


}
