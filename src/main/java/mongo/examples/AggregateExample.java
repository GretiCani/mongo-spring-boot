// Copyright 2019 Kuei-chun Chen. All rights reserved.
package mongo.examples;

import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.expr;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Variable;

import mongo.DocumentPrinter;

@Component
public class AggregateExample extends DocumentPrinter {
	@Autowired
	private MongoClient mongoClient;

	public void testAggregateLookup() {
		logger.debug("testAggregateLookup");
		MongoCollection<Document> c = mongoClient.getDatabase("keyhole").getCollection("cars");
		/*
		 * [ { '$lookup': { 'from': 'dealers', 'localField': 'dealer', 'foreignField':
		 * '_id', 'as': 'dealer' } }, { '$unwind': { 'path': '$dealer' } }, { '$group':
		 * { '_id': '$dealer.name', 'count': { '$sum': 1 } } } ]
		 */
		logger.debug("total: " + c.countDocuments());
		c.aggregate(Arrays.asList(lookup("dealers", "dealer", "_id", "dealer"), Aggregates.unwind("$dealer"),
				Aggregates.group("$dealer.name", Accumulators.sum("count", 1)))).forEach(printer);
	}

	public void testAggregateLookupPipeline() {
		logger.debug("testAggregateLookupPipeline");
		MongoCollection<Document> c = mongoClient.getDatabase("keyhole").getCollection("cars");
		/*
		 * [ { '$lookup': { 'from': 'dealers', 'let': { 'dealer': '$dealer' },
		 * 'pipeline': [ { '$match': { '$expr': { '$eq': [ '$_id', '$$dealer' ] } } }, {
		 * '$project': { 'name': 1, '_id': 0 } } ], 'as': 'dealer' } }, { '$unwind': {
		 * 'path': '$_id' } }, { '$group': { '_id': '$dealer.name', 'count': { '$sum': 1
		 * } } } ]
		 */
		List<Variable<?>> variables = Arrays.asList(new Variable<>("dealer", "$dealer"));
		List<Bson> pipeline = Arrays.asList(
				match(expr(
						new Document("$and", Arrays.asList(new Document("$eq", Arrays.asList("$_id", "$$dealer")))))),
				project(fields(Projections.include("name"), excludeId())));
		c.aggregate(Arrays.asList(lookup("dealers", variables, pipeline, "dealer"), Aggregates.unwind("$dealer"),
				Aggregates.group("$dealer.name", Accumulators.sum("count", 1)))).forEach(printer);
	}

	public void testAggregateObjectToArray() {
		logger.debug("testAggregateObjectToArray");
		MongoCollection<Document> c = mongoClient.getDatabase("keyhole").getCollection("favorites");
		/*
		 * [ { '$project': { '_id': 0, 'allFavoritesArray': { '$objectToArray':
		 * '$favoritesAll' } } }, { '$limit': 3 } ]
		 */
		c.aggregate(Arrays.asList(
				project(fields(excludeId(), computed("allFavoritesArray", eq("$objectToArray", "$favoritesAll")))),
				limit(3))).forEach(printer);
	}

	public void testAggregateProjectFilter() {
		logger.debug("testAggregateProjectFilter");
		MongoCollection<Document> c = mongoClient.getDatabase("keyhole").getCollection("favorites");
		/*
		 * [ { '$match': { 'favoritesList.book': 'Journey to the West' } }, {
		 * '$project': { 'favoritesList': { '$filter': { 'input': '$favoritesList',
		 * 'as': 'favoritesList', 'cond': { '$eq': [ '$$favoritesList.book', 'Journey to
		 * the West' ] } } }, '_id': 0 } }, { '$limit': 3 } ]
		 */
		c.aggregate(Arrays.asList(match(eq("favoritesList.book", "Journey to the West")),
				project(fields(
						computed("favoritesList",
								eq("$filter", and(eq("input", "$favoritesList"), eq("as", "favoritesList"),
										eq("cond", Arrays.asList("$$favoritesList.book", "Journey to the West"))))),
						excludeId())),
				limit(3))).forEach(printer);
	}
}
