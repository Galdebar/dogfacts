package lt.galdebar.dogfacts.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lt.galdebar.dogfacts.domain.Fact;
import lt.galdebar.dogfacts.domain.external.FactWithDates;
import lt.galdebar.dogfacts.domain.external.FactWithUser;
import lt.galdebar.dogfacts.services.adapters.FactWithDatesAdapter;
import lt.galdebar.dogfacts.services.adapters.FactWithUserAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FactService {

    private final String DEFAULT_URL = "https://cat-fact.herokuapp.com/facts/";
    private final String RANDOM_SUFFIX = "random";
    private final String ANIMAL_TYPE_PARAMETER = "?animal_type=dog";
    private final String AMOUNT_PARAMETER = "&amount=";
    private final Integer MAX_RESPONSE_COUNT = 500;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final FactWithDatesAdapter factWithDatesAdapter = new FactWithDatesAdapter();
    private final FactWithUserAdapter factWithUserAdapter = new FactWithUserAdapter();


    public List<Fact> getQueuedFacts() {
        List<FactWithUser> factsToReturn = new ArrayList<>();

        ResponseEntity<String> responseString = restTemplate.getForEntity(
                DEFAULT_URL + ANIMAL_TYPE_PARAMETER,
                String.class
        );

        try {
            JsonNode responseNode = objectMapper.readTree(responseString.getBody());
            ObjectReader reader = objectMapper.readerFor(new TypeReference<List<FactWithUser>>() {
            });
            factsToReturn = reader.readValue(responseNode.get("all"));
        } catch (Exception e) {
            e.printStackTrace();
            return factWithUserAdapter.convertToFact(factsToReturn);
        }
        return factWithUserAdapter.convertToFact(factsToReturn);
    }


    public Fact getRandomFact() {
        FactWithDates factWithDates= restTemplate.getForObject(DEFAULT_URL + RANDOM_SUFFIX + ANIMAL_TYPE_PARAMETER, FactWithDates.class);
        return factWithDatesAdapter.convertToFact(factWithDates);
    }

    public List<Fact> getRandomFact(Integer amount) {
        if (amount <= 1) {
            return List.of(getRandomFact());
        }
        if (amount >= MAX_RESPONSE_COUNT) {
            return getRandomList(MAX_RESPONSE_COUNT);
        }

        return getRandomList(amount);
    }

    public Fact getFactByID(String factID) {
        FactWithDates retrievedFactWithDates = restTemplate.getForObject(DEFAULT_URL + factID, FactWithDates.class);
        return factWithDatesAdapter.convertToFact(retrievedFactWithDates);
    }

    private List<Fact> getRandomList(Integer amount) {
        ResponseEntity<FactWithDates[]> responseEntity = restTemplate.getForEntity(
                DEFAULT_URL + RANDOM_SUFFIX + ANIMAL_TYPE_PARAMETER + AMOUNT_PARAMETER + amount,
                FactWithDates[].class
        );
        FactWithDates[] factWithDates = responseEntity.getBody();
        return factWithDatesAdapter.convertToFact(
                Arrays.asList(factWithDates)
        );
    }
}
