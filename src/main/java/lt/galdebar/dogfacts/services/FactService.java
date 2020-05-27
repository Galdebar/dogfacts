package lt.galdebar.dogfacts.services;

import lombok.RequiredArgsConstructor;
import lt.galdebar.dogfacts.domain.Fact;
import lt.galdebar.dogfacts.domain.QueuedFacts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.*;

@Service
@RequiredArgsConstructor
public class FactService {

    private static final String DEFAULT_URL = "https://cat-fact.herokuapp.com/facts";
    private static final String RANDOM_SUFFIX = "/random";
    private static final String QUERY_DOG = "?animal_type=dog";
    private static final String QUERY_AMOUNT = "&amount=";
    private static final Integer MAX_RESPONSE_COUNT = 500;

    private final RestTemplate restTemplate;


    public List<Fact> getQueuedFacts() {

        ResponseEntity<QueuedFacts> response = restTemplate.getForEntity(
                DEFAULT_URL + "/" + QUERY_DOG,
                QueuedFacts.class
        );
        requireNonNull(response);
        return response.getBody().getAll();
    }


    public Fact getRandomFact() {
        Fact response = restTemplate.getForObject(
                DEFAULT_URL + RANDOM_SUFFIX + QUERY_DOG,
                Fact.class
        );
        requireNonNull(response);
        return response;
    }

    public List<Fact> getRandomFact(Integer amount) {
        if (amount == null || amount <= 1) {
            return List.of(getRandomFact());
        }
        if (amount >= MAX_RESPONSE_COUNT) {
            return getRandomList(MAX_RESPONSE_COUNT);
        }

        return getRandomList(amount);
    }

    public Fact getFactByID(String factID) {
        Fact fact = null;
        try {
            fact = restTemplate.getForObject(
                    DEFAULT_URL + "/" + factID,
                    Fact.class
            );
        } catch (HttpClientErrorException e){
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                throw new FactNotFound("Incorrect fact id: " + factID);
            }
        }
        requireNonNull(fact);
        return fact;
    }

    private List<Fact> getRandomList(Integer amount) {
        ResponseEntity<Fact[]> responseEntity = restTemplate.getForEntity(
                DEFAULT_URL + RANDOM_SUFFIX + QUERY_DOG + QUERY_AMOUNT + amount,
                Fact[].class
        );
        requireNonNull(responseEntity);
        return Arrays.asList(responseEntity.getBody());
    }
}
