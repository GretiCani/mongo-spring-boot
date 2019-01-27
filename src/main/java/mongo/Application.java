package mongo;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Variable;

@SpringBootApplication
public class Application implements CommandLineRunner {
	Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private MongoClient mongoClient;

	@Override
	public void run(String... args) throws Exception {
		MongoCollection<Document> collection = mongoClient.getDatabase("keyhole").getCollection("cars");
		/*
		 * [ { '$lookup': { 'from': 'dealers', 'let': { 'dealer': '$dealer' },
		 * 'pipeline': [ { '$match': { '$expr': { '$eq': [ '$_id', '$$dealer' ] } } } ],
		 * 'as': 'inventory' } } ]
		 */
		List<Variable<?>> variables = Arrays.asList(new Variable<>("dealer", "$dealer"));
		List<Bson> pipeline = Arrays.asList(
				match(expr(
						new Document("$and", Arrays.asList(new Document("$eq", Arrays.asList("$_id", "$$dealer")))))),
				project(fields(excludeId())));

		System.out.println(collection.countDocuments());
		Block<Document> printer = System.out::println;
		System.out.println(lookup("dealers", variables, pipeline, "inventory"));
		collection.aggregate(Arrays.asList(lookup("cars", variables, pipeline, "inventory"),
				Aggregates.group("$brand", Accumulators.sum("count", 1)))).forEach(printer);
	}
}
