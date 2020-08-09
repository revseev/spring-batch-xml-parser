package com.revseev.batch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ParsingResourceProvider {

    @Value("${parsing.resource.path}")
    private String path;

    public Resource provide() {
        return new FileSystemResource(path);
    }

}
