package lt.galdebar.dogfacts.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fact {
    @JsonProperty("_id")
    private String id;
    private String text;
}
