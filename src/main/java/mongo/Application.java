// Copyright 2019 Kuei-chun Chen. All rights reserved.
package mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import mongo.examples.MongoExamples;
import mongo.spring.SpringExamples;

@SpringBootApplication
public class Application implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	MongoExamples mongoExamples;
	@Autowired
	SpringExamples SpringExamples;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.debug("Application.run()");
		
		mongoExamples.run();
		SpringExamples.run();
	}
}
