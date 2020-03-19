package com.example.ia2;

public class User {
    private String name;
    private String username;
    private String password;
    private String circleId;
    private boolean isAdmin;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getCircleId() {
        return circleId;
    }
    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public User () {

    }
}
