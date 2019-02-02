// Copyright 2019 Kuei-chun Chen. All rights reserved.
package mongo.spring;

import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter.filter;
import static org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Eq.valueOf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ObjectOperators;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
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
		LookupOperation lookupStage = Aggregation.lookup("dealers", "dealer", "_id", "dealer");
		UnwindOperation unwindStage = Aggregation.unwind("$dealer");
		GroupOperation groupStage = Aggregation.group("$dealer.name").count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(lookupStage, unwindStage, groupStage);
		mongoTemplate.aggregate(aggregation, "cars", Car.class).forEach(consumer);
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
		String lookup = "{ '$lookup': { 'from': 'dealers', 'let': { 'dealer': '$dealer' },"
				+ "	'pipeline': [ { '$match': { '$expr': { '$eq': [ '$_id', '$$dealer' ] } } }, {"
				+ "	'$project': { 'name': 1, '_id': 0 } } ], 'as': 'dealer' } }";
		AggregationOperation lookupStage = AggregationOperationParser.parse(lookup);
		UnwindOperation unwindStage = Aggregation.unwind("$_id");
		GroupOperation groupStage = Aggregation.group("$dealer.name").count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(lookupStage, unwindStage, groupStage);
		mongoTemplate.aggregate(aggregation, "cars", Car.class).forEach(consumer);
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

	public void testAggregateProjectFilter() {
		logger.debug("testAggregateProjectFilter");
		/*
		 * [ { '$match': { 'favoritesList.book': 'Journey to the West' } }, {
		 * '$project': { 'favoritesList': { '$filter': { 'input': '$favoritesList',
		 * 'as': 'favoritesList', 'cond': { '$eq': [ '$$favoritesList.book', 'Journey to
		 * the West' ] } } }, '_id': 0 } }, { '$limit': 3 } ]
		 */
		MatchOperation matchStage = Aggregation.match(Criteria.where("favoritesList.book").is("Journey to the West"));
		ProjectionOperation projectStage = Aggregation.project()
				.and(filter("favoritesList").as("favoritesList")
						.by(valueOf("favoritesList.book").equalToValue("Journey to the West")))
				.as("favoritesList").andExclude("_id");
		LimitOperation limitStage = Aggregation.limit(3L);
		Aggregation aggregation = Aggregation.newAggregation(matchStage, projectStage, limitStage);
		mongoTemplate.aggregate(aggregation, "favorites", Favorite.class).forEach(consumer);
	}
}
