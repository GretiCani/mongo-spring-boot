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
	public List<Hobbies> favoritesList;

	@Override
	public String toString() {
		return String.format("{favoriteCity: '%s', favoritesCities: '%s'}", favoriteCity, favoriteCities.get(0));
	}
}

class Hobbies {
	public String book;
	public String city;
	public String movie;
	public String music;
	public String sport;
}