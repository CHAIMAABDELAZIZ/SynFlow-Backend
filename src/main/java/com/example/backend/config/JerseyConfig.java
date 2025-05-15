package com.example.backend.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.example.backend.rest.BookResource;
import com.example.backend.rest.DocumentResource;
import com.example.backend.rest.ForageResource;
import com.example.backend.rest.IndicateurResource;
import com.example.backend.rest.OperationResource;
import com.example.backend.rest.PhaseResource;
import com.example.backend.rest.ProblemeResource;
import com.example.backend.rest.PuitResource;
import com.example.backend.rest.RegionResource;
import com.example.backend.rest.ReservoirResource;
import com.example.backend.rest.TypeIndicateurResource;
import com.example.backend.rest.TypeOperationResource;
import com.example.backend.rest.UtilisateurResource;

import jakarta.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    
    public JerseyConfig() {
        // Register all resource classes
        register(BookResource.class);
        register(RegionResource.class);
        register(UtilisateurResource.class);
        register(OperationResource.class);
        register(PhaseResource.class);
        register(TypeOperationResource.class);
        register(TypeIndicateurResource.class);
        register(IndicateurResource.class);
        register(ProblemeResource.class);
        register(DocumentResource.class);
        register(ReservoirResource.class);
        register(ForageResource.class);
        register(PuitResource.class);
        
        // Enable CORS filter for Jersey
        register(CorsFilter.class);
        
        // Enable MultiPartFeature
        property("jersey.config.server.provider.classnames", 
                "org.glassfish.jersey.media.multipart.MultiPartFeature");
        
        // For proper handling of Jackson serialization
        register(org.glassfish.jersey.jackson.JacksonFeature.class);
    }
}
