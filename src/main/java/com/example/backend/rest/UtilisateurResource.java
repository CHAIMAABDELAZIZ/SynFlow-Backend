package com.example.backend.rest;

import org.springframework.stereotype.Component;

import com.example.backend.dto.AuthRequest;
import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.model.ApiResponse;
import com.example.backend.model.Utilisateur;
import com.example.backend.service.UtilisateurService;

import jakarta.validation.Valid;
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
@Path("/utilisateurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UtilisateurResource {

    private final UtilisateurService utilisateurService;

    public UtilisateurResource(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequest registerRequest) {
        try {
            AuthResponse authResponse = utilisateurService.register(registerRequest);
            return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse<>(true, authResponse, "User registered successfully"))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, null, e.getMessage()))
                    .build();
        }
    }
    
    @POST
    @Path("/login")
    public Response login(@Valid AuthRequest authRequest) {
        try {
            AuthResponse authResponse = utilisateurService.login(authRequest);
            return Response.ok(new ApiResponse<>(true, authResponse, "Login successful"))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ApiResponse<>(false, null, e.getMessage()))
                    .build();
        }
    }
    
    @POST
    @Path("/{id}/logout")
    public Response logout(@PathParam("id") Long id) {
        return utilisateurService.logout(id)
                ? Response.ok(new ApiResponse<>(true, null, "Logout successful"))
                        .build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("User with ID %d not found", id)))
                        .build();
    }

    @GET
    public Response getAllUsers() {
        return Response.ok(new ApiResponse<>(true, utilisateurService.findAll(), 
                "Users fetched successfully")).build();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") Long id) {
        return utilisateurService.findById(id)
                .map(user -> Response.ok(new ApiResponse<>(true, user, 
                    String.format("User with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("User with ID %d not found", id))).build());
    }
    
    @GET
    @Path("/profile/{id}")
    public Response getUserProfile(@PathParam("id") Long id) {
        return utilisateurService.findById(id)
                .map(user -> Response.ok(new ApiResponse<>(true, user, 
                    "User profile fetched successfully")).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, "User not found")).build());
    }

    @PUT
    @Path("/{id}")  
    public Response updateUser(@PathParam("id") Long id, Utilisateur user) {
        try {
            return utilisateurService.update(id, user)
                    .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                        String.format("User with ID %d updated successfully", id))).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiResponse<>(false, null, 
                                String.format("User with ID %d not found", id))).build());
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, null, e.getMessage()))
                    .build();
        }
    }
    
    @PUT
    @Path("/{id}/password")
    public Response updatePassword(
            @PathParam("id") Long id, 
            java.util.Map<String, String> passwordData) {
        
        String currentPassword = passwordData.get("currentPassword");
        String newPassword = passwordData.get("newPassword");
        
        if (currentPassword == null || newPassword == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, null, 
                        "Current password and new password are required"))
                    .build();
        }
        
        try {
            return utilisateurService.updatePassword(id, currentPassword, newPassword)
                    .map(user -> Response.ok(new ApiResponse<>(true, null, 
                        "Password updated successfully")).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiResponse<>(false, null, 
                                String.format("User with ID %d not found", id))).build());
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse<>(false, null, e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        return utilisateurService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("User with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("User with ID %d not found", id))).build();
    }
}
