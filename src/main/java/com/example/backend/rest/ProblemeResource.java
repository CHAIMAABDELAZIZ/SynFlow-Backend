package com.example.backend.rest;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.Probleme;
import com.example.backend.service.ProblemeService;

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
@Path("/problemes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProblemeResource {

    private final ProblemeService problemeService;

    public ProblemeResource(ProblemeService problemeService) {
        this.problemeService = problemeService;
    }

    @GET
    public Response getAllProblemes(
            @QueryParam("operationId") Long operationId,
            @QueryParam("type") String type,
            @QueryParam("signaleParId") Long signaleParId,
            @QueryParam("resoluParId") Long resoluParId,
            @QueryParam("gravite") String gravite,
            @QueryParam("statut") String statut) {
        
        try {
            System.out.println("=== GET PROBLEMES REQUEST ===");
            System.out.println("operationId: " + operationId);
            System.out.println("type: " + type);
            System.out.println("gravite: " + gravite);
            System.out.println("statut: " + statut);
            
            // Filter by operation
            if (operationId != null) {
                List<Probleme> problems = problemeService.findByOperation(operationId);
                System.out.println("Found " + problems.size() + " problems for operation " + operationId);
                return Response.ok(new ApiResponse<>(true, problems, 
                        String.format("Problèmes for operation ID %d fetched successfully", operationId))).build();
            }
            
            // Filter by type
            if (type != null && !type.isEmpty()) {
                List<Probleme> problems = problemeService.findByType(type);
                System.out.println("Found " + problems.size() + " problems of type " + type);
                return Response.ok(new ApiResponse<>(true, problems, 
                        String.format("Problèmes of type '%s' fetched successfully", type))).build();
            }
            
            // Filter by signaler
            if (signaleParId != null) {
                List<Probleme> problems = problemeService.findBySignalePar(signaleParId);
                System.out.println("Found " + problems.size() + " problems reported by user " + signaleParId);
                return Response.ok(new ApiResponse<>(true, problems, 
                        String.format("Problèmes reported by user ID %d fetched successfully", signaleParId))).build();
            }
            
            // Filter by resolver
            if (resoluParId != null) {
                List<Probleme> problems = problemeService.findByResoluPar(resoluParId);
                System.out.println("Found " + problems.size() + " problems resolved by user " + resoluParId);
                return Response.ok(new ApiResponse<>(true, problems, 
                        String.format("Problèmes resolved by user ID %d fetched successfully", resoluParId))).build();
            }
            
            // Filter by severity
            if (gravite != null && !gravite.isEmpty()) {
                List<Probleme> problems = problemeService.findByGravite(gravite);
                System.out.println("Found " + problems.size() + " problems with severity " + gravite);
                return Response.ok(new ApiResponse<>(true, problems, 
                        String.format("Problèmes with severity '%s' fetched successfully", gravite))).build();
            }
            
            // Filter by status
            if (statut != null && !statut.isEmpty()) {
                List<Probleme> problems = problemeService.findByStatut(statut);
                System.out.println("Found " + problems.size() + " problems with status " + statut);
                return Response.ok(new ApiResponse<>(true, problems, 
                        String.format("Problèmes with status '%s' fetched successfully", statut))).build();
            }
            
            // No filters, get all
            List<Probleme> allProblems = problemeService.findAll();
            System.out.println("Found " + allProblems.size() + " total problems");
            return Response.ok(new ApiResponse<>(true, allProblems, 
                    "Problèmes fetched successfully")).build();
        } catch (Exception e) {
            System.err.println("Error in getAllProblemes: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, null, "Error fetching problems: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getProbleme(@PathParam("id") Long id) {
        try {
            System.out.println("=== GET PROBLEME BY ID ===");
            System.out.println("Fetching problem with ID: " + id);
            
            return problemeService.findById(id)
                    .map(probleme -> {
                        System.out.println("Found problem: " + probleme.getDescription());
                        return Response.ok(new ApiResponse<>(true, probleme, 
                            String.format("Problème with ID %d fetched successfully", id))).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiResponse<>(false, null, 
                                String.format("Problème with ID %d not found", id))).build());
        } catch (Exception e) {
            System.err.println("Error fetching problem " + id + ": " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, null, "Error fetching problem: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response createProbleme(Probleme probleme) {
        try {
            System.out.println("=== CREATE PROBLEME ===");
            System.out.println("Creating problem: " + probleme.getDescription());
            System.out.println("Type: " + probleme.getType());
            System.out.println("Gravité: " + probleme.getGravite());
            
            Probleme created = problemeService.create(probleme);
            System.out.println("Successfully created problem with ID: " + created.getId());
            
            return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse<>(true, created, 
                        String.format("Problème created successfully with ID %d", created.getId())))
                    .build();
        } catch (Exception e) {
            System.err.println("Error creating problem: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, null, "Error creating problem: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")  
    public Response updateProbleme(@PathParam("id") Long id, Probleme probleme) {
        try {
            System.out.println("=== UPDATE PROBLEME ===");
            System.out.println("Updating problem with ID: " + id);
            System.out.println("New status: " + probleme.getStatut());
            
            return problemeService.update(id, probleme)
                    .map(updated -> {
                        System.out.println("Successfully updated problem: " + updated.getId());
                        return Response.ok(new ApiResponse<>(true, updated, 
                            String.format("Problème with ID %d updated successfully", id))).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiResponse<>(false, null, 
                                String.format("Problème with ID %d not found", id))).build());
        } catch (Exception e) {
            System.err.println("Error updating problem " + id + ": " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, null, "Error updating problem: " + e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProbleme(@PathParam("id") Long id) {
        try {
            System.out.println("=== DELETE PROBLEME ===");
            System.out.println("Deleting problem with ID: " + id);
            
            boolean deleted = problemeService.delete(id);
            if (deleted) {
                System.out.println("Successfully deleted problem: " + id);
                return Response.ok(new ApiResponse<>(true, null, 
                    String.format("Problème with ID %d deleted successfully", id))).build();
            } else {
                System.out.println("Problem not found for deletion: " + id);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Problème with ID %d not found", id))).build();
            }
        } catch (Exception e) {
            System.err.println("Error deleting problem " + id + ": " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, null, "Error deleting problem: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/detect")
    public Response detectProblems(ProblemDetectionRequest request) {
        try {
            System.out.println("=== PROBLEM DETECTION REQUEST ===");
            System.out.println("Daily Report ID: " + request.getDailyReportId());
            System.out.println("Updated Operation IDs: " + request.getUpdatedOperationIds());
            
            if (request.getDailyReportId() == null) {
                System.err.println("Daily report ID is null");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ApiResponse<>(false, null, "Daily report ID is required"))
                        .build();
            }
            
            List<Probleme> detectedProblems = problemeService.detectProblemsFromDailyReport(
                request.getDailyReportId(), request.getUpdatedOperationIds());
            
            System.out.println("Detected " + detectedProblems.size() + " problems");
            for (Probleme problem : detectedProblems) {
                System.out.println("- " + problem.getType() + ": " + problem.getDescription());
            }
            
            return Response.ok(new ApiResponse<>(true, detectedProblems, 
                String.format("Problem detection completed. %d problems detected.", detectedProblems.size()))).build();
        } catch (Exception e) {
            System.err.println("Error in problem detection: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse<>(false, null, "Error during problem detection: " + e.getMessage()))
                    .build();
        }
    }

    // Inner class for request body
    public static class ProblemDetectionRequest {
        private Long dailyReportId;
        private List<Long> updatedOperationIds;
        
        public ProblemDetectionRequest() {
            // Default constructor for JSON deserialization
        }
        
        public Long getDailyReportId() {
            return dailyReportId;
        }
        
        public void setDailyReportId(Long dailyReportId) {
            this.dailyReportId = dailyReportId;
        }
        
        public List<Long> getUpdatedOperationIds() {
            return updatedOperationIds;
        }
        
        public void setUpdatedOperationIds(List<Long> updatedOperationIds) {
            this.updatedOperationIds = updatedOperationIds;
        }
        
        @Override
        public String toString() {
            return "ProblemDetectionRequest{" +
                    "dailyReportId=" + dailyReportId +
                    ", updatedOperationIds=" + updatedOperationIds +
                    '}';
        }
    }
}
