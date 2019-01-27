package mongo.examples;

import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.expr;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Variable;

public class AggregateExample extends MongoExample {
	public void testAggregateLookup(MongoClient mongoClient) {
		logger.debug("testAggregateLookup");
		MongoCollection<Document> collection = mongoClient.getDatabase("keyhole").getCollection("cars");
		/*
		 * [ { '$lookup': { 'from': 'dealers', 'localField': 'dealer', 'foreignField':
		 * '_id', 'as': 'dealer' } }, { '$unwind': { 'path': '$dealer' } }, { '$group':
		 * { '_id': '$dealer.name', 'count': { '$sum': 1 } } } ]
		 */
		logger.debug("total: " + collection.countDocuments());
		collection.aggregate(Arrays.asList(lookup("dealers", "dealer", "_id", "dealer"), Aggregates.unwind("$dealer"),
				Aggregates.group("$dealer.name", Accumulators.sum("count", 1)))).forEach(printer);
	}

	public void testAggregateLookupPipeline(MongoClient mongoClient) {
		logger.debug("testAggregateLookupPipeline");
		MongoCollection<Document> collection = mongoClient.getDatabase("keyhole").getCollection("cars");
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

		logger.debug("total: " + collection.countDocuments());
		collection.aggregate(Arrays.asList(lookup("dealers", variables, pipeline, "dealer"),
				Aggregates.unwind("$dealer"), Aggregates.group("$dealer.name", Accumulators.sum("count", 1))))
				.forEach(printer);
	}
}
