package com.example.dashboard.model;

import org.apache.jena.query.QuerySolution;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SparqlResult {
    private final String place;
    private final String type;
    private final LocalDate date;

    // ✅ Constructor using explicit values
    public SparqlResult(String place, String type, LocalDate date) {
        this.place = place;
        this.type = type;
        this.date = date;
    }

    // ✅ Constructor using QuerySolution
    public SparqlResult(QuerySolution sol) {
        this.place = sol.get("place").toString();
        this.type = sol.get("type").toString();
        String dateStr = sol.get("date").toString().split("\\^")[0];
        this.date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public String getPlace() {
        return place;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return place + " | " + type + " | " + date;
    }
}
