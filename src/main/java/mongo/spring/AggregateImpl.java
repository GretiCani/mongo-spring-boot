// Copyright 2019 Kuei-chun Chen. All rights reserved.
package mongo.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.ObjectOperators;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.stereotype.Component;

import mongo.DocumentPrinter;

@Component
public class AggregateImpl extends DocumentPrinter {
	@Autowired
	private MongoTemplate mongoTemplate;

	public void testAggregateLookup() {
		logger.debug("testAggregateLookup");
		/*
		 * [ { '$lookup': { 'from': 'dealers', 'localField': 'dealer', 'foreignField':
		 * '_id', 'as': 'dealer' } }, { '$unwind': { 'path': '$dealer' } }, { '$group':
		 * { '_id': '$dealer.name', 'count': { '$sum': 1 } } } ]
		 */
	}

	public void testAggregateLookupPipeline() {
		logger.debug("testAggregateLookupPipeline");
		/*
		 * [ { '$lookup': { 'from': 'dealers', 'let': { 'dealer': '$dealer' },
		 * 'pipeline': [ { '$match': { '$expr': { '$eq': [ '$_id', '$$dealer' ] } } }, {
		 * '$project': { 'name': 1, '_id': 0 } } ], 'as': 'dealer' } }, { '$unwind': {
		 * 'path': '$_id' } }, { '$group': { '_id': '$dealer.name', 'count': { '$sum': 1
		 * } } } ]
		 */
	}

	public void testAggregateObjectToArray() {
		logger.debug("testAggregateObjectToArray");
		/*
		 * [ { '$project': { '_id': 0, 'favoriteCity', 'favoriteCities',
		 * 'allFavoritesArray': { '$objectToArray': '$favoritesAll' } } }, { '$limit': 3
		 * } ]
		 */
		ProjectionOperation projectStage = Aggregation.project("favoriteCity", "favoriteCities").andExclude("_id")
				.and(ObjectOperators.ObjectToArray.toArray("$favoritesAll")).as("allFavoritesArray");
		LimitOperation limitStage = Aggregation.limit(3L);
		Aggregation aggregation = Aggregation.newAggregation(projectStage, limitStage);
		mongoTemplate.aggregate(aggregation, "favorites", Favorite.class).forEach(consumer);
	}
}
