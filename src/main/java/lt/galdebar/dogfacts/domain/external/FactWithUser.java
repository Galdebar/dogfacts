package lt.galdebar.dogfacts.domain.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class FactWithUser {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String _id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private User user;


    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class User{
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private String _id;

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        private Name name;


        @JsonIgnoreProperties(ignoreUnknown = true)
        @Data
        public class Name{
            @JsonFormat(shape = JsonFormat.Shape.STRING)
            private String first;

            @JsonFormat(shape = JsonFormat.Shape.STRING)
            private String last;
        }

    }
}
