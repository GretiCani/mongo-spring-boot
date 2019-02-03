package mongo.spring.results;

import java.util.List;

public class AggregateObjectToArrayResult {
	public List<KV> allFavoritesArray;

	@Override
	public String toString() {
		return String.format("{favoritesCities: '%s'}", allFavoritesArray);
	}
}

class KV {
	public String k;
	public String v;

	@Override
	public String toString() {
		return String.format("{k: '%s', v: '%s'}", k, v);
	}
}