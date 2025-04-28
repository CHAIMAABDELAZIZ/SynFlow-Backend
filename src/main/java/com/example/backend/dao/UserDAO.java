package com.example.backend.dao;

import com.example.backend.model.User;
import com.example.backend.utils.OracleConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    public void createUser(String firstName, String lastName, String email, String password, String role) {
        try (Connection conn = OracleConnection.getConnection()) {
            String query = "INSERT INTO Users (nom, prenom, email, password, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, role);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
