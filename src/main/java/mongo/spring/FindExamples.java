// Copyright 2019 Kuei-chun Chen. All rights reserved.
package mongo.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import mongo.DocumentPrinter;

@Component
public class FindExamples extends DocumentPrinter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MongoTemplate mongoTemplate;

	public void testFindMany() {
		logger.debug("testFindMany");

		/*
		 * db.cars.find({"color": "Red", "year": {"$gte": 2017, "$lt": 2020}}, {"brand":
		 * 1, "style": 1, "year": 1, "_id": 0}) .sort({"year": -1, "brand": -1, "style":
		 * -1}).skip(5).limit(3)
		 */
		Sort sort = new Sort(Sort.Direction.DESC, "year").and(new Sort(Sort.Direction.DESC, "brand"))
				.and(new Sort(Sort.Direction.DESC, "style"));
		Query query = new Query(Criteria.where("color").is("Red"))
				.addCriteria(Criteria.where("year").gte(2017).lt(2020)).with(sort);
		query.fields().include("brand").include("style").include("year").exclude("_id");
		mongoTemplate.find(query.skip(5).limit(3), Car.class).forEach(consumer);
	}

	public void testFindArray() {
		logger.debug("testFinArray");

		/*
		 * db.favorites.find({'favoritesList.book': "Journey to the West"},
		 * {"favoriteCity":1, "favoriteCities":{"$slice": 1}, "_id": 0}).limit(3)
		 */
		Query query = new Query(Criteria.where("favoritesList.book").is("Journey to the West"));
		query.fields().include("favoriteCity").slice("favoriteCities", 1).exclude("_id");
		mongoTemplate.find(query.limit(3), Favorite.class).forEach(consumer);
	}

	public void testFindArrayElemMatch() {
		logger.debug("testFindArrayElemMatch");

		/*
		 * db.favorites.find({"favoritesList": {"$elemMatch": {"book":
		 * "Journey to the West", "city": "Atlanta" }}}, {"favoriteCity":1,
		 * "favoriteCities":{"$slice": 1}, "_id": 0}).limit(3)
		 */
		Query query = new Query(
				Criteria.where("favoritesList").elemMatch(Criteria.where("book").is("Journey to the West"))
						.elemMatch(Criteria.where("city").is("Atlanta")));
		query.fields().include("favoriteCity").slice("favoriteCities", 1).exclude("_id");
		mongoTemplate.find(query.limit(3), Favorite.class).forEach(consumer);
	}

	public void testFindKeyValueArray() {
		logger.debug("testFindKeyValueArray");

		/*
		 * db.favorites.find({"favoritesKVList.categories": {"$elemMatch": {"key":
		 * "book", "value": "Journey to the West"}}}, {"favoriteBooks": 1, "_id":
		 * 0}).limit(3)
		 */
		Query query = new Query(
				Criteria.where("favoritesKVList.categories").elemMatch(Criteria.where("key").is("book"))
						.elemMatch(Criteria.where("value").is("Journey to the West")));
		query.fields().include("favoriteBooks").exclude("_id");
		mongoTemplate.find(query.limit(3), Favorite.class).forEach(o->logger.info("{book: '" + o.favoriteBooks.get(0) + "'}"));
	}
}
