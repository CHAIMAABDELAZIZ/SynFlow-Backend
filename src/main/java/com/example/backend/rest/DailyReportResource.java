package com.example.backend.rest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.DailyReport;
import com.example.backend.service.DailyReportService;

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
@Path("/daily-reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class DailyReportResource {

    private final DailyReportService dailyReportService;

    public DailyReportResource(DailyReportService dailyReportService) {
        this.dailyReportService = dailyReportService;
    }

    @GET
    @Transactional(readOnly = true)
    public Response getAllDailyReports(@QueryParam("puitId") Long puitId, 
                                     @QueryParam("reportDate") String reportDateStr) {
        try {
            if (puitId != null && reportDateStr != null) {
                LocalDate reportDate = LocalDate.parse(reportDateStr);
                return Response.ok(new ApiResponse<>(true, 
                    dailyReportService.findByPuitAndDate(puitId, reportDate), 
                    "Daily reports fetched successfully")).build();
            } else if (puitId != null) {
                return Response.ok(new ApiResponse<>(true, 
                    dailyReportService.findByPuit(puitId), 
                    "Daily reports fetched successfully")).build();
            } else if (reportDateStr != null) {
                LocalDate reportDate = LocalDate.parse(reportDateStr);
                return Response.ok(new ApiResponse<>(true, 
                    dailyReportService.findByReportDate(reportDate), 
                    "Daily reports fetched successfully")).build();
            } else {
                return Response.ok(new ApiResponse<>(true, 
                    dailyReportService.findAll(), 
                    "Daily reports fetched successfully")).build();
            }
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, null, "Invalid date format")).build();
        }
    }

    @GET
    @Path("/{id}")
    @Transactional(readOnly = true)
    public Response getDailyReport(@PathParam("id") Long id) {
        return dailyReportService.findById(id)
                .map(report -> Response.ok(new ApiResponse<>(true, report, 
                    String.format("Daily report with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Daily report with ID %d not found", id))).build());
    }

    @POST
    public Response createDailyReport(DailyReport dailyReport) {
        try {
            DailyReport created = dailyReportService.create(dailyReport);
            return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse<>(true, created, 
                        String.format("Daily report created successfully with ID %d", created.getId())))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, null, e.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateDailyReport(@PathParam("id") Long id, DailyReport dailyReport) {
        return dailyReportService.update(id, dailyReport)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Daily report with ID %d updated successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Daily report with ID %d not found", id))).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDailyReport(@PathParam("id") Long id) {
        return dailyReportService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Daily report with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Daily report with ID %d not found", id))).build();
    }
}
