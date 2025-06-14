package com.example.backend.rest;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.Forage;
import com.example.backend.service.ForageService;

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
import java.util.List;

@Component
@Path("/forages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ForageResource {

    private final ForageService forageService;

    public ForageResource(ForageService forageService) {
        this.forageService = forageService;
    }

    @GET
    public Response getAllForages() {
        return Response.ok(new ApiResponse<>(true, forageService.findAll(), "Forages fetched successfully")).build();
    }

    @GET
    @Path("/{id}")
    public Response getForage(@PathParam("id") Long id) {
        return forageService.findById(id)
                .map(forage -> Response.ok(new ApiResponse<>(true, forage, 
                    String.format("Forage with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Forage with ID %d not found", id))).build());
    }

    @POST
    public Response createForage(Forage forage) {
        Forage created = forageService.create(forage);
        return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse<>(true, created, 
                    String.format("Forage created successfully with ID %d", created.getId())))
                .build();
    }

    @GET
    @Path("/puit/{puitId}")
    public Response getForagesByPuitId(@PathParam("puitId") Long puitId) {
        List<Forage> forages = forageService.findByPuitId(puitId);
        return Response.ok(new ApiResponse<>(true, forages,
                String.format("Forages for Puit ID %d fetched successfully", puitId))).build();
    }

    @PUT
    @Path("/{id}")  
    public Response updateForage(@PathParam("id") Long id, Forage forage) {
        return forageService.update(id, forage)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Forage with ID %d updated successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Forage with ID %d not found", id))).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteForage(@PathParam("id") Long id) {
        return forageService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Forage with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Forage with ID %d not found", id))).build();
    }
}
