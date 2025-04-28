package com.example.backend;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import com.example.backend.servlet.RegisterServlet;

public class JettyServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080); // Choisis le port sur lequel tu veux que ton app écoute

        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping((Class<? extends Servlet>) RegisterServlet.class, "/register");

        server.setHandler(handler);
        server.start();
        server.join();
    }
}
