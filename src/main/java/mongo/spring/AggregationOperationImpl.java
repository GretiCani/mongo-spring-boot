package mongo.spring;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

public class AggregationOperationImpl implements AggregationOperation {
	private String json;

	public AggregationOperationImpl(String json) {
		this.json = json;
	}

	@Override
	public Document toDocument(AggregationOperationContext aggregationOperationContext) {
		return aggregationOperationContext.getMappedObject(Document.parse(json));
	}
}