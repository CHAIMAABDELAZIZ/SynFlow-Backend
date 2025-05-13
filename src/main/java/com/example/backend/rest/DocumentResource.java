package com.example.backend.rest;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.Document;
import com.example.backend.service.DocumentService;

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
@Path("/documents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentResource {

    private final DocumentService documentService;

    public DocumentResource(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GET
    public Response getAllDocuments(
            @QueryParam("puitId") Long puitId,
            @QueryParam("phaseId") Long phaseId,
            @QueryParam("operationId") Long operationId,
            @QueryParam("utilisateurId") Long utilisateurId,
            @QueryParam("estPublic") Boolean estPublic,
            @QueryParam("type") String type) {
        
        // Filter by puit
        if (puitId != null) {
            return Response.ok(new ApiResponse<>(true, documentService.findByPuit(puitId), 
                    String.format("Documents for puit ID %d fetched successfully", puitId))).build();
        }
        
        // Filter by phase
        if (phaseId != null) {
            return Response.ok(new ApiResponse<>(true, documentService.findByPhase(phaseId), 
                    String.format("Documents for phase ID %d fetched successfully", phaseId))).build();
        }
        
        // Filter by operation
        if (operationId != null) {
            return Response.ok(new ApiResponse<>(true, documentService.findByOperation(operationId), 
                    String.format("Documents for operation ID %d fetched successfully", operationId))).build();
        }
        
        // Filter by uploader
        if (utilisateurId != null) {
            return Response.ok(new ApiResponse<>(true, documentService.findByUploadePar(utilisateurId), 
                    String.format("Documents uploaded by user ID %d fetched successfully", utilisateurId))).build();
        }
        
        // Filter by public status
        if (estPublic != null) {
            return Response.ok(new ApiResponse<>(true, documentService.findByEstPublic(estPublic), 
                    String.format("Documents with public status %b fetched successfully", estPublic))).build();
        }
        
        // Filter by document type
        if (type != null && !type.isEmpty()) {
            return Response.ok(new ApiResponse<>(true, documentService.findByType(type), 
                    String.format("Documents of type '%s' fetched successfully", type))).build();
        }
        
        // No filters, get all
        return Response.ok(new ApiResponse<>(true, documentService.findAll(), 
                "Documents fetched successfully")).build();
    }

    @GET
    @Path("/{id}")
    public Response getDocument(@PathParam("id") Long id) {
        return documentService.findById(id)
                .map(document -> Response.ok(new ApiResponse<>(true, document, 
                    String.format("Document with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Document with ID %d not found", id))).build());
    }

    @POST
    public Response createDocument(Document document) {
        Document created = documentService.create(document);
        return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse<>(true, created, 
                    String.format("Document created successfully with ID %d", created.getId())))
                .build();
    }

    @PUT
    @Path("/{id}")  
    public Response updateDocument(@PathParam("id") Long id, Document document) {
        return documentService.update(id, document)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Document with ID %d updated successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Document with ID %d not found", id))).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDocument(@PathParam("id") Long id) {
        return documentService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Document with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Document with ID %d not found", id))).build();
    }
}
