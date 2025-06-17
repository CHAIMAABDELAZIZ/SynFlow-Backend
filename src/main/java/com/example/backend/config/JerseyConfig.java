package com.example.backend.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.example.backend.rest.DailyReportResource;
import com.example.backend.rest.DashboardResource;
import com.example.backend.rest.ForageResource;
import com.example.backend.rest.OperationResource;
import com.example.backend.rest.PhaseResource;
import com.example.backend.rest.ProblemeResource;
import com.example.backend.rest.PuitResource;
import com.example.backend.rest.RegionResource;
import com.example.backend.rest.TypeIndicateurResource;
import com.example.backend.rest.TypeOperationResource;
import com.example.backend.rest.UtilisateurResource;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        // Register all REST resources
        register(PuitResource.class);
        register(RegionResource.class);
        register(UtilisateurResource.class);
        register(TypeOperationResource.class);
        register(TypeIndicateurResource.class);
        register(ForageResource.class);
        register(PhaseResource.class);
        register(OperationResource.class);
        register(DailyReportResource.class);
        register(ProblemeResource.class);
        register(DashboardResource.class); // Add dashboard resource
        
        // Enable CORS
        register(CorsFilter.class);
        
        // Enable logging
        property("jersey.config.server.tracing.type", "ALL");
        property("jersey.config.server.tracing.threshold", "VERBOSE");
    }
    
    // CORS Filter
    @Component
    public static class CorsFilter implements jakarta.ws.rs.container.ContainerResponseFilter {
        @Override
        public void filter(jakarta.ws.rs.container.ContainerRequestContext requestContext,
                          jakarta.ws.rs.container.ContainerResponseContext responseContext) {
            responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
            responseContext.getHeaders().add("Access-Control-Allow-Headers", 
                "origin, content-type, accept, authorization");
            responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().add("Access-Control-Allow-Methods", 
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        }
    }
}
