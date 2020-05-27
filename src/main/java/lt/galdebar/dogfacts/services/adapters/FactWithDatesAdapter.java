package lt.galdebar.dogfacts.services.adapters;

import lt.galdebar.dogfacts.domain.Fact;
import lt.galdebar.dogfacts.domain.external.FactWithDates;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FactWithDatesAdapter implements IsFactAdapter<FactWithDates> {
    @Override
    public Fact convertToFact(FactWithDates external) {
        return new Fact(
                external.get_id(),
                external.getText(),
                external.getUser()
        );
    }

    @Override
    public List<Fact> convertToFact(List<FactWithDates> externalList) {
        return externalList.stream().map(this::convertToFact).collect(Collectors.toList());
    }
}
