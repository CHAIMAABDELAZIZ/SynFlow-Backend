package com.example.backend.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.example.backend.rest.ActiviteResource;
import com.example.backend.rest.BookResource;
import com.example.backend.rest.RegionResource;

import jakarta.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    
    public JerseyConfig() {
        // Register all resource classes
        register(BookResource.class);
        register(RegionResource.class);
        register(ActiviteResource.class);
        
        // Add more resources as needed
        
        // Enable CORS
        property("jersey.config.server.provider.classnames", 
                "org.glassfish.jersey.media.multipart.MultiPartFeature");
    }
}
