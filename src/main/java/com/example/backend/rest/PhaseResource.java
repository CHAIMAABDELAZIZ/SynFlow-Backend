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
        try {
            return Response.ok(new ApiResponse<>(true, phaseService.findAll(), 
                    "Phases fetched successfully")).build();
        } catch (Exception e) {
            System.err.println("Error fetching all phases: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, null, "Error fetching phases: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getPhase(@PathParam("id") Long id) {
        try {
            return phaseService.findById(id)
                    .map(phase -> Response.ok(new ApiResponse<>(true, phase, 
                        String.format("Phase with ID %d fetched successfully", id))).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiResponse<>(false, null, 
                                String.format("Phase with ID %d not found", id))).build());
        } catch (Exception e) {
            System.err.println("Error fetching phase " + id + ": " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, null, "Error fetching phase: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/forage/{forageId}")
    public Response getPhasesByForage(@PathParam("forageId") Long forageId) {
        try {
            return Response.ok(new ApiResponse<>(true, phaseService.findByForage(forageId), 
                    String.format("Phases for forage with ID %d fetched successfully", forageId))).build();
        } catch (Exception e) {
            System.err.println("Error fetching phases for forage " + forageId + ": " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, null, "Error fetching phases for forage: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response createPhase(Phase phase) {
        try {
            System.out.println("=== PHASE CREATION REQUEST ===");
            System.out.println("Received phase object: " + phase);
            System.out.println("Phase number: " + phase.getNumeroPhase());
            System.out.println("Phase diametre: " + phase.getDiametre());
            System.out.println("Phase description: " + phase.getDescription());
            System.out.println("Phase forage: " + (phase.getForage() != null ? phase.getForage().getId() : "null"));
            System.out.println("Phase start date: " + phase.getDateDebutPrevue());
            System.out.println("Phase end date: " + phase.getDateFinPrevue());
            
            if (phase.getForage() == null || phase.getForage().getId() == null) {
                System.err.println("ERROR: Forage is null or has null ID");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ApiResponse<>(false, null, "Forage ID is required"))
                        .build();
            }

            if (phase.getNumeroPhase() == null) {
                System.err.println("ERROR: Phase number is null");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ApiResponse<>(false, null, "Phase number is required"))
                        .build();
            }

            if (phase.getDiametre() == null) {
                System.err.println("ERROR: Phase diametre is null");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ApiResponse<>(false, null, "Phase diametre is required"))
                        .build();
            }
            
            System.out.println("Calling phaseService.create()...");
            Phase created = phaseService.create(phase);
            System.out.println("Successfully created phase with ID: " + created.getId());
            System.out.println("=== PHASE CREATION SUCCESS ===");
            
            return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse<>(true, created, 
                        String.format("Phase created successfully with ID %d", created.getId())))
                    .build();
        } catch (IllegalArgumentException e) {
            System.err.println("=== PHASE CREATION VALIDATION ERROR ===");
            System.err.println("Validation error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, null, e.getMessage()))
                    .build();
        } catch (Exception e) {
            System.err.println("=== PHASE CREATION UNEXPECTED ERROR ===");
            System.err.println("Unexpected error creating phase: " + e.getMessage());
            System.err.println("Error class: " + e.getClass().getName());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, null, "Error creating phase: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")  
    public Response updatePhase(@PathParam("id") Long id, Phase phase) {
        return phaseService.update(id, phase)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Phase with ID %d updated successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Phase with ID %d not found", id))).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deletePhase(@PathParam("id") Long id) {
        try {
            return phaseService.delete(id)
                    ? Response.ok(new ApiResponse<>(true, null, 
                        String.format("Phase with ID %d deleted successfully", id))).build()
                    : Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiResponse<>(false, null, 
                                String.format("Phase with ID %d not found", id))).build();
        } catch (Exception e) {
            System.err.println("Error deleting phase " + id + ": " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, null, "Error deleting phase: " + e.getMessage()))
                    .build();
        }
    }
}
