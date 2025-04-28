package com.example.backend.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.backend.utils.OracleConnection;

public class UserController {

    // Méthode pour enregistrer l'utilisateur dans la base de données
    public static boolean registerUser(String firstName, String lastName, String email, String password, String role) {
        // Validation des champs requis
        if (firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty() ||
                role == null || role.isEmpty()) {
            System.out.println("Erreur: Tous les champs doivent être remplis.");
            return false;
        }

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = OracleConnection.getConnection();
            if (conn == null) {
                System.out.println("Erreur de connexion à la base de données.");
                return false;
            }

            String query = "INSERT INTO Users (first_name, last_name, email, password, role) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, role);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                return true; // Succès
            } else {
                return false; // Échec
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'enregistrement de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
            return false; // Échec en cas d'exception
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
