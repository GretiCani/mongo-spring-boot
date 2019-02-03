package mongo.spring.results;

import org.springframework.data.annotation.Id;

public class AggregateLookupResult {
	@Id
	public String id;
	public int count;

	@Override
	public String toString() {
		return String.format("{dealer: '%s', count: %d}", id, count);
	}
}
