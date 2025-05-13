package com.example.backend.rest;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.Reservoir;
import com.example.backend.service.ReservoirService;

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
@Path("/reservoirs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservoirResource {

    private final ReservoirService reservoirService;

    public ReservoirResource(ReservoirService reservoirService) {
        this.reservoirService = reservoirService;
    }

    @GET
    public Response getAllReservoirs() {
        return Response.ok(new ApiResponse<>(true, reservoirService.findAll(), "Reservoirs fetched successfully")).build();
    }

    @GET
    @Path("/{id}")
    public Response getReservoir(@PathParam("id") Long id) {
        return reservoirService.findById(id)
                .map(reservoir -> Response.ok(new ApiResponse<>(true, reservoir, 
                    String.format("Reservoir with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Reservoir with ID %d not found", id))).build());
    }
    
    @GET
    @Path("/puit/{puitId}")
    public Response getReservoirsByPuit(@PathParam("puitId") Long puitId) {
        return Response.ok(new ApiResponse<>(true, reservoirService.findByPuit(puitId), 
                String.format("Reservoirs for puit with ID %d fetched successfully", puitId))).build();
    }

    @POST
    public Response createReservoir(Reservoir reservoir) {
        Reservoir created = reservoirService.create(reservoir);
        return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse<>(true, created, 
                    String.format("Reservoir created successfully with ID %d", created.getId())))
                .build();
    }

    @PUT
    @Path("/{id}")  
    public Response updateReservoir(@PathParam("id") Long id, Reservoir reservoir) {
        return reservoirService.update(id, reservoir)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Reservoir with ID %d updated successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Reservoir with ID %d not found", id))).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteReservoir(@PathParam("id") Long id) {
        return reservoirService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Reservoir with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Reservoir with ID %d not found", id))).build();
    }
}
