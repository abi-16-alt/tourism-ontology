module semantic.dashboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.jena.core;
    requires org.apache.jena.arq;
    requires org.apache.jena.shacl;

    opens com.example.dashboard to javafx.fxml;
    opens com.example.dashboard.controller to javafx.fxml;

    exports com.example.dashboard;
}

