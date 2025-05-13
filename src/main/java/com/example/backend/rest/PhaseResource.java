package com.example.backend.rest;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.Phase;
import com.example.backend.service.PhaseService;

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
@Path("/phases")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PhaseResource {

    private final PhaseService phaseService;

    public PhaseResource(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    @GET
    public Response getAllPhases() {
        return Response.ok(new ApiResponse<>(true, phaseService.findAll(), "Phases fetched successfully")).build();
    }

    @GET
    @Path("/{id}")
    public Response getPhase(@PathParam("id") Long id) {
        return phaseService.findById(id)
                .map(phase -> Response.ok(new ApiResponse<>(true, phase, 
                    String.format("Phase with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Phase with ID %d not found", id))).build());
    }
    
    @GET
    @Path("/forage/{forageId}")
    public Response getPhasesByForage(@PathParam("forageId") Long forageId) {
        return Response.ok(new ApiResponse<>(true, phaseService.findByForage(forageId), 
                String.format("Phases for forage with ID %d fetched successfully", forageId))).build();
    }

    @POST
    public Response createPhase(Phase phase) {
        try {
            Phase created = phaseService.create(phase);
            return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse<>(true, created, 
                        String.format("Phase created successfully with ID %d", created.getId())))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, null, e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")  
    public Response updatePhase(@PathParam("id") Long id, Phase phase) {
        try {
            return phaseService.update(id, phase)
                    .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                        String.format("Phase with ID %d updated successfully", id))).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiResponse<>(false, null, 
                                String.format("Phase with ID %d not found", id))).build());
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, null, e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletePhase(@PathParam("id") Long id) {
        return phaseService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Phase with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Phase with ID %d not found", id))).build();
    }
}
