package lt.galdebar.dogfacts.controllers;

import lt.galdebar.dogfacts.services.FactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facts")
public class FactController {

    @Autowired
    private FactService factService;

    @GetMapping()
    ResponseEntity getFacts() {
        return ResponseEntity.ok(factService.getQueuedFacts());
    }

    @GetMapping("/random")
    ResponseEntity getRandomFact(@RequestParam(required = false) Integer amount) {
        if (amount == null || amount <= 1) {
            return ResponseEntity.ok(factService.getRandomFact());
        }
        return ResponseEntity.ok(factService.getRandomFact(amount));
    }

    @GetMapping("/{id}")
    ResponseEntity getFactByID(@PathVariable(required = false) String id) {
        if(id == null || id.trim().isEmpty()){
            return getRandomFact(null);
        }
        return ResponseEntity.ok(
                factService.getFactByID(id)
        );
    }
}
