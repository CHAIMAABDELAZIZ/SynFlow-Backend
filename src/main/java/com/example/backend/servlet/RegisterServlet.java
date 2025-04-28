package com.example.backend.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.backend.controller.UserController;
import com.example.backend.model.User;

@WebServlet("/backend")
public class RegisterServlet extends HttpServlet {

    // Méthode qui sera appelée pour gérer les requêtes POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lire le corps de la requête JSON
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }

        // Convertir le JSON en un objet Java
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(sb.toString(), User.class); // Assumes you have a User class with fields like
                                                                       // firstName, lastName, etc.

        // Vérifier si tous les champs sont remplis
        if (user.getFirstName() == null || user.getFirstName().isEmpty() ||
                user.getFirstName() == null || user.getFirstName().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getRole() == null || user.getRole().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Tous les champs doivent être remplis.\"}");
            return;
        }

        // Appel à la méthode registerUser et récupération de la réponse
        boolean isRegistered = UserController.registerUser(user.getFirstName(), user.getFirstName(), user.getEmail(),
                user.getPassword(), user.getRole());

        // Envoi de la réponse JSON avec le message du contrôleur
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (isRegistered) {
            out.println("{\"message\": \"Utilisateur enregistré avec succès !\"}");
        } else {
            out.println("{\"message\": \"Erreur lors de l'enregistrement de l'utilisateur.\"}");
        }
        out.flush();
    }
}
