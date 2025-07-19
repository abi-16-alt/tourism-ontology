from rdflib import Graph
import json
import os

# Load RDF from the correct path
g = Graph()
g.parse("../data/tourism.ttl", format="turtle")

# SPARQL Query
q = """
PREFIX : <http://example.org/tourism#>
SELECT ?name ?rating ?latitude ?longitude
WHERE {
  ?attraction a :TouristAttraction ;
              :hasName ?name ;
              :hasRating ?rating ;
              :latitude ?latitude ;
              :longitude ?longitude .
}
ORDER BY DESC(?rating)
"""

results = g.query(q)

# GeoJSON output
features = []
for row in results:
    features.append({
        "type": "Feature",
        "properties": {
            "name": str(row.name),
            "rating": float(row.rating)
        },
        "geometry": {
            "type": "Point",
            "coordinates": [float(row.longitude), float(row.latitude)]
        }
    })

# Save GeoJSON to map folder
output_folder = "../map"
os.makedirs(output_folder, exist_ok=True)
with open(f"{output_folder}/highly_rated.geojson", "w") as f:
    json.dump({"type": "FeatureCollection", "features": features}, f, indent=2)

print("✅ GeoJSON exported to ../map/highly_rated.geojson")
