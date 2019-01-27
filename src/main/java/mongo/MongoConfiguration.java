package mongo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfiguration {
	@Value("${spring.data.mongodb.uri}")
	private String mongoURI;

	@Bean
	public MongoClient mongoClient() throws Exception {
		return MongoClients.create(mongoURI);
	}
}