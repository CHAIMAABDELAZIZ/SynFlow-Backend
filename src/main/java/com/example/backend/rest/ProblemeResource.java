package com.example.backend.rest;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.Probleme;
import com.example.backend.service.ProblemeService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Component
@Path("/problemes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProblemeResource {

    private final ProblemeService problemeService;

    public ProblemeResource(ProblemeService problemeService) {
        this.problemeService = problemeService;
    }

    @GET
    public Response getAllProblemes(
            @QueryParam("operationId") Long operationId,
            @QueryParam("type") String type,
            @QueryParam("signaleParId") Long signaleParId,
            @QueryParam("resoluParId") Long resoluParId,
            @QueryParam("gravite") String gravite,
            @QueryParam("statut") String statut) {
        
        // Filter by operation
        if (operationId != null) {
            return Response.ok(new ApiResponse<>(true, problemeService.findByOperation(operationId), 
                    String.format("Problèmes for operation ID %d fetched successfully", operationId))).build();
        }
        
        // Filter by type
        if (type != null && !type.isEmpty()) {
            return Response.ok(new ApiResponse<>(true, problemeService.findByType(type), 
                    String.format("Problèmes of type '%s' fetched successfully", type))).build();
        }
        
        // Filter by signaler
        if (signaleParId != null) {
            return Response.ok(new ApiResponse<>(true, problemeService.findBySignalePar(signaleParId), 
                    String.format("Problèmes reported by user ID %d fetched successfully", signaleParId))).build();
        }
        
        // Filter by resolver
        if (resoluParId != null) {
            return Response.ok(new ApiResponse<>(true, problemeService.findByResoluPar(resoluParId), 
                    String.format("Problèmes resolved by user ID %d fetched successfully", resoluParId))).build();
        }
        
        // Filter by severity
        if (gravite != null && !gravite.isEmpty()) {
            return Response.ok(new ApiResponse<>(true, problemeService.findByGravite(gravite), 
                    String.format("Problèmes with severity '%s' fetched successfully", gravite))).build();
        }
        
        // Filter by status
        if (statut != null && !statut.isEmpty()) {
            return Response.ok(new ApiResponse<>(true, problemeService.findByStatut(statut), 
                    String.format("Problèmes with status '%s' fetched successfully", statut))).build();
        }
        
        // No filters, get all
        return Response.ok(new ApiResponse<>(true, problemeService.findAll(), 
                "Problèmes fetched successfully")).build();
    }

    @GET
    @Path("/{id}")
    public Response getProbleme(@PathParam("id") Long id) {
        return problemeService.findById(id)
                .map(probleme -> Response.ok(new ApiResponse<>(true, probleme, 
                    String.format("Problème with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Problème with ID %d not found", id))).build());
    }

    @POST
    public Response createProbleme(Probleme probleme) {
        Probleme created = problemeService.create(probleme);
        return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse<>(true, created, 
                    String.format("Problème created successfully with ID %d", created.getId())))
                .build();
    }

    @PUT
    @Path("/{id}")  
    public Response updateProbleme(@PathParam("id") Long id, Probleme probleme) {
        return problemeService.update(id, probleme)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Problème with ID %d updated successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Problème with ID %d not found", id))).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProbleme(@PathParam("id") Long id) {
        return problemeService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Problème with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Problème with ID %d not found", id))).build();
    }
}
