package io.github.dearrudam.esteganografiaporlsb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.nio.file.Path;

@Configuration
@EnableAsync
public class AppConfig {

    @Value("#{systemProperties['java.io.tmpdir']}")
    private String tempDir;

    @Bean
    @WorkDir
    public Path getTempDir(){
        return Path.of(tempDir);
    }

}
