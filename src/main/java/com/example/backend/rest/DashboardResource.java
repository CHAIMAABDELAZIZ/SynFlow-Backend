package com.example.backend.rest;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.service.DashboardService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Component
@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardResource {

    private final DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GET
    @Path("/data")
    public Response getDashboardData() {
        try {
            Map<String, Object> dashboardData = dashboardService.getDashboardData();
            return Response.ok(new ApiResponse<>(true, dashboardData, 
                "Dashboard data fetched successfully")).build();
        } catch (Exception e) {
            System.err.println("Error fetching dashboard data: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, null, "Error fetching dashboard data"))
                    .build();
        }
    }
}
