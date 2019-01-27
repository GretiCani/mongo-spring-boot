package mongo.examples;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.Block;

public class MongoExample {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected Block<Document> printer = new Block<Document>() {
		@Override
		public void apply(final Document document) {
			logger.info(document.toJson());
		}
	};
}
