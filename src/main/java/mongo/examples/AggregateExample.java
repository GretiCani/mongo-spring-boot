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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Variable;

public class AggregateExample {
	private Logger logger = LoggerFactory.getLogger(AggregateExample.class);

	public void testAggregateLookup(MongoClient mongoClient) {
		MongoCollection<Document> collection = mongoClient.getDatabase("keyhole").getCollection("cars");
		/*
		 * [ { "$lookup": { "from": "dealers", "localField": "dealer", "foreignField":
		 * "_id", "as": "inventory" } } ]
		 */
		logger.debug("total: " + collection.countDocuments());
		AggregateIterable<Document> output = collection
				.aggregate(Arrays.asList(lookup("dealers", "dealer", "_id", "inventory"),
						Aggregates.group("$brand", Accumulators.sum("count", 1))));

		for (Document doc : output) {
			logger.info(doc.toJson());
		}
	}

	public void testAggregateLookupPipeline(MongoClient mongoClient) {
		MongoCollection<Document> collection = mongoClient.getDatabase("keyhole").getCollection("cars");
		/*
		 * [{ "$lookup": { "from": "dealers", "let": { "dealer": "$dealer" },
		 * "pipeline": [{ "$match": { "$expr": { "$eq": ["$_id", "$$dealer"] } } }],
		 * "as": "inventory" } }]
		 */
		List<Variable<?>> variables = Arrays.asList(new Variable<>("dealer", "$dealer"));
		List<Bson> pipeline = Arrays.asList(
				match(expr(
						new Document("$and", Arrays.asList(new Document("$eq", Arrays.asList("$_id", "$$dealer")))))),
				project(fields(excludeId())));

		logger.debug("total: " + collection.countDocuments());
		// Block<Document> printer = System.out::println;
		AggregateIterable<Document> output = collection
				.aggregate(Arrays.asList(lookup("cars", variables, pipeline, "inventory"),
						Aggregates.group("$brand", Accumulators.sum("count", 1))));

		for (Document doc : output) {
			logger.info(doc.toJson());
		}
	}
}
