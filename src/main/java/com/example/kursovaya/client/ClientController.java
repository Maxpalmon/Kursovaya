package com.example.kursovaya.client;

        import java.net.URL;
        import java.sql.ResultSet;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.ResourceBundle;
        import java.util.SimpleTimeZone;

        import com.example.kursovaya.models.Ticket;
        import javafx.application.Platform;
        import javafx.beans.property.*;
        import javafx.beans.value.ObservableValue;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.control.TableColumn;
        import javafx.scene.control.TableView;
        import javafx.scene.control.cell.PropertyValueFactory;
        import javafx.scene.input.MouseEvent;

public class ClientController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button BackButton;

    @FXML
    public TableView MainTable;
    @FXML
    private TableColumn<Ticket, Integer> costColumn;

    @FXML
    private TableColumn<Ticket, Integer> idFlightColumn;

    @FXML
    private TableColumn<Ticket, Integer> idTicketColumn;

    @FXML
    private TableColumn<Ticket, String> idArrivalCityColumn;

    @FXML
    private TableColumn<Ticket, String> idDepartmentCityColumn;

    @FXML
    private TableColumn<Ticket, Boolean> idExtraConditionsColumn;
    @FXML
    private Button RenewInfoButton;


    @FXML
    private Button BuyTicketButton;

    @FXML
    private Button ReturnTicketButton;


    @FXML
    void onRenewInfoButtonClick(ActionEvent event) {
        ClientApplication.connection.getClientDataRequest(ClientApplication.session.getSpecificId());
    }

    @FXML
    void onBackToWelcomeWindow(ActionEvent event) {
        ClientApplication.app.switchScene("WelcomeWindowView.fxml");
    }

    @FXML
    void getItem(MouseEvent event) {
        int index = MainTable.getSelectionModel().getSelectedIndex();
    }

    @FXML
    void onBuyTicket(ActionEvent event) {
        System.out.println("Клиент перешел в магазин");
        ClientApplication.connection.goToShopRequest();
    }

    @FXML
    void onReturnTicket(ActionEvent event) {
        int index = MainTable.getSelectionModel().getSelectedIndex();
        int idTicket = idTicketColumn.getCellData(index);
        ClientApplication.connection.returnTicketRequest(idTicket);
        ClientApplication.connection.getClientDataRequest(ClientApplication.session.getSpecificId());
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
        private SimpleIntegerProperty price;
        private SimpleIntegerProperty idFlight;
        private SimpleBooleanProperty extraConditions;
        private SimpleStringProperty departmentCity;
        private SimpleStringProperty arrivalCity;




        tableData(int idTicket, int price, int idFlight, boolean extraConditions,
                  String departmentCity, String arrivalCity)
        {
            this.idTicket = new SimpleIntegerProperty(idTicket);
            this.price = new SimpleIntegerProperty(price);
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

        public int getprice() {
            return price.get();
        }

        public SimpleIntegerProperty priceProperty() {
            return price;
        }

        public void setprice(int price) {
            this.price.set(price);
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