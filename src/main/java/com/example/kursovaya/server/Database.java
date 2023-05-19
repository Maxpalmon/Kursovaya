package com.example.kursovaya.server;

import com.example.kursovaya.models.Ticket;

import java.sql.*;

import java.util.ArrayList;
import java.util.Objects;

public class Database {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/app";
    private static final String user = "root";
    private static final String password = "123qweASD";

    public static Connection con;
    private static ResultSet rs;
    Database() {
        try
        {
            con = DriverManager.getConnection(url, user, password);
            if (con != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ArrayList<Ticket> getClientData(int idClient){    //Возвращает информацию о приобретенных билетах конкретного клиента
        ArrayList<Ticket> tickets = new ArrayList<>();
        try {
            int idTicket;
            PreparedStatement preparedStatement = con.prepareStatement("select * from ticket where idClient = ?");
            preparedStatement.setInt(1, idClient);      //Вставляем номер клиента в SQL запрос
            ResultSet multirs = preparedStatement.executeQuery();
            while (multirs.next()){
                idTicket = multirs.getInt("idTicket");
                int price = multirs.getInt(3);
                int idFlight = multirs.getInt(4);
                boolean extraConditions = multirs.getBoolean(5);
                String departmentCity = multirs.getString(6);
                String arrivalCity = multirs.getString(7);
                tickets.add(new Ticket(idTicket, idClient, price, idFlight, extraConditions, departmentCity, arrivalCity));
            }
        }catch (SQLException e){
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
        return tickets;
    }

    //Возвращает информацию о всех билетах
    ArrayList<Ticket> getTicketData(){
        ArrayList<Ticket> tickets = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select * from ticket");
            ResultSet multirs = preparedStatement.executeQuery();
            while (multirs.next()){
                int idTicket = multirs.getInt("idTicket");
                int idClient = multirs.getInt("idClient");
                int price = multirs.getInt("price");
                int idFlight = multirs.getInt("idFlight");
                boolean extraConditions = multirs.getBoolean("extraConditions");
                String departmentCity = multirs.getString("departmentCity");
                String arrivalCity = multirs.getString("arrivalCity");
                tickets.add(new Ticket(idTicket, idClient, price, idFlight, extraConditions, departmentCity, arrivalCity));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return  tickets;
    }
    //Возвращает информацию о всех не приобретенных билетах
    ArrayList<Ticket> getFreeTicketData(){
        ArrayList<Ticket> tickets = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select * from ticket where idClient = 0");
            ResultSet multirs = preparedStatement.executeQuery();
            while (multirs.next()){
                int idTicket = multirs.getInt("idTicket");
                int idClient = multirs.getInt("idClient");
                int price = multirs.getInt("price");
                int idFlight = multirs.getInt("idFlight");
                boolean extraConditions = multirs.getBoolean("extraConditions");
                String departmentCity = multirs.getString("departmentCity");
                String arrivalCity = multirs.getString("arrivalCity");
                tickets.add(new Ticket(idTicket, idClient, price, idFlight, extraConditions, departmentCity, arrivalCity));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return  tickets;
    }

public int checkCredentials(String login, String password){
    try {
        PreparedStatement preparedStatement = con.prepareStatement("select idUser from credentials where login = ? and password = ?");
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        rs = preparedStatement.executeQuery();
        int idUser = 0;
        if(rs.next()){
            idUser = rs.getInt("idUser");
        }
        return idUser;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

    public int getClientId(int userId){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select * from client where client.idUser = ?");
            preparedStatement.setInt(1, userId);
            rs = preparedStatement.executeQuery();
            int clientId = 0;
            if(rs.next()){
                clientId = rs.getInt(1);
            }
            return clientId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

     public int getAdminId(int userId) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select * from admin where admin.idUser = ?");
            preparedStatement.setInt(1, userId);
            rs = preparedStatement.executeQuery();
            int adminId = 0;
            if(rs.next()){
                adminId = rs.getInt(1);
            }
            return adminId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean isLoginFree(String login) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select * from credentials where credentials.login = ?");
            preparedStatement.setString(1, login);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                return false;
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getUserAvailableId(){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select max(idUser) from user");
            rs = preparedStatement.executeQuery();
            int availableId = 0;
            if(rs.next()){
                availableId = rs.getInt(1);
            }
            return availableId + 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    int getClientAvailableId(){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select max(idClient) from client");
            rs = preparedStatement.executeQuery();
            int availableId = 0;
            if(rs.next()){
                availableId = rs.getInt(1);
            }
            return availableId + 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private int getTicketAvailibleId() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select max(idTicket) from ticket");
            rs = preparedStatement.executeQuery();
            int availableId = 0;
            if(rs.next()){
                availableId = rs.getInt(1);
            }
            return availableId + 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void insertClient(String login, String password, String email){
        try {
            int userId = getUserAvailableId();
            int clientId = getClientAvailableId();
            PreparedStatement preparedStatement = con.prepareStatement("insert into user (idUser, login, password, email) values (?, ?, ?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, email);
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement("insert into client (idClient, idUser) values (?, ?)");
            preparedStatement.setInt(1, clientId);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement("insert into credentials (idUser, login, password) values (?, ?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    void insertAdmin(String login, String password, String email){
        try {
            int userId = getUserAvailableId();
            int adminId = getClientAvailableId();
            PreparedStatement preparedStatement = con.prepareStatement("insert into user (idUser, login, password, email) values (?, ?, ?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, email);
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement("insert into admin (idAdmin, idUser) values (?, ?)");
            preparedStatement.setInt(1, adminId);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement("insert into credentials (idUser, login, password) values (?, ?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertTicket(String[] content) {
        try {
            int idTicket = getTicketAvailibleId();
            int idClient = 0;
            PreparedStatement preparedStatement = con.prepareStatement("insert into ticket (idTicket, idClient, price, idFlight, extraConditions, departmentCity, arrivalCity) values (?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, idTicket);
            preparedStatement.setInt(2,idClient);
            preparedStatement.setInt(3, Integer.parseInt(content[0]));
            preparedStatement.setInt(4, Integer.parseInt(content[1]));
            if(Objects.equals(content[2], "0")) preparedStatement.setBoolean(5, false);
            else preparedStatement.setBoolean(5, true);
            preparedStatement.setString(6, content[3]);
            preparedStatement.setString(7, content[4]);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void buyTicket(int[] content) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("update ticket set idClient  = ? where idTicket = ?");
            preparedStatement.setInt(1, content[1]);
            preparedStatement.setInt(2, content[0]);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeTicketInfo(String[] content) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("update ticket set price = ?, idFlight = ?, extraConditions = ?, departmentCity = ?, arrivalCity = ? where idTicket = ?");
            preparedStatement.setInt(1, Integer.parseInt(content[1]));
            preparedStatement.setInt(2, Integer.parseInt(content[2]));
            preparedStatement.setBoolean(3, Boolean.parseBoolean(content[3]));
            preparedStatement.setString(4, content[4]);
            preparedStatement.setString(5, content[5]);
            preparedStatement.setInt(6, Integer.parseInt(content[0]));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTicketInfo(int idTicket) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("delete from ticket where idTicket = ?");
            preparedStatement.setInt(1, idTicket);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void returnTicketRequest(int idTicket) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("update ticket set idClient = 0 where idTicket = ?");
            preparedStatement.setInt(1, idTicket);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
