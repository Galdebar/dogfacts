package lt.galdebar.dogfacts.domain.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class FactWithDates {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String source;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String _id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String user;
}
