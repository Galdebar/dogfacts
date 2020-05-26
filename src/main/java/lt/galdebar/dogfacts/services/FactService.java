package lt.galdebar.dogfacts.services;

import lt.galdebar.dogfacts.domain.Fact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class FactService {

    private final String DEFAULT_URL = "https://cat-fact.herokuapp.com/facts";
    private final String RANDOM_SUFFIX = "/random";
    private final String ANIMAL_TYPE_PARAMETER = "?animal_type=dog";
    private final String AMOUNT_PARAMETER = "&amount=";
    private final Integer MIN_RESPONSE_COUNT = 1;
    private final Integer MAX_RESPONSE_COUNT = 500;

    @Autowired
    private RestTemplate restTemplate;

    public Fact getRandomFact() {
        return restTemplate.getForObject(DEFAULT_URL + RANDOM_SUFFIX + ANIMAL_TYPE_PARAMETER, Fact.class);
    }

    public List<Fact> getRandomFact(Integer amount) {
        if (amount <= 1) {
            return List.of(getRandomFact());
        }
        if (amount >= MAX_RESPONSE_COUNT) {
            return getList(MAX_RESPONSE_COUNT);
        }

        return getList(amount);
    }

    private List<Fact> getList(Integer amount) {
        ResponseEntity<Fact[]> factEntities = restTemplate.getForEntity(
                DEFAULT_URL + RANDOM_SUFFIX + ANIMAL_TYPE_PARAMETER + AMOUNT_PARAMETER + amount, Fact[].class
        );
        Fact[] facts = factEntities.getBody();
        return Arrays.asList(facts);
    }


}
