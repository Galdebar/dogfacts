package lt.galdebar.dogfacts;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.galdebar.dogfacts.controllers.FactController;
import lt.galdebar.dogfacts.services.FactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.springframework.util.Assert.*;

@SpringBootTest
class DogfactsApplicationTests {

	@Autowired
	private FactController factController;

	@Autowired
	private FactService factService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
		notNull(factController,"facts controller up");
		notNull(factService,"facts service up");
		notNull(objectMapper,"object mapper up");
	}

}
