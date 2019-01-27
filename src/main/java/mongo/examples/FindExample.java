package mongo.examples;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;

public class FindExample extends MongoExample {
	public void testFindMany(MongoClient mongoClient) {
		logger.debug("testFindMany");
		MongoCollection<Document> collection = mongoClient.getDatabase("keyhole").getCollection("cars");

		/*
		 * db.cars.find({"color": "Red", "year": {"$gte": 2017, "$lt": 2020}},
		 * 			{"brand": 1, "style": 1, "year": 1, "_id": 0})
		 * 		.sort({"year": -1, "brand": -1, "style": -1}).skip(5).limit(5)
		 */
		collection.find(and(gte("year", 2017), lt("year", 2020), eq("color", "Red")))
				.projection(fields(include("brand", "style", "year"), excludeId()))
				.sort(Sorts.descending("year", "brand", "style")).skip(5).limit(5).forEach(printer);
	}
}
