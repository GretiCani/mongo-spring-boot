// Copyright 2019 Kuei-chun Chen. All rights reserved.
package mongo.examples;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

public class InsertExample extends MongoExample {
	public void testInsertOneWMajority(MongoClient mongoClient) {
		logger.debug("testInsertOneWMajority");
		MongoCollection<Document> collection = mongoClient.getDatabase("keyhole").getCollection("cars")
				.withWriteConcern(WriteConcern.MAJORITY);

		/*
		 * db.cars.insertOne({"color": "Red", "year": 2019, "brand": "BMW"})
		 */
		try {
			collection.insertOne(new Document("color", "Read").append("year", 2019).append("brand", "BMW"));
		} catch (MongoWriteConcernException e) {
			logger.error(e.getMessage());
		} catch (MongoException e) {
			logger.error(e.getMessage());
		}
	}
}
