package com.example.backend;

import java.io.*;

import com.example.backend.controller.UserController;

public class Main {
    public static void main(String[] args) {
        // Créer un PrintWriter pour écrire dans la sortie (le terminal)
        PrintWriter out = new PrintWriter(System.out, true);

        // Message JSON à afficher dans le terminal
        String json = "{ \"message\": \"Hello from Java! 👋\" }";

        // Afficher le message dans le terminal
        out.println(json);
        UserController.registerUser("Chaimaa", "B.", "chaimaa2@example.com", "secret123", "admin");
    }
    }

