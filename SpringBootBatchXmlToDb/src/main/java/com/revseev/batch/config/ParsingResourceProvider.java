package com.revseev.batch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ParsingResourceProvider {

    @Value("${parsing.resource.path}")
    private String path;
    @Value("${parsing.resource.classpath}")
    private String classpath;

    public Resource provide() {
//        return new FileSystemResource(path);
        return new ClassPathResource(classpath);
    }

}
