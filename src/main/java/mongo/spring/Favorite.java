package mongo.spring;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "favorites")
public class Favorite {

	@Id
	public String id;
	public String favoriteCity;
	public List<String> favoriteBooks;
	public List<String> favoriteCities;

	@Override
	public String toString() {
		return String.format("{favoriteCity: '%s', favoritesCities: '%s'}", favoriteCity,
				(favoriteCities == null) ? null : favoriteCities.get(0));
	}
}