package com.example.kursovaya.client;

import com.example.kursovaya.models.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection implements Runnable{
    static Socket socket;
    static ObjectOutputStream out;
    static ObjectInputStream in;

    ServerConnection(){
        try{
            socket = new Socket("localhost", 5548);
            DataOutputStream o = new DataOutputStream(socket.getOutputStream());
            out = new ObjectOutputStream(o);
            DataInputStream i = new DataInputStream(socket.getInputStream());
            in = new ObjectInputStream(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginRequest(String login, String password){
        try {
            String[] content = new String[2];
            content[0] = login;
            content[1] = password;
            Message message = new Message(Requests.loginRequest, true, content);
            out.writeObject(message);
            out.flush();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void registerRequest(String login, String password, String mail, boolean userType){
        try {
            String[] content = new String[4];
            content[0] = login;
            content[1] = password;
            content[2] = mail;
            if(userType){content[3] = "Admin";}
                else content[3] = "Client";
            Message message = new Message(Requests.registerRequest, true, content);
            System.out.println(out);
            out.writeObject(message);
            out.flush();
        }catch (IOException e) {
            throw new RuntimeException(e);
            }
    }
    public void disconnect(){
        try {
            Message message = new Message(Requests.disconnect, true, null);
            out.writeObject(message);
            out.flush();
            out.close();
            in.close();
            socket.close();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

   void getClientDataRequest(int idClient) {
        try{
            Message message = new Message(Requests.getClientDataRequest, true, idClient);
            out.writeObject(message);
            out.flush();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    void getAdminDataRequest(int idAdmin) {
        try{
            Message message = new Message(Requests.getAdminDataRequest, true, idAdmin);
            out.writeObject(message);
            out.flush();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void getUserDataRequest(int idUser) {
        try{
            Message message = new Message(Requests.getUserDataRequest, true, idUser);
            out.writeObject(message);
            out.flush();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void buyTicketRequest(int idTicket) {
        try{
            int content[] = new int[2];
            content[0] = idTicket;
            content[1] = ClientApplication.session.getSpecificId();
            Message message = new Message(Requests.buyTicketRequest, true, content);
            out.writeObject(message);
            out.flush();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void addTicketRequest(String price, String idFlight, boolean extraConditions, String departmentCity, String arrivalCity) {
        try {
            String[] content = new String[5];
            content[0] = price;
            content[1] = idFlight;
            if(extraConditions){content[2] = "1";}
            else content[2] = "0";
            content[3] = departmentCity;
            content[4] = arrivalCity;
            Message message = new Message(Requests.addTicketRequest, true, content);
            out.writeObject(message);
            out.flush();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void goToShopRequest() {
        try {
            Message message = new Message(Requests.goToShopRequest, true, null);
            out.writeObject(message);
            out.flush();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void changeTicketDataRequest(String[] content) {
        try {
            Message message = new Message(Requests.changeTicketDataRequest, true, content);
            out.writeObject(message);
            out.flush();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteTicketDataRequest(int idTicket) {
        try {
            Message message = new Message(Requests.deleteTicketDataRequest, true, idTicket);
            out.writeObject(message);
            out.flush();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void returnTicketRequest(int idTicket) {
        try {
            Message message = new Message(Requests.returnTicketRequest, true, idTicket);
            out.writeObject(message);
            out.flush();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try{
            while (!socket.isClosed()){
                Message entry = null;
                entry = (Message) in.readObject();
                Message finalEntry = entry;
                if(finalEntry!=null){

                    Platform.runLater(() -> {
                        switch (finalEntry.type) {
                            case loginRequest -> {
                                System.out.println("Its loginRequest");
                                if (finalEntry.status) {
                                    Object[] content = (Object[]) finalEntry.message;
                                    int userId = (Integer) content[0];
                                    UserType userType = UserType.valueOf((String) content[1]);
                                    int specificId = (Integer) content[2];
                                    ClientApplication.session.setUserId(userId);
                                    ClientApplication.session.setUserType(userType);
                                    ClientApplication.session.setSpecificId(specificId);
                                    switch (userType) {
                                        case Client -> getClientDataRequest(specificId);
                                        case Admin -> getAdminDataRequest(specificId);
                                    }
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Ошибка авторизации");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Пользователь с таким набором данных не найден");
                                    alert.showAndWait();
                                }
                            }
                            case registerRequest -> {
                                System.out.println("Its registerRequest request");
                                if (finalEntry.status) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Пользователь зарегестрирован");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Пользователь был успешно зарегестрирован.");
                                    alert.showAndWait();
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Ошибка регистрации");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Данный логин занят");
                                    alert.showAndWait();
                                }
                            }
                            case getClientDataRequest -> {
                                if (finalEntry.status) {
                                    ArrayList<Ticket> tickets = (ArrayList<Ticket>) finalEntry.message;
                                    if (tickets != null)
                                        ClientApplication.stage.setUserData(tickets);
                                    ClientApplication.app.switchScene("ClientView.fxml");
                                }
                            }
                            case getAdminDataRequest -> {
                                if (finalEntry.status) {
                                    ArrayList<Ticket> tickets = (ArrayList<Ticket>) finalEntry.message;
                                    if (tickets != null)
                                        ClientApplication.stage.setUserData(tickets);
                                    ClientApplication.app.switchScene("AdminView.fxml");
                                }
                            }
                            case buyTicketRequest -> {
                                System.out.println("Its buyTicketRequest");
                                if (finalEntry.status) {
                                    ArrayList<Ticket> tickets = (ArrayList<Ticket>) finalEntry.message;
                                    if (tickets != null)
                                        ClientApplication.stage.setUserData(tickets);
                                }
                            }
                            case addTicketRequest -> {
                                System.out.println("Its addTicketRequest");
                                if(finalEntry.status){
                                    ArrayList<Ticket> tickets = (ArrayList<Ticket>) finalEntry.message;
                                    ClientApplication.stage.setUserData(tickets);
                                }
                            }
                            case goToShopRequest -> {
                                System.out.println("Its goToShopRequest");
                                if(finalEntry.status){
                                    ArrayList<Ticket> tickets = (ArrayList<Ticket>) finalEntry.message;
                                    ClientApplication.stage.setUserData(tickets);
                                    ClientApplication.app.switchScene("BuyTicketView.fxml");
                                }
                            }
                            case changeTicketDataRequest -> {
                                System.out.println("Its changeTicketRequest");
                                if(finalEntry.status){
                                    ArrayList<Ticket> tickets = (ArrayList<Ticket>) finalEntry.message;
                                    ClientApplication.stage.setUserData(tickets);
                                }
                            }
                            case deleteTicketDataRequest -> {
                                System.out.println("Its deleteRequest");
                                if(finalEntry.status){
                                    ArrayList<Ticket> tickets = (ArrayList<Ticket>) finalEntry.message;
                                    ClientApplication.stage.setUserData(tickets);
                                }
                            }
                            case returnTicketRequest -> {
                                System.out.println("Its returnTicketRequest");
                                if(finalEntry.status){
                                    ArrayList<Ticket> tickets = (ArrayList<Ticket>) finalEntry.message;
                                    ClientApplication.stage.setUserData(tickets);
                                }
                            }
                        }
                    });
                }
            }
        } catch (IOException e){
        } catch (ClassNotFoundException e) {
        }
    }



}
