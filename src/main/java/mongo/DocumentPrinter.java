// Copyright 2019 Kuei-chun Chen. All rights reserved.
package mongo;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.Block;

public class DocumentPrinter {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected Block<Document> printer = new Block<Document>() {
		@Override
		public void apply(final Document document) {
			logger.info(document.toJson());
		}
	};
}
