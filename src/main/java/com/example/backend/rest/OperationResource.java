package com.example.backend.rest;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.Operation;
import com.example.backend.service.OperationService;

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
@Path("/operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OperationResource {

    private final OperationService operationService;

    public OperationResource(OperationService operationService) {
        this.operationService = operationService;
    }

    @GET
    public Response getAllOperations(@QueryParam("statut") String statut) {
        if (statut != null && !statut.isEmpty()) {
            return Response.ok(new ApiResponse<>(true, operationService.findByStatut(statut), 
                    String.format("Operations with statut '%s' fetched successfully", statut))).build();
        }
        return Response.ok(new ApiResponse<>(true, operationService.findAll(), 
                "Operations fetched successfully")).build();
    }

    @GET
    @Path("/{id}")
    public Response getOperation(@PathParam("id") Long id) {
        return operationService.findById(id)
                .map(operation -> Response.ok(new ApiResponse<>(true, operation, 
                    String.format("Operation with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Operation with ID %d not found", id))).build());
    }
    
    @GET
    @Path("/phase/{phaseId}")
    public Response getOperationsByPhase(@PathParam("phaseId") Long phaseId) {
        return Response.ok(new ApiResponse<>(true, operationService.findByPhase(phaseId), 
                String.format("Operations for phase with ID %d fetched successfully", phaseId))).build();
    }
    
    @GET
    @Path("/type/{typeCode}")
    public Response getOperationsByType(@PathParam("typeCode") String typeCode) {
        return Response.ok(new ApiResponse<>(true, operationService.findByTypeOperation(typeCode), 
                String.format("Operations for type operation with code '%s' fetched successfully", typeCode))).build();
    }
    
    @GET
    @Path("/user/{userId}")
    public Response getOperationsByUser(@PathParam("userId") Long userId) {
        return Response.ok(new ApiResponse<>(true, operationService.findByCreatedBy(userId), 
                String.format("Operations created by user with ID %d fetched successfully", userId))).build();
    }

    @POST
    public Response createOperation(Operation operation) {
        try {
            System.out.println("Creating operation with cost: " + operation.getCoutPrev());
            Operation created = operationService.create(operation);
            System.out.println("Created operation with cost: " + created.getCoutPrev());
            return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse<>(true, created, 
                        String.format("Operation created successfully with ID %d", created.getId())))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, null, e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, null, "Error creating operation: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")  
    public Response updateOperation(@PathParam("id") Long id, Operation operation) {
        return operationService.update(id, operation)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Operation with ID %d updated successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Operation with ID %d not found", id))).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOperation(@PathParam("id") Long id) {
        return operationService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Operation with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Operation with ID %d not found", id))).build();
    }
}
