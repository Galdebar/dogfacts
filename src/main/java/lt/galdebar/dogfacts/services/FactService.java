package lt.galdebar.dogfacts.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lt.galdebar.dogfacts.domain.Fact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FactService {

    private final String DEFAULT_URL = "https://cat-fact.herokuapp.com/facts/";
    private final String RANDOM_SUFFIX = "random";
    private final String ANIMAL_TYPE_PARAMETER = "?animal_type=dog";
    private final String AMOUNT_PARAMETER = "&amount=";
    private final Integer MIN_RESPONSE_COUNT = 1;
    private final Integer MAX_RESPONSE_COUNT = 500;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List<Fact> getQueuedFacts() {
        List<Fact> returnList = new ArrayList<>();
        ResponseEntity<String> responseString = restTemplate.getForEntity(
                DEFAULT_URL + ANIMAL_TYPE_PARAMETER,
                String.class
        );

        try {
            JsonNode response = objectMapper.readTree(responseString.getBody());
            returnList = parseResponseWithUserObject(response);
            return returnList;
        } catch (Exception e) {
            e.printStackTrace();
            return returnList;
        }
    }

    public Fact getRandomFact() {
        return restTemplate.getForObject(DEFAULT_URL + RANDOM_SUFFIX + ANIMAL_TYPE_PARAMETER, Fact.class);
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
        Fact retrievedFact = restTemplate.getForObject(DEFAULT_URL + factID, Fact.class);
        return retrievedFact;
    }

    private List<Fact> getRandomList(Integer amount) {
        ResponseEntity<Fact[]> responseEntity = restTemplate.getForEntity(
                DEFAULT_URL + RANDOM_SUFFIX + ANIMAL_TYPE_PARAMETER + AMOUNT_PARAMETER + amount,
                Fact[].class
        );
        Fact[] facts = responseEntity.getBody();
        return Arrays.asList(facts);
    }

    private List<Fact> parseResponseWithUserObject(JsonNode response){
        List<Fact> facts = new ArrayList<>();
        JsonNode factsListNode = response.get("all");
        factsListNode.forEach(node -> {
            String id = node.get("_id").textValue();
            Fact foundFact = getFactByID(id);
            facts.add(foundFact);
        });
        return facts;
    }
}
