package com.example.kursovaya.models;

import java.io.Serializable;

public class Ticket implements Serializable {
    private int idTicket;
    private int idClient;
    private int price;
    private int idFlight;
    private boolean extraConditions;
    private String departmentCity;
    private String arrivalCity;

    public Ticket(int idTicket, int idClient, int price, int idFlight,
                  boolean extraConditions, String departmentCity, String arrivalCity){
        this.idTicket = idTicket;
        this.idClient = idClient;
        this.price = price;
        this.idFlight = idFlight;
        this.extraConditions = extraConditions;
        this.departmentCity = departmentCity;
        this.arrivalCity = arrivalCity;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getprice() {
        return price;
    }

    public void setprice(int price) {
        this.price = price;
    }

    public int getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(int idFlight) {
        this.idFlight = idFlight;
    }

    public boolean getExtraConditions() {
        return extraConditions;
    }

    public void setExtraConditions(boolean extraConditions) {
        this.extraConditions = extraConditions;
    }

    public String getDepartmentCity() {
        return departmentCity;
    }

    public void setDepartmentCity(String departmentCity) {
        this.departmentCity = departmentCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }
}
