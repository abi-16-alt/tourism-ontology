package com.example.dashboard.service;

import com.example.dashboard.model.SparqlResult;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SparqlService {

    public List<SparqlResult> query(String sparqlQuery) {
        List<SparqlResult> results = new ArrayList<>();

        try {
            // ✅ Load the local TTL file from resources
            InputStream in = getClass().getResourceAsStream("/data/tourism.ttl");
            if (in == null) {
                System.err.println("❌ Could not find tourism.ttl");
                return results;
            }

            Model model = ModelFactory.createDefaultModel();
            RDFDataMgr.read(model, in, org.apache.jena.riot.Lang.TTL);

            // ✅ Run the SPARQL query on the local model
            Query query = QueryFactory.create(sparqlQuery);
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet rs = qexec.execSelect();
                while (rs.hasNext()) {
                    results.add(new SparqlResult(rs.next()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}
