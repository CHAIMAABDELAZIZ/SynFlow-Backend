package com.example.backend.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.example.backend.model.Activite;
import com.example.backend.model.ApiResponse;
import com.example.backend.service.ActiviteService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
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
@Path("/activites")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActiviteResource {

    private final ActiviteService activiteService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE_TIME;

    public ActiviteResource(ActiviteService activiteService) {
        this.activiteService = activiteService;
    }

    @GET
    public Response getAllActivites(
            @QueryParam("utilisateurId") Long utilisateurId,
            @QueryParam("type") String type,
            @QueryParam("dateDebut") String dateDebutStr,
            @QueryParam("dateFin") String dateFinStr,
            @QueryParam("typeEntite") String typeEntite,
            @QueryParam("entiteConcerne") String entiteConcerne,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        
        // Handle paginated results for large datasets
        if (page >= 0 && size > 0) {
            PageRequest pageable = PageRequest.of(page, size, Sort.by("date").descending());
            
            if (utilisateurId != null) {
                Page<Activite> activitesPage = activiteService.findByUtilisateurPaginated(utilisateurId, pageable);
                return Response.ok(new ApiResponse<>(true, activitesPage, 
                        String.format("Activités for user ID %d fetched successfully (page %d)", 
                                utilisateurId, page))).build();
            }
            
            Page<Activite> activitesPage = activiteService.findAllPaginated(pageable);
            return Response.ok(new ApiResponse<>(true, activitesPage, 
                    String.format("Activités fetched successfully (page %d)", page))).build();
        }
        
        // Filter by user
        if (utilisateurId != null) {
            return Response.ok(new ApiResponse<>(true, activiteService.findByUtilisateur(utilisateurId), 
                    String.format("Activités for user ID %d fetched successfully", utilisateurId))).build();
        }
        
        // Filter by type
        if (type != null && !type.isEmpty()) {
            return Response.ok(new ApiResponse<>(true, activiteService.findByType(type), 
                    String.format("Activités of type '%s' fetched successfully", type))).build();
        }
        
        // Filter by date range
        if (dateDebutStr != null && dateFinStr != null) {
            try {
                LocalDateTime dateDebut = LocalDateTime.parse(dateDebutStr, dateFormatter);
                LocalDateTime dateFin = LocalDateTime.parse(dateFinStr, dateFormatter);
                return Response.ok(new ApiResponse<>(true, activiteService.findByDateRange(dateDebut, dateFin), 
                        "Activités within date range fetched successfully")).build();
            } catch (DateTimeParseException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ApiResponse<>(false, null, 
                                "Invalid date format. Use ISO date time format (yyyy-MM-ddTHH:mm:ss)"))
                        .build();
            }
        }
        
        // Filter by entity type
        if (typeEntite != null && !typeEntite.isEmpty()) {
            return Response.ok(new ApiResponse<>(true, activiteService.findByTypeEntite(typeEntite), 
                    String.format("Activités for entity type '%s' fetched successfully", typeEntite))).build();
        }
        
        // Filter by concerned entity
        if (entiteConcerne != null && !entiteConcerne.isEmpty()) {
            return Response.ok(new ApiResponse<>(true, activiteService.findByEntiteConcerne(entiteConcerne), 
                    String.format("Activités for entity '%s' fetched successfully", entiteConcerne))).build();
        }
        
        // No filters, get all
        return Response.ok(new ApiResponse<>(true, activiteService.findAll(), 
                "Activités fetched successfully")).build();
    }

    @GET
    @Path("/{id}")
    public Response getActivite(@PathParam("id") Long id) {
        return activiteService.findById(id)
                .map(activite -> Response.ok(new ApiResponse<>(true, activite, 
                    String.format("Activité with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Activité with ID %d not found", id))).build());
    }

    @POST
    public Response createActivite(Activite activite) {
        Activite created = activiteService.create(activite);
        return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse<>(true, created, 
                    String.format("Activité created successfully with ID %d", created.getId())))
                .build();
    }
    
    @POST
    @Path("/log")
    public Response logActivite(
            @QueryParam("utilisateurId") Long utilisateurId,
            @QueryParam("type") String type,
            @QueryParam("description") String description,
            @QueryParam("entiteConcerne") String entiteConcerne,
            @QueryParam("typeEntite") String typeEntite) {
        
        if (utilisateurId == null || type == null || description == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, null, 
                        "utilisateurId, type, and description are required"))
                    .build();
        }
        
        Activite created = activiteService.logActivity(
                utilisateurId, type, description, entiteConcerne, typeEntite);
        
        return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse<>(true, created, 
                    String.format("Activité logged successfully with ID %d", created.getId())))
                .build();
    }

    @PUT
    @Path("/{id}")  
    public Response updateActivite(@PathParam("id") Long id, Activite activite) {
        return activiteService.update(id, activite)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Activité with ID %d updated successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Activité with ID %d not found", id))).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteActivite(@PathParam("id") Long id) {
        return activiteService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Activité with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Activité with ID %d not found", id))).build();
    }
}
