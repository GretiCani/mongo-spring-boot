package mongo.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpringImpl {
	@Autowired
	FindImpl findImpl;
	@Autowired
	AggregateImpl aggregateImpl;
	@Autowired
	InsertImpl insertImpl;
	
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
		aggregateImpl.testAggregateProjectFilter();
	
		// Insert examples
		insertImpl.testInsertOneWMajority();
	}
}
