package com.example.backend.rest;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.Region;
import com.example.backend.service.RegionService;

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
@Path("/regions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegionResource {

    private final RegionService regionService;

    public RegionResource(RegionService regionService) {
        this.regionService = regionService;
    }

    @GET
    public Response getAllRegions() {
        return Response.ok(new ApiResponse<>(true, regionService.findAll(), "Regions fetched successfully")).build();
    }

    @GET
    @Path("/{id}")
    public Response getRegion(@PathParam("id") Long id) {
        return regionService.findById(id)
                .map(region -> Response.ok(new ApiResponse<>(true, region, 
                    String.format("Region with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Region with ID %d not found", id))).build());
    }

    @POST
    public Response createRegion(Region region) {
        Region created = regionService.create(region);
        return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse<>(true, created, 
                    String.format("Region created successfully with ID %d", created.getId())))
                .build();
    }

    @PUT
    @Path("/{id}")  
    public Response updateRegion(@PathParam("id") Long id, Region region) {
        return regionService.update(id, region)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Region with ID %d updated successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Region with ID %d not found", id))).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRegion(@PathParam("id") Long id) {
        return regionService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Region with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Region with ID %d not found", id))).build();
    }
}
