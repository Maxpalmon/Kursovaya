package com.example.kursovaya.client;

        import java.net.URL;
        import java.util.ArrayList;
        import java.util.ResourceBundle;

        import com.example.kursovaya.models.Ticket;
        import javafx.application.Platform;
        import javafx.beans.property.SimpleBooleanProperty;
        import javafx.beans.property.SimpleIntegerProperty;
        import javafx.beans.property.SimpleStringProperty;
        import javafx.beans.value.ObservableValue;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.control.TableColumn;
        import javafx.scene.control.TableView;
        import javafx.scene.control.cell.PropertyValueFactory;
        import javafx.util.Callback;
        import org.w3c.dom.events.MouseEvent;

public class BuyTicketController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button BackButton;

    @FXML
    private Button BuyTicketButton;

    @FXML
    private TableColumn<Ticket, String> arrivalCityColumn;

    @FXML
    private Button RenewButton;
    @FXML
    private TableColumn<Ticket, Integer> costColumn;

    @FXML
    private TableColumn<Ticket, String> departmentCityColumn;

    @FXML
    private TableColumn<Ticket, Integer> idColumn;

    @FXML
    private TableColumn<Ticket, Boolean> extraConditionsColumn;

    @FXML
    private TableView mainTable;

    @FXML
    private TableColumn<?, ?> raceNumberColumn;

    @FXML
    void onRenewButtonClick(ActionEvent event) {
        ClientApplication.connection.goToShopRequest();
    }
    @FXML
    void onBackToClientView(ActionEvent event) {
        ClientApplication.connection.getClientDataRequest(ClientApplication.session.getSpecificId());
    }

    @FXML
    void onBuyTicket(ActionEvent event) {
        int index = mainTable.getSelectionModel().getSelectedIndex();
        int idTicket = idColumn.getCellData(index);
        ClientApplication.connection.buyTicketRequest(idTicket);
        ClientApplication.connection.goToShopRequest();
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
            ObservableList<TableColumn> list = mainTable.getColumns();
            list.get(0).setCellValueFactory(new PropertyValueFactory<>("idTicket"));
            list.get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
            list.get(2).setCellValueFactory(new PropertyValueFactory<>("idFlight"));
            list.get(3).setCellValueFactory(new PropertyValueFactory<>("extraConditions"));
            list.get(4).setCellValueFactory(new PropertyValueFactory<>("departmentCity"));
            list.get(5).setCellValueFactory(new PropertyValueFactory<>("arrivalCity"));
            mainTable.setItems(FXCollections.observableList(myTableData));
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
