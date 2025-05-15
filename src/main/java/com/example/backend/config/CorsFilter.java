package com.example.backend.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, 
            ContainerResponseContext responseContext) throws IOException {
        // Allow access from any origin
        responseContext.getHeaders().add(
                "Access-Control-Allow-Origin", "*");
        // Note: We can't use wildcards with credentials, so if you need credentials,
        // you'll need to specify exact origins instead of "*"
        responseContext.getHeaders().add(
                "Access-Control-Allow-Credentials", "true");
        // Allow all headers
        responseContext.getHeaders().add(
                "Access-Control-Allow-Headers",
                "*");
        // Allow all methods
        responseContext.getHeaders().add(
                "Access-Control-Allow-Methods", 
                "*");
    }
}
