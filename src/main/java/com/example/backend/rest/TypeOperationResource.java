package com.example.backend.rest;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.TypeOperation;
import com.example.backend.service.TypeOperationService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Component
@Path("/type-operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TypeOperationResource {

    private final TypeOperationService typeOperationService;

    public TypeOperationResource(TypeOperationService typeOperationService) {
        this.typeOperationService = typeOperationService;
    }

    @GET
    public Response getAllTypeOperations() {
        return Response.ok(new ApiResponse<>(true, typeOperationService.findAll(), 
                "Type operations fetched successfully")).build();
    }

    @GET
    @Path("/{code}")
    public Response getTypeOperation(@PathParam("code") String code) {
        return typeOperationService.findByCode(code)
                .map(typeOperation -> Response.ok(new ApiResponse<>(true, typeOperation, 
                    String.format("Type operation with code %s fetched successfully", code))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Type operation with code %s not found", code))).build());
    }
    
    @GET
    @Path("/phase/{phaseId}")
    public Response getTypeOperationsByPhase(@PathParam("phaseId") Long phaseId) {
        return Response.ok(new ApiResponse<>(true, typeOperationService.findByPhase(phaseId), 
                String.format("Type operations for phase with ID %d fetched successfully", phaseId))).build();
    }

    @POST
    public Response createTypeOperation(TypeOperation typeOperation) {
        if (typeOperation.getCode() == null || typeOperation.getCode().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, null, "Type operation code is required"))
                    .build();
        }
        
        // Check if the code already exists
        if (typeOperationService.findByCode(typeOperation.getCode()).isPresent()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ApiResponse<>(false, null, 
                        String.format("Type operation with code %s already exists", typeOperation.getCode())))
                    .build();
        }
        
        TypeOperation created = typeOperationService.create(typeOperation);
        return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse<>(true, created, 
                    String.format("Type operation with code %s created successfully", created.getCode())))
                .build();
    }

    @PUT
    @Path("/{code}")  
    public Response updateTypeOperation(@PathParam("code") String code, TypeOperation typeOperation) {
        return typeOperationService.update(code, typeOperation)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Type operation with code %s updated successfully", code))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Type operation with code %s not found", code))).build());
    }

    @DELETE
    @Path("/{code}")
    public Response deleteTypeOperation(@PathParam("code") String code) {
        return typeOperationService.delete(code)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Type operation with code %s deleted successfully", code))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Type operation with code %s not found", code))).build();
    }
}
