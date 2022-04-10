package io.github.dearrudam.esteganografiaporlsb.controller;

import io.github.dearrudam.esteganografiaporlsb.config.WorkDir;
import io.github.dearrudam.esteganografiaporlsb.model.Codificador;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNullApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping(path = "/write-message-on-image")
public class WriteMessageOnImageController {

    private Path workdir;
    private final Codificador codificador;

    public WriteMessageOnImageController(
            @WorkDir Path workdir,
            Codificador codificador) {
        this.workdir = workdir;
        this.codificador = codificador;
    }

    @PostMapping
    public ResponseEntity process(
            @RequestBody @Valid MessageRequest message
    ) throws IOException {

        Path imagemBase = Path.of(this.workdir.toString(), message.image());
        if (!imagemBase.toFile().exists()) {
            return ResponseEntity.badRequest().build();
        }

        var imagemDeDestino =
                Path.of(this.workdir.toString(), UUID.randomUUID().toString());

        codificador.codifique(message.message(), imagemBase, imagemDeDestino);

        return ResponseEntity.accepted().body(
                new MessageResponse(imagemDeDestino.toFile().getName())
        );
    }


    record MessageRequest(@NotBlank String image, @NotBlank String message) {
    }

    record MessageResponse(String file) {
    }
}
