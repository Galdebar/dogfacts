package lt.galdebar.dogfacts.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fact {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String _id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String userID;
}
