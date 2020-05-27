package lt.galdebar.dogfacts.controllers;

import lombok.RequiredArgsConstructor;
import lt.galdebar.dogfacts.domain.Fact;
import lt.galdebar.dogfacts.services.FactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/facts")
@RequiredArgsConstructor
public class FactController {

    private final FactService factService;

    @GetMapping
    List<Fact> getFacts() {
        return factService.getQueuedFacts();

    }

    @GetMapping("/random")
    ResponseEntity getRandomFact(@RequestParam(required = false) Integer amount) {
        if(amount == null){
            return ResponseEntity.ok(factService.getRandomFact());
        }
        return ResponseEntity.ok(factService.getRandomFact(amount));
    }


    @GetMapping("/{id}")
    Fact getFactByID(@PathVariable(required = false) String id) {
        return factService.getFactByID(id);
    }
}
