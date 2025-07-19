package com.example.dashboard.controller;

import com.example.dashboard.model.SparqlResult;
import com.example.dashboard.service.SparqlService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML private TableView<SparqlResult> resultTable;
    @FXML private TableColumn<SparqlResult, String> subjectColumn;
    @FXML private TableColumn<SparqlResult, String> predicateColumn;
    @FXML private TableColumn<SparqlResult, String> objectColumn;

    @FXML private TextField keywordField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private DatePicker fromDate;
    @FXML private DatePicker toDate;

    private final ObservableList<SparqlResult> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind columns to model fields
        subjectColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlace()));
        predicateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        objectColumn.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getDate() != null ? data.getValue().getDate().toString() : "N/A"
        ));

        resultTable.setItems(masterData);
        typeComboBox.getItems().addAll("Hotel", "Temple", "Museum");

        updateResults();

        // Live filtering
        keywordField.textProperty().addListener((obs, o, n) -> applyFilters());
        typeComboBox.valueProperty().addListener((obs, o, n) -> applyFilters());
        fromDate.valueProperty().addListener((obs, o, n) -> applyFilters());
        toDate.valueProperty().addListener((obs, o, n) -> applyFilters());
    }

    @FXML
    private void onSearch(ActionEvent event) {
        performSearch();
    }

    private void performSearch() {
        updateResults(); // Refreshes results from SPARQL endpoint
        applyFilters();  // Applies keyword/type/date filters
    }

    private void updateResults() {
    String sparql = """
        PREFIX schema: <http://schema.org/>
        PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>

        SELECT ?place ?type ?date WHERE {
          ?place a ?type ;
                 schema:startDate ?date .
          FILTER(datatype(?date) = xsd:date)
        } LIMIT 100
    """;

    List<SparqlResult> results = new SparqlService().query(sparql);

    // ✅ Debug
    System.out.println("SPARQL returned " + results.size() + " rows");
    results.forEach(System.out::println);  // See what’s returned

    masterData.setAll(results);
}



    private void applyFilters() {
        String keyword = keywordField.getText().toLowerCase();
        String typeFilter = typeComboBox.getValue();
        LocalDate from = fromDate.getValue();
        LocalDate to = toDate.getValue();

        var filtered = masterData.stream().filter(result -> {
            boolean match = true;
            if (keyword != null && !keyword.isEmpty()) {
                match &= result.toString().toLowerCase().contains(keyword);
            }
            if (typeFilter != null && !typeFilter.isEmpty()) {
                match &= result.getType().contains(typeFilter);
            }
            if (from != null || to != null) {
                LocalDate date = result.getDate();
                if (date != null) {
                    if (from != null) match &= !date.isBefore(from);
                    if (to != null) match &= !date.isAfter(to);
                }
            }
            return match;
        }).collect(Collectors.toList());

        resultTable.setItems(FXCollections.observableArrayList(filtered));
    }
}
