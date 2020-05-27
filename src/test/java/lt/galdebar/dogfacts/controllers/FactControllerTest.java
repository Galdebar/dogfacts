package lt.galdebar.dogfacts.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Random;

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

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void whenGetFacts_returnListOfFacts() throws Exception {
        int maxFacts = 500;
        String response = mockMvc.perform(get("/facts"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Fact> actualFacts = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, Fact.class));

        assertNotNull(response);
        assertTrue(!response.trim().isEmpty());
        assertNotNull(actualFacts);
        assertTrue(actualFacts.size() > 0);
        assertTrue(actualFacts.size() <= maxFacts);
    }

    @Test
    public void givenNoParameters_whenGetRandomFact_thenReturnOneFact() throws Exception {
        String response = mockMvc.perform(get("/facts/random"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        Fact actualFact = objectMapper.readValue(response, Fact.class);

        assertNotNull(response);
        assertTrue(!response.trim().isEmpty());
        assertNotNull(actualFact);
        assertTrue(!actualFact.getText().trim().isEmpty());
    }


    @Test
    public void givenValidAmountParameter_whenGetRandomFact_thenReturnCorrectNumOfFacts() throws Exception {
        int expectedCount = new Random().nextInt(500 - 1) + 1;
        String response = mockMvc.perform(get("/facts/random?amount=" + expectedCount))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Fact> actualFact = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, Fact.class));

        assertNotNull(response);
        assertTrue(!response.trim().isEmpty());
        assertNotNull(actualFact);
        assertTrue(actualFact.size() <= expectedCount); // Smaller, because there aren't that many dog facts, so amount might not reach max
    }

    @Test
    public void givenTooLowAmount_whenGetRandomFact_thenReturnOneFact() throws Exception {
        int invalidAmount = 0;
        String response = mockMvc.perform(get("/facts/random?amount=" + invalidAmount))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Fact actualFact = objectMapper.readValue(response, Fact.class);

        assertNotNull(response);
        assertTrue(!response.trim().isEmpty());
        assertNotNull(actualFact);
        assertTrue(!actualFact.getText().trim().isEmpty());
    }

    @Test
    public void givenTooLargeAmount_whenGetRandomFact_thenReturnMaxFacts() throws Exception {
        int maxFacts = 500;
        int requestedAmount = Math.abs(new Random().nextInt() + maxFacts);

        String response = mockMvc.perform(get("/facts/random?amount=" + requestedAmount))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Fact> actualFact = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, Fact.class));

        assertNotNull(response);
        assertTrue(!response.trim().isEmpty());
        assertNotNull(actualFact);
        assertTrue(actualFact.size() <= maxFacts);

    }

    @Test
    public void givenValidID_whenGetFactByID_thenReturnSameFact() throws Exception {
        Fact expectedFact = objectMapper.readValue(
                mockMvc.perform(get("/facts/random"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Fact.class
        );

        String response = mockMvc.perform(get("/facts/" + expectedFact.get_id()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Fact actualFact = objectMapper.readValue(response, Fact.class);

        assertNotNull(actualFact);
        assertEquals(expectedFact, actualFact);
    }

    @Test
    public void givenInvalidID_whenGetFactByID_thenReturnNotFound() throws Exception {
        String invalidID = "09awoidhawd";

        mockMvc.perform(get("/facts" + invalidID))
                .andExpect(status().isNotFound());
    }

//    @Test
//    public void givenEmptyID_whenGetFactByID_thenReturnRandomFact() throws Exception {
//        String response = mockMvc.perform(get("/facts/" + ""))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        Fact actualFact = objectMapper.readValue(response, Fact.class);
//
//        assertNotNull(response);
//        assertTrue(!response.trim().isEmpty());
//        assertNotNull(actualFact);
//        assertTrue(!actualFact.getText().trim().isEmpty());
//    }

    @Test
    public void givenBlankID_whenGetFactByID_thenReturnRandomFact() throws Exception {
        String response = mockMvc.perform(get("/facts/" + "    "))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Fact actualFact = objectMapper.readValue(response, Fact.class);

        assertNotNull(response);
        assertTrue(!response.trim().isEmpty());
        assertNotNull(actualFact);
        assertTrue(!actualFact.getText().trim().isEmpty());
    }


    //get queued facts

}