package lt.galdebar.dogfacts.controllers;

import lt.galdebar.dogfacts.services.Exceptions.FailedToRetrieveResource;
import lt.galdebar.dogfacts.services.FactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/facts")
public class FactController {

    @Autowired
    private FactService factService;

    @GetMapping()
    ResponseEntity getFacts() {
        try {
            return ResponseEntity.ok(factService.getQueuedFacts());
        } catch (FailedToRetrieveResource | IOException failedToRetrieveResource) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/random")
    ResponseEntity getRandomFact(@RequestParam(required = false) Integer amount) {
        try {
            if (amount == null || amount <= 1) {
                return ResponseEntity.ok(factService.getRandomFact());
            }
            return ResponseEntity.ok(factService.getRandomFact(amount));
        } catch (FailedToRetrieveResource e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity getFactByID(@PathVariable(required = false) String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return ResponseEntity.ok(factService.getRandomFact());
            }
            return ResponseEntity.ok(
                    factService.getFactByID(id)
            );
        } catch (FailedToRetrieveResource e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
