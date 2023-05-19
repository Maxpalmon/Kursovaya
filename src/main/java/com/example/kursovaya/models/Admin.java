package com.example.kursovaya.models;

import java.io.Serializable;

public class Admin  extends User implements Serializable {
    private int idAdmin;

    public Admin(int idAdmin, int idUser, String username, String email, String password, String userType){
        super(idUser, username, email, password, userType);
        this.idAdmin = idAdmin;
    }
    public Admin(int idAdmin,User user){
        super(user.idUser, user.username, user.email, user.password, user.userType);
        this.idAdmin = idAdmin;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

}
