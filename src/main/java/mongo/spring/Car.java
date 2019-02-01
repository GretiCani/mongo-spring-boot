package mongo.spring;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cars")
public class Car {

	@Id
	public String id;

	public String brand;
	public String color;
	public String style;
	public int year;
	public int count;

	@Override
	public String toString() {
		return String.format("{count: %d, brand: '%s', style: '%s', year: %d}", count, brand, style, year);
	}
}
