package lt.galdebar.dogfacts.services.adapters;

import lt.galdebar.dogfacts.domain.Fact;

import java.util.List;

public interface IsFactAdapter< T> {
    Fact convertToFact(T external);
    List<Fact> convertToFact(List<T> externalList);
}
