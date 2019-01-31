package mongo.examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoExamples {
	@Autowired
	FindExample findImpl;
	@Autowired
	AggregateExample aggregateImpl;
	@Autowired
	InsertExample insertImpl;
	
	public void run() {
		// Find examples
		findImpl.testFindMany();
		findImpl.testFindArray();
		findImpl.testFindArrayElemMatch();
		findImpl.testFindKeyValueArray();
	
		// Aggregation examples
		aggregateImpl.testAggregateLookup();
		aggregateImpl.testAggregateLookupPipeline();
		aggregateImpl.testAggregateObjectToArray();
	
		// Insert examples
		insertImpl.testInsertOneWMajority();
	}
}
