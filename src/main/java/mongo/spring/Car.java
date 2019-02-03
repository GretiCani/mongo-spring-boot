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

	@Override
	public String toString() {
		return String.format("{brand: '%s', style: '%s', year: %d}", brand, style, year);
	}
}
