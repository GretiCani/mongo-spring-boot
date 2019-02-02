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
	public List<KV> allFavoritesArray;

	@Override
	public String toString() {
		return String.format("{favoriteCity: '%s', favoritesCities: '%s', favoritesAll: '%s, favoritesList.0.book: '%s''}",
				favoriteCity, (favoriteCities == null) ? null : favoriteCities.get(0),
				(allFavoritesArray == null) ? null : allFavoritesArray.get(0).v,
				(favoritesList == null) ? null : favoritesList.get(0).book);
	}
}

class Hobbies {
	public String book;
	public String city;
	public String movie;
	public String music;
	public String sport;
}

class KV {
	public String k;
	public String v;
}