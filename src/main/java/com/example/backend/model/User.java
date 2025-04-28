package com.example.backend.model;

import java.sql.Date;

public class User {
    private Long pid;
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private String role;
    private Date createdAt;
    private Date lastConnection;
    private String status;

    // Getters
    public Long getPid() {
        return pid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getLastConnection() {
        return lastConnection;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setLastConnection(Date lastConnection) {
        this.lastConnection = lastConnection;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
