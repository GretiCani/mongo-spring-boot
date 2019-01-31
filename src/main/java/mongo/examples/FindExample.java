// Copyright 2019 Kuei-chun Chen. All rights reserved.
package mongo.examples;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Projections.slice;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;

import mongo.DocumentPrinter;

@Component
public class FindExample extends DocumentPrinter {
	@Autowired
	private MongoClient mongoClient;
	
	public void testFindMany() {
		logger.debug("testFindMany");
		MongoCollection<Document> collection = mongoClient.getDatabase("keyhole").getCollection("cars");

		/*
		 * db.cars.find({"color": "Red", "year": {"$gte": 2017, "$lt": 2020}}, {"brand":
		 * 1, "style": 1, "year": 1, "_id": 0}) .sort({"year": -1, "brand": -1, "style":
		 * -1}).skip(5).limit(3)
		 */
		collection.find(and(gte("year", 2017), lt("year", 2020), eq("color", "Red")))
				.projection(fields(include("brand", "style", "year"), excludeId()))
				.sort(Sorts.descending("year", "brand", "style")).skip(5).limit(3).forEach(printer);
	}

	public void testFindArray() {
		logger.debug("testFinArray");
		MongoCollection<Document> collection = mongoClient.getDatabase("keyhole").getCollection("favorites");

		/*
		 * db.favorites.find({'favoritesList.book': "Journey to the West"},
		 * {"favoriteCity":1, "favoriteCities":{"$slice": 1}, "_id": 0}).limit(3)
		 */
		collection.find(eq("favoritesList.book", "Journey to the West"))
				.projection(fields(include("favoriteCity"), slice("favoriteCities", 1), excludeId())).limit(3)
				.forEach(printer);
	}

	public void testFindArrayElemMatch() {
		logger.debug("testFindArrayElemMatch");
		MongoCollection<Document> collection = mongoClient.getDatabase("keyhole").getCollection("favorites");

		/*
		 * db.favorites.find({"favoritesList": {"$elemMatch": {"book":
		 * "Journey to the West", "city": "Atlanta" }}}, {"favoriteCity":1,
		 * "favoriteCities":{"$slice": 1}, "_id": 0}).limit(3)
		 */
		collection.find(elemMatch("favoritesList", and(eq("book", "Journey to the West"), eq("city", "Atlanta"))))
				.projection(fields(include("favoriteCity"), slice("favoriteCities", 1), excludeId())).limit(3)
				.forEach(printer);
	}

	public void testFindKeyValueArray() {
		logger.debug("testFindKeyValueArray");
		MongoCollection<Document> collection = mongoClient.getDatabase("keyhole").getCollection("favorites");

		/*
		 * db.favorites.find({"favoritesKVList.categories": {"$elemMatch": {"key":
		 * "book", "value": "Journey to the West"}}}, {"favoriteBooks": 1, "_id":
		 * 0}).limit(3)
		 */
		collection
				.find(elemMatch("favoritesKVList.categories",
						and(eq("value", "Journey to the West"), eq("key", "book"))))
				.projection(fields(include("favoriteBooks"), excludeId())).limit(3).forEach(printer);
	}
}
