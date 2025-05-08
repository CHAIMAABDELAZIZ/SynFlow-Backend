package com.example.backend.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
import jakarta.ws.rs.ApplicationPath;

import com.example.backend.rest.BookResource;

@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(BookResource.class);
    }
}
