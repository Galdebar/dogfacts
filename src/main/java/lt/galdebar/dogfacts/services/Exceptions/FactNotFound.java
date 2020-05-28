package lt.galdebar.dogfacts.services.Exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class FactNotFound extends RuntimeException {
    public FactNotFound(String message) {
        super(message);
    }
}
