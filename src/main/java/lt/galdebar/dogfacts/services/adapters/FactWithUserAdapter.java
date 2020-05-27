package lt.galdebar.dogfacts.services.adapters;

import lt.galdebar.dogfacts.domain.Fact;
import lt.galdebar.dogfacts.domain.external.FactWithUser;

import java.util.List;
import java.util.stream.Collectors;

public class FactWithUserAdapter implements IsFactAdapter<FactWithUser> {

    @Override
    public Fact convertToFact(FactWithUser external) {
        return new Fact(
                external.get_id(),
                external.getText(),
                external.getUser().get_id()
        );
    }

    @Override
    public List<Fact> convertToFact(List<FactWithUser> externalList) {
        return externalList.stream().map(this::convertToFact).collect(Collectors.toList());
    }
}
