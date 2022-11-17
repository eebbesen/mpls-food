package com.humegatech.mpls_food.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.io.IOException;


@Configuration
@Profile("local")
public class LocalDevConfig {

    public LocalDevConfig(final TemplateEngine templateEngine) throws IOException {
        File sourceRoot = new ClassPathResource("application.yml").getFile().getParentFile();
        while (sourceRoot.listFiles((dir, name) -> name.equals("gradlew")).length != 1) {
            sourceRoot = sourceRoot.getParentFile();
        }
        final FileTemplateResolver fileTemplateResolver = new FileTemplateResolver();
        fileTemplateResolver.setPrefix(sourceRoot.getPath() + "/src/main/resources/templates/");
        fileTemplateResolver.setSuffix(".html");
        fileTemplateResolver.setCacheable(false);
        fileTemplateResolver.setCharacterEncoding("UTF-8");
        fileTemplateResolver.setCheckExistence(true);

        templateEngine.setTemplateResolver(fileTemplateResolver);
    }

}
