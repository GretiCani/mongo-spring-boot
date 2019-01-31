package mongo.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpringExamples {
	@Autowired
	FindExamples findImpl;
	
	public void run() {
		// Find examples
		findImpl.testFindMany();
		findImpl.testFindArray();
		findImpl.testFindArrayElemMatch();
		findImpl.testFindKeyValueArray();
	}
}
