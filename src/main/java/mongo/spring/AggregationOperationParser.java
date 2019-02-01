package mongo.spring;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

public class AggregationOperationParser {
	public static AggregationOperation parse(String json) {
		return new AggregationOperationImpl(json);
	}
}
