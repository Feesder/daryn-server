package com.boyapcky.bookworld.model;

public class User {
    Long id;
    String username;
    String email;
    String password;
    String created;
    String updated;
    String status;

    public User(Long id, String username, String email, String password, String created, String updated, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.created = created;
        this.updated = updated;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
