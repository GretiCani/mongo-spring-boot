// Copyright 2019 Kuei-chun Chen. All rights reserved.
package mongo.examples;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.MongoException;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

import mongo.DocumentPrinter;

@Component
public class InsertExample extends DocumentPrinter {
	@Autowired
	private MongoClient mongoClient;

	public void testInsertOneWMajority() {
		logger.debug("testInsertOneWMajority");
		MongoCollection<Document> collection = mongoClient.getDatabase("keyhole").getCollection("cars")
				.withWriteConcern(WriteConcern.MAJORITY);

		/*
		 * db.cars.insertOne({"color": "Red", "year": 2019, "brand": "BMW"})
		 */
		try {
			Document doc = new Document("color", "Read").append("year", 2019).append("brand", "BMW");
			collection.insertOne(doc);
			DeleteResult result = collection.deleteOne(Filters.eq(doc.getObjectId("_id")));
			logger.debug("n deleted: " + result.getDeletedCount());
		} catch (MongoWriteConcernException e) {
			logger.error(e.getMessage());
		} catch (MongoException e) {
			logger.error(e.getMessage());
		}
	}
}
