package com.example.backend.rest;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.Puit;
import com.example.backend.service.PuitService;

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
@Path("/puits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PuitResource {

    private final PuitService puitService;

    public PuitResource(PuitService puitService) {
        this.puitService = puitService;
    }

    @GET
    public Response getAllPuits() {
        return Response.ok(new ApiResponse<>(true, puitService.findAll(), "Puits fetched successfully")).build();
    }

    @GET
    @Path("/{id}")
    public Response getPuit(@PathParam("id") Long id) {
        return puitService.findById(id)
                .map(puit -> Response.ok(new ApiResponse<>(true, puit, 
                    String.format("Puit with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Puit with ID %d not found", id))).build());
    }
    
    @GET
    @Path("/region/{regionId}")
    public Response getPuitsByRegion(@PathParam("regionId") Long regionId) {
        return Response.ok(new ApiResponse<>(true, puitService.findByRegion(regionId), 
                String.format("Puits for region with ID %d fetched successfully", regionId))).build();
    }

    @POST
    public Response createPuit(Puit puit) {
        Puit created = puitService.create(puit);
        return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse<>(true, created, 
                    String.format("Puit created successfully with ID %d", created.getId())))
                .build();
    }

    @PUT
    @Path("/{id}")  
    public Response updatePuit(@PathParam("id") Long id, Puit puit) {
        return puitService.update(id, puit)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Puit with ID %d updated successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Puit with ID %d not found", id))).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deletePuit(@PathParam("id") Long id) {
        return puitService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Puit with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Puit with ID %d not found", id))).build();
    }
}
