package com.example.backend.rest;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.Indicateur;
import com.example.backend.service.IndicateurService;

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
@Path("/indicateurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IndicateurResource {

    private final IndicateurService indicateurService;

    public IndicateurResource(IndicateurService indicateurService) {
        this.indicateurService = indicateurService;
    }

    @GET
    public Response getAllIndicateurs(
            @QueryParam("operationId") Long operationId,
            @QueryParam("typeId") Long typeIndicateurId) {
        
        // Filter by both operation and type indicateur
        if (operationId != null && typeIndicateurId != null) {
            return Response.ok(new ApiResponse<>(true, 
                    indicateurService.findByOperationAndTypeIndicateur(operationId, typeIndicateurId), 
                    "Indicateurs filtered by operation and type fetched successfully")).build();
        }
        
        // Filter by operation
        if (operationId != null) {
            return Response.ok(new ApiResponse<>(true, 
                    indicateurService.findByOperation(operationId), 
                    String.format("Indicateurs for operation ID %d fetched successfully", operationId))).build();
        }
        
        // Filter by type indicateur
        if (typeIndicateurId != null) {
            return Response.ok(new ApiResponse<>(true, 
                    indicateurService.findByTypeIndicateur(typeIndicateurId), 
                    String.format("Indicateurs for type indicateur ID %d fetched successfully", typeIndicateurId))).build();
        }
        
        // No filters, get all
        return Response.ok(new ApiResponse<>(true, indicateurService.findAll(), 
                "Indicateurs fetched successfully")).build();
    }

    @GET
    @Path("/{id}")
    public Response getIndicateur(@PathParam("id") Long id) {
        return indicateurService.findById(id)
                .map(indicateur -> Response.ok(new ApiResponse<>(true, indicateur, 
                    String.format("Indicateur with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Indicateur with ID %d not found", id))).build());
    }

    @POST
    public Response createIndicateur(Indicateur indicateur) {
        Indicateur created = indicateurService.create(indicateur);
        return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse<>(true, created, 
                    String.format("Indicateur created successfully with ID %d", created.getId())))
                .build();
    }

    @PUT
    @Path("/{id}")  
    public Response updateIndicateur(@PathParam("id") Long id, Indicateur indicateur) {
        return indicateurService.update(id, indicateur)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Indicateur with ID %d updated successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Indicateur with ID %d not found", id))).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteIndicateur(@PathParam("id") Long id) {
        return indicateurService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Indicateur with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Indicateur with ID %d not found", id))).build();
    }
}
