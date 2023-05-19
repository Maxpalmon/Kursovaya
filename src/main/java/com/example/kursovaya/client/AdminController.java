package com.example.kursovaya.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.example.kursovaya.models.Ticket;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class AdminController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button AddInfoButton;

    @FXML
    private TableColumn<Ticket, String> ArrivalCityColumn;

    @FXML
    private Button BackButton;

    @FXML
    private Button ChangeButton;

    @FXML
    private Button DeleteButton;

    @FXML
    private TableColumn<Ticket, String> DepartmentCityColumn;

    @FXML
    private TableColumn<Ticket, Boolean> ExtraConditionsColumn;

    @FXML
    private TableView MainTable;

    @FXML
    private TableColumn<Ticket, Integer> PriceColumn;

    @FXML
    private TextField PriceField;

    @FXML
    private TableColumn<Ticket, Integer> RaceNumberColumn;

    @FXML
    private TextField arrivalCityField;

    @FXML
    private TextField departmentCityField;

    @FXML
    private CheckBox extraConditionsCheckBox;

    @FXML
    private TextField idFlightField;
    @FXML
    private Button RenewButton;


    @FXML
    private TableColumn<Ticket, Integer> idTicketColumn;

    @FXML
    void onRenewButton(ActionEvent event) {
        ClientApplication.connection.getAdminDataRequest(ClientApplication.session.getSpecificId());
    }

    public static boolean isNumeric(String intValue) {
        try {
            int value = Integer.parseInt(intValue);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
            return false;
        }
    }

    @FXML
    void onAddInfoClick(ActionEvent event) {
        if(PriceField.getText().isEmpty()||idFlightField.getText().isEmpty()||
                departmentCityField.getText().isEmpty()||arrivalCityField.getText().isEmpty())
            return;
        if (isNumeric(PriceField.getText()))
            System.out.println("XD");
        if(isNumeric(PriceField.getText())
                &&isNumeric(idFlightField.getText())
                &&isNumeric(PriceField.getText()))
            ClientApplication.connection.addTicketRequest(PriceField.getText(), idFlightField.getText(),extraConditionsCheckBox.isSelected(), departmentCityField.getText(), arrivalCityField.getText());
    }

    @FXML
    void onBackToWelcomeWindow(ActionEvent event) {
        ClientApplication.app.switchScene("WelcomeWindowView.fxml");
    }


    @FXML
    void GetItem(MouseEvent event) {
        int index = MainTable.getSelectionModel().getSelectedIndex();
        PriceField.setText(String.valueOf(PriceColumn.getCellData(index)));
        idFlightField.setText(String.valueOf(RaceNumberColumn.getCellData(index)));
        departmentCityField.setText(String.valueOf(DepartmentCityColumn.getCellData(index)));
        arrivalCityField.setText(String.valueOf(ArrivalCityColumn.getCellData(index)));
        if (ExtraConditionsColumn.toString() == "true")
        {
            extraConditionsCheckBox.setSelected(true);
        }
        else extraConditionsCheckBox.setSelected(false);
    }

    @FXML
    void onChangeInfoClick(ActionEvent event) {//TODO
        String[] content = new String[6];
        int index = MainTable.getSelectionModel().getSelectedIndex();
        content[0] = String.valueOf(idTicketColumn.getCellData(index));
        content[1] = PriceField.getText();
        content[2] = idFlightField.getText();
        content[3] = String.valueOf(extraConditionsCheckBox.isSelected());
        content[4] = departmentCityField.getText();
        content[5] = arrivalCityField.getText();
        ClientApplication.connection.changeTicketDataRequest(content);
    }

    @FXML
    void onDeleteInfoClick(ActionEvent event) {//TODO
        int index = MainTable.getSelectionModel().getSelectedIndex();
        int idTicket = idTicketColumn.getCellData(index);
        ClientApplication.connection.deleteTicketDataRequest(idTicket);
    }

    @FXML
    void initialize() {
        Platform.runLater(()->{
            ArrayList<Ticket> tickets = (ArrayList<Ticket>) ClientApplication.stage.getUserData();
            ArrayList<tableData> myTableData = new ArrayList<>();
            for (int i = 0; i < tickets.size(); ++i) {
                Ticket ticket = tickets.get(i);
                myTableData.add(new tableData(ticket.getIdTicket(), ticket.getprice(), ticket.getIdFlight(), ticket.getExtraConditions(), ticket.getDepartmentCity(), ticket.getArrivalCity()));
            }
            ObservableList<TableColumn> list = MainTable.getColumns();
            list.get(0).setCellValueFactory(new PropertyValueFactory<>("idTicket"));
            list.get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
            list.get(2).setCellValueFactory(new PropertyValueFactory<>("idFlight"));
            list.get(3).setCellValueFactory(new PropertyValueFactory<>("extraConditions"));
            list.get(4).setCellValueFactory(new PropertyValueFactory<>("departmentCity"));
            list.get(5).setCellValueFactory(new PropertyValueFactory<>("arrivalCity"));
            MainTable.setItems(FXCollections.observableList(myTableData));

        });




    }
    public class tableData{
        private SimpleIntegerProperty idTicket;
        private SimpleIntegerProperty Price;
        private SimpleIntegerProperty idFlight;
        private SimpleBooleanProperty extraConditions;
        private SimpleStringProperty departmentCity;
        private SimpleStringProperty arrivalCity;

        tableData(int idTicket, int Price, int idFlight, boolean extraConditions,
                  String departmentCity, String arrivalCity)
        {
            this.idTicket = new SimpleIntegerProperty(idTicket);
            this.Price = new SimpleIntegerProperty(Price);
            this.idFlight = new SimpleIntegerProperty(idFlight);
            this.extraConditions = new SimpleBooleanProperty(extraConditions);
            this.departmentCity = new SimpleStringProperty(departmentCity);
            this.arrivalCity = new SimpleStringProperty(arrivalCity);
        }

        public int getIdTicket() {
            return idTicket.get();
        }

        public SimpleIntegerProperty idTicketProperty() {
            return idTicket;
        }

        public void setIdTicket(int idTicket) {
            this.idTicket.set(idTicket);
        }

        public int getPrice() {
            return Price.get();
        }

        public SimpleIntegerProperty priceProperty() {
            return Price;
        }

        public void setPrice(int price) {
            this.Price.set(price);
        }

        public int getIdFlight() {
            return idFlight.get();
        }

        public SimpleIntegerProperty idFlightProperty() {
            return idFlight;
        }

        public void setIdFlight(int idFlight) {
            this.idFlight.set(idFlight);
        }

        public String getDepartmentCity() {
            return departmentCity.get();
        }

        public SimpleStringProperty departmentCityProperty() {
            return departmentCity;
        }

        public void setDepartmentCity(String departmentCity) {
            this.departmentCity.set(departmentCity);
        }

        public String getArrivalCity() {
            return arrivalCity.get();
        }

        public SimpleStringProperty arrivalCityProperty() {
            return arrivalCity;
        }

        public void setArrivalCity(String arrivalCity) {
            this.arrivalCity.set(arrivalCity);
        }

        public boolean isExtraConditions() {
            return extraConditions.get();
        }

        public SimpleBooleanProperty extraConditionsProperty() {
            return extraConditions;
        }

        public void setExtraConditions(boolean extraConditions) {
            this.extraConditions.set(extraConditions);
        }
    }
}
