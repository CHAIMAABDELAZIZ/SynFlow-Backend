package com.example.backend.utils;

import java.sql.*;
import java.util.Properties;

public class OracleConnection {

    // Méthode pour obtenir une connexion à la base de données Oracle
    public static Connection getConnection() {
        String url = "jdbc:oracle:thin:@localhost:1521/FREEPDB1";
        String user = "sys";
        String password = "root";

        try {
            // Créer des propriétés de connexion pour inclure le sysdba
            Properties properties = new Properties();
            properties.setProperty("user", user);
            properties.setProperty("password", password);
            properties.setProperty("internal_logon", "sysdba"); // Spécifier sysdba ici

            // Charger le driver JDBC Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Se connecter à la base de données
            Connection conn = DriverManager.getConnection(url, properties);
            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            // Gérer les erreurs de connexion
            e.printStackTrace();
            return null;
        }
    }

    // Méthode principale pour tester la connexion
    public static void main(String[] args) {
        // Appeler getConnection pour tester la connexion à la base de données
        Connection conn = getConnection();

        if (conn != null) {
            System.out.println("Connexion réussie à Oracle Database");
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Échec de la connexion à la base de données.");
        }
    }
}
