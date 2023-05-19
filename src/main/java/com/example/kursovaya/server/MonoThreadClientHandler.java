package com.example.kursovaya.server;

import com.example.kursovaya.models.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.kursovaya.server.MultiThreadServer.db;
public class MonoThreadClientHandler implements Runnable {

    private static Socket clientDialog;
    private static int count = 0;
    public int id;

    private Logger log;
    public MonoThreadClientHandler(Socket client, Logger log) {
        this.log = log;
        clientDialog = client;
        this.id = count++;
    }

    @Override
    public void run() {

        try {
            DataOutputStream o = new DataOutputStream(clientDialog.getOutputStream());
            ObjectOutputStream out = new ObjectOutputStream(o);
            DataInputStream i = new DataInputStream(clientDialog.getInputStream());
            ObjectInputStream in = new ObjectInputStream(i);
            log.log(Level.INFO, "Client <" + this.id + "> has connected.");
            while (!clientDialog.isClosed()) {
                Message input = (Message) in.readObject();
                Message output;
                switch(input.type) {
                    case loginRequest -> {
                        String[] content = (String[]) input.message;
                        log.log(Level.INFO, "Client <" + this.id + "> sent loginRequest with login '"+content[0]+"' and password '"+content[1]+"'");
                        int userId = db.checkCredentials(content[0], content[1]);
                        if(userId != 0) {
                            Object [] contentOut = new Object[3];
                            contentOut[0] = userId;         //Номер пользователя в проверке на права(CheckCredentials)
                            int specificId = 0;
                            if((specificId = db.getClientId(userId))!= 0){
                                contentOut[1] = UserType.Client.toString();
                            }
                            else if((specificId = db.getAdminId(userId)) !=0){
                                contentOut[1] = UserType.Admin.toString();
                            }
                            contentOut[2] = specificId;
                            output = new Message(Requests.loginRequest, true, contentOut);
                            log.log(Level.INFO, "Server replied to Client <" + this.id + "> loginRequest with status=True userId="+contentOut[0]+" userType="+contentOut[1]+" specificId="+contentOut[2]);
                        } else {
                            output = new Message(Requests.loginRequest, false, null);
                            log.log(Level.INFO, "Server replied to Client <" + this.id + "> loginRequest with status=False");
                        }
                        out.writeObject(output);
                        out.flush();
                    }
                    case registerRequest -> {
                        String[] content = (String[]) input.message;
                        String login = content[0];
                        String password = content[1];
                        String email = content[2];
                        UserType type = UserType.valueOf(content[3]);
                        log.log(Level.INFO, "Client <" + this.id + "> sent registerRequest with userType="+type+" login="+login+" password="+password+" email="+email);
                        if(db.isLoginFree(login)){
                            switch (type) {
                                case Client -> {
                                    db.insertClient(login, password, email);
                                }
                                case Admin -> {
                                    db.insertAdmin(login, password, email);
                                }
                            }
                            output = new Message(Requests.registerRequest, true, null);
                            log.log(Level.INFO, "Server replied to Client <" + this.id + "> registerRequest with status=True");
                        } else {
                            output = new Message(Requests.registerRequest, false, null);
                            log.log(Level.INFO, "Server replied to Client <" + this.id + "> registerRequest with status=False");
                        }
                        out.writeObject(output);
                        out.flush();
                    }
                    case disconnect -> {
                        log.log(Level.INFO, "Client <" + this.id + "> has disconnected.");
                        System.out.println("Client disconnected");
                        System.out.println("Closing connections & channels.");
                        in.close();
                        out.close();
                        clientDialog.close();
                        System.out.println("Closing connections & channels - DONE.");
                    }
                    case getClientDataRequest -> {
                        int idClient = (Integer) input.message;
                        log.log(Level.INFO, "Client <" + this.id + "> sent getClientDataRequest with idClient="+idClient);
                        ArrayList<Ticket> tickets = db.getClientData(idClient);
                        log.log(Level.INFO, "Server replied to Client <" + this.id + "> getClientDataRequest with status=true and list of tickets");
                        output = new Message(Requests.getClientDataRequest, true, tickets);
                        out.writeObject(output);
                        out.flush();
                    }
                    case getAdminDataRequest -> {
                        int idAdmin = (Integer) input.message;
                        log.log(Level.INFO, "Admin <" + this.id + "> sent getAdminDataRequest with idAdmin="+idAdmin);
                        ArrayList<Ticket> tickets = db.getTicketData();
                        log.log(Level.INFO, "Server replied to Admin <" + this.id + "> getAdminDataRequest with status=true and list of flights");
                        output = new Message(Requests.getAdminDataRequest, true, tickets);
                        out.writeObject(output);
                        out.flush();
                    }
                    case addTicketRequest -> {
                        String[] content = (String[]) input.message;
                        String price = content[0];
                        String idFlight = content[1];
                        String extraConditions = content[2];
                        String departmentCity = content[3];
                        String arrivalCity = content[4];
                        log.log(Level.INFO, "Admin <" + this.id + "> sent addTicketRequest with price="+price+" idFlight="+idFlight+" extraConditions="+extraConditions+" departmentCity="+departmentCity+" arrivalCity="+arrivalCity);
                        db.insertTicket(content);
                        output = new Message(Requests.addTicketRequest, true, null);
                        log.log(Level.INFO, "Server replied to Admin <" + this.id + "> addTicketRequest with status=True");
                        out.writeObject(output);
                        out.flush();
                        }
                    case buyTicketRequest -> {
                        int[] content = (int[]) input.message;
                        int idTicket = content[0];
                        int idClient = content[1];
                        log.log(Level.INFO, "Client <" + this.id + "> sent buyTicketRequest with idTicket="+idTicket);
                        db.buyTicket(content);
                        output = new Message(Requests.buyTicketRequest, true, null);
                        log.log(Level.INFO, "Server replied to Client <" + this.id + "> buyTicketRequest with status=True");
                        out.writeObject(output);
                        out.flush();
                    }
                    case goToShopRequest -> {
                        log.log(Level.INFO, "Client <" + this.id + "> sent buyTicketRequest");
                        ArrayList<Ticket> tickets = db.getFreeTicketData();
                        output = new Message(Requests.goToShopRequest, true, tickets);
                        log.log(Level.INFO, "Server replied to Client <" + this.id + "> buyTicketRequest with status=True");
                        out.writeObject(output);
                        out.flush();
                    }
                    case changeTicketDataRequest -> {
                        String[] content = (String[]) input.message;
                        String idTicket = content[0];
                        log.log(Level.INFO, "Admin <" + this.id + "> sent changeTicketRequest with idTicket="+idTicket);
                        db.changeTicketInfo(content);
                        output = new Message(Requests.changeTicketDataRequest, true, null);
                        log.log(Level.INFO, "Server replied to Admin <" + this.id + "> changeTicketRequest with status=True");
                        out.writeObject(output);
                        out.flush();
                    }
                    case deleteTicketDataRequest -> {
                        int idTicket = (int) input.message;
                        log.log(Level.INFO, "Admin <" + this.id + "> sent deleteTicketRequest with idTicket="+idTicket);
                        db.deleteTicketInfo(idTicket);
                        output = new Message(Requests.deleteTicketDataRequest, true, null);
                        log.log(Level.INFO, "Server replied to Admin <" + this.id + "> deleteTicketRequest with status=True");
                        out.writeObject(output);
                        out.flush();
                    }
                    case returnTicketRequest->{
                        int idTicket = (int) input.message;
                        log.log(Level.INFO, "Client <" + this.id + "> sent returnTicketRequest with idTicket="+idTicket);
                        db.returnTicketRequest(idTicket);
                        output = new Message(Requests.returnTicketRequest, true, null);
                        log.log(Level.INFO, "Server replied to Client <" + this.id + "> returnTicketRequest with status=True");
                        out.writeObject(output);
                        out.flush();
                    }
                }
            }
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
