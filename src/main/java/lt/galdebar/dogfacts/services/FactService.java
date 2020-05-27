package lt.galdebar.dogfacts.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lt.galdebar.dogfacts.domain.Fact;
import lt.galdebar.dogfacts.domain.external.FactWithDates;
import lt.galdebar.dogfacts.domain.external.FactWithUser;
import lt.galdebar.dogfacts.services.Exceptions.FailedToRetrieveResource;
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


    public List<Fact> getQueuedFacts() throws FailedToRetrieveResource, IOException {
        List<FactWithUser> factsToReturn = new ArrayList<>();

        ResponseEntity<String> responseString = restTemplate.getForEntity(
                DEFAULT_URL + ANIMAL_TYPE_PARAMETER,
                String.class
        );

        checkIfFalid(responseString);

        JsonNode responseNode = objectMapper.readTree(responseString.getBody());
        ObjectReader reader = objectMapper.readerFor(new TypeReference<List<FactWithUser>>() {
        });
        factsToReturn = reader.readValue(responseNode.get("all"));

        return factWithUserAdapter.convertToFact(factsToReturn);
    }


    public Fact getRandomFact() throws FailedToRetrieveResource {
        FactWithDates factWithDates = restTemplate.getForObject(DEFAULT_URL + RANDOM_SUFFIX + ANIMAL_TYPE_PARAMETER, FactWithDates.class);
        checkIfFalid(factWithDates);
        return factWithDatesAdapter.convertToFact(factWithDates);
    }

    public List<Fact> getRandomFact(Integer amount) throws FailedToRetrieveResource {
        if (amount <= 1) {
            return List.of(getRandomFact());
        }
        if (amount >= MAX_RESPONSE_COUNT) {
            return getRandomList(MAX_RESPONSE_COUNT);
        }

        return getRandomList(amount);
    }

    public Fact getFactByID(String factID) throws FailedToRetrieveResource {
        FactWithDates retrievedFactWithDates = restTemplate.getForObject(DEFAULT_URL + factID, FactWithDates.class);
        checkIfFalid(retrievedFactWithDates);
        return factWithDatesAdapter.convertToFact(retrievedFactWithDates);
    }

    private List<Fact> getRandomList(Integer amount) throws FailedToRetrieveResource {
        ResponseEntity<FactWithDates[]> responseEntity = restTemplate.getForEntity(
                DEFAULT_URL + RANDOM_SUFFIX + ANIMAL_TYPE_PARAMETER + AMOUNT_PARAMETER + amount,
                FactWithDates[].class
        );
        checkIfFalid(responseEntity);
        FactWithDates[] factsWithDates = responseEntity.getBody();
        return factWithDatesAdapter.convertToFact(
                Arrays.asList(factsWithDates)
        );
    }

    private void checkIfFalid(Object object) throws FailedToRetrieveResource {
        if(object == null){
            throw new FailedToRetrieveResource();
        }
    }
}
