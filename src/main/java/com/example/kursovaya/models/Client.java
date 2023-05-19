package com.example.kursovaya.models;

import java.io.Serializable;
import java.sql.Date;

public class Client extends User implements Serializable {
    private int idClient;

    public Client(int idClient, int idUser, String username, String email, String password, String userType){
        super(idUser, username, email, password, userType);
        this.idClient = idClient;
    }
    public Client(int idClient,User user){
        super(user.idUser, user.username, user.email, user.password, user.userType);
        this.idClient = idClient;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }
}
