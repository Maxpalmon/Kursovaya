package com.example.kursovaya.models;

import java.io.Serializable;

public class User implements Serializable {
    protected int idUser;
    protected String username;
    protected String email;
    protected String password;
    protected String userType;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public User(int idUser, String username, String email, String password, String userType) {
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }
}
