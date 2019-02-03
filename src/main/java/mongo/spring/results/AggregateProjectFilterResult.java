package mongo.spring.results;

import java.util.List;

public class AggregateProjectFilterResult {
	public List<Hobbies> favoritesList;

	@Override
	public String toString() {
		return String.format("{favoritesList: '%s'}", favoritesList);
	}
}

class Hobbies {
	public String book;
	public String city;
	public String movie;
	public String music;
	public String sport;

	@Override
	public String toString() {
		return String.format("{book: '%s', city: '%s', movie: '%s', music: '%s', sport: '%s'}", book, city, movie,
				music, sport);
	}
}
