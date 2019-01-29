// Copyright 2019 Kuei-chun Chen. All rights reserved.
package mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.client.MongoClient;

import mongo.examples.AggregateExample;
import mongo.examples.FindExample;
import mongo.examples.InsertExample;

@SpringBootApplication
public class Application implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	private MongoClient mongoClient;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.debug("Application.run()");
		
		// Find examples
		FindExample findImpl = new FindExample();
		findImpl.testFindMany(mongoClient);
		findImpl.testFindArray(mongoClient);
		findImpl.testFindArrayElemMatch(mongoClient);
		findImpl.testFindKeyValueArray(mongoClient);
		
		// Aggregation examples
		AggregateExample aggregateImpl = new AggregateExample();
		aggregateImpl.testAggregateLookup(mongoClient);
		aggregateImpl.testAggregateLookupPipeline(mongoClient);
		aggregateImpl.testAggregateObjectToArray(mongoClient);
		
		// Insert examples
		InsertExample insertImpl = new InsertExample();
		insertImpl.testInsertOneWMajority(mongoClient);
	}
}
