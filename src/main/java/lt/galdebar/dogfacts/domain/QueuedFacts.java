package lt.galdebar.dogfacts.domain;

import lombok.Data;

import java.util.List;

@Data
public class QueuedFacts {
    private List<Fact> all; //field name corresponds with JSON field name- for easier mapping
}
