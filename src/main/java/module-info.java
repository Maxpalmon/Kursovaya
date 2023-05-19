module com.example.kursovaya {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;
    requires org.junit.jupiter.api;
    requires junit4;
    exports com.example.kursovaya.server;


    opens com.example.kursovaya to javafx.fxml;
    exports com.example.kursovaya;
    exports com.example.kursovaya.client;
    opens com.example.kursovaya.client to javafx.fxml;
    exports com.example.test.java;
    opens com.example.test.java to javafx.fxml;
}