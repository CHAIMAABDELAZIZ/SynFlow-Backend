package com.example.backend.rest;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.TypeIndicateur;
import com.example.backend.service.TypeIndicateurService;

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
@Path("/type-indicateurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TypeIndicateurResource {

    private final TypeIndicateurService typeIndicateurService;

    public TypeIndicateurResource(TypeIndicateurService typeIndicateurService) {
        this.typeIndicateurService = typeIndicateurService;
    }

    @GET
    public Response getAllTypeIndicateurs(@QueryParam("obligatoire") Boolean estObligatoire) {
        if (estObligatoire != null) {
            return Response.ok(new ApiResponse<>(true, typeIndicateurService.findByEstObligatoire(estObligatoire), 
                    String.format("Type indicateurs where estObligatoire=%s fetched successfully", estObligatoire))).build();
        }
        return Response.ok(new ApiResponse<>(true, typeIndicateurService.findAll(), 
                "Type indicateurs fetched successfully")).build();
    }

    @GET
    @Path("/{id}")
    public Response getTypeIndicateur(@PathParam("id") Long id) {
        return typeIndicateurService.findById(id)
                .map(typeIndicateur -> Response.ok(new ApiResponse<>(true, typeIndicateur, 
                    String.format("Type indicateur with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Type indicateur with ID %d not found", id))).build());
    }
    
    @GET
    @Path("/type-operation/{typeOperationCode}")
    public Response getTypeIndicateursByTypeOperation(@PathParam("typeOperationCode") String typeOperationCode) {
        return Response.ok(new ApiResponse<>(true, typeIndicateurService.findByTypeOperation(typeOperationCode), 
                String.format("Type indicateurs for type operation with code '%s' fetched successfully", typeOperationCode))).build();
    }

    @POST
    public Response createTypeIndicateur(TypeIndicateur typeIndicateur) {
        TypeIndicateur created = typeIndicateurService.create(typeIndicateur);
        return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse<>(true, created, 
                    String.format("Type indicateur created successfully with ID %d", created.getId())))
                .build();
    }

    @PUT
    @Path("/{id}")  
    public Response updateTypeIndicateur(@PathParam("id") Long id, TypeIndicateur typeIndicateur) {
        return typeIndicateurService.update(id, typeIndicateur)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Type indicateur with ID %d updated successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Type indicateur with ID %d not found", id))).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTypeIndicateur(@PathParam("id") Long id) {
        return typeIndicateurService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Type indicateur with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Type indicateur with ID %d not found", id))).build();
    }
}
