// Copyright 2019 Kuei-chun Chen. All rights reserved.
package mongo.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.MongoException;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.WriteConcern;
import com.mongodb.client.result.DeleteResult;

import mongo.DocumentPrinter;

@Component
public class InsertImpl extends DocumentPrinter {
	@Autowired
	private MongoTemplate mongoTemplate;

	public void testInsertOneWMajority() {
		logger.debug("testInsertOneWMajority");
		mongoTemplate.setWriteConcern(WriteConcern.MAJORITY);
		/*
		 * db.cars.insertOne({"color": "Red", "year": 2019, "brand": "BMW"})
		 */
		try {
			Car car = new Car();
			car.color = "Red";
			car.brand = "BMW";
			car.year = 2019;
			mongoTemplate.insert(car);
			DeleteResult result = mongoTemplate.remove(car);
			logger.debug("n deleted: " + result.getDeletedCount());
		} catch (MongoWriteConcernException e) {
			logger.error(e.getMessage());
		} catch (MongoException e) {
			logger.error(e.getMessage());
		}
	}
}
