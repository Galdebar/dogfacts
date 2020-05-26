package lt.galdebar.dogfacts.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lt.galdebar.dogfacts.domain.Fact;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void beforeAll(){
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());
    }

    @Test
    public void whenGetFacts_thenReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/facts")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello from Facts Controller")));
    }

    @Test
    public void givenNoParameters_whenGetRandomFact_thenReturnOneFact() throws Exception {
        String response = mockMvc.perform(get("/facts/random"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        Fact responseFact = objectMapper.readValue(response, Fact.class);

        assertNotNull(response);
        assertTrue(!response.trim().isEmpty());
        assertNotNull(responseFact);
        assertTrue(!responseFact.getText().trim().isEmpty());
    }

    @Test
    public void givenValidAmountParameter_whenGetRandomFact_thenReturnCorrectNumOfFacts() throws Exception {
        int expectedCount = 33;
        String response = mockMvc.perform(get("/facts/random?amount=" + expectedCount))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Fact> facts = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, Fact.class));

        assertNotNull(response);
        assertTrue(!response.trim().isEmpty());
        assertNotNull(facts);
        assertEquals(expectedCount, facts.size());
    }

    //get random fact
    //get random fact with limit from 1 to 500
    //get fact by ID
    //get queued facts

}