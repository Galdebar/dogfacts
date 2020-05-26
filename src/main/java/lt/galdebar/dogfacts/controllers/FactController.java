package lt.galdebar.dogfacts.controllers;

import lt.galdebar.dogfacts.services.FactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facts")
public class FactController {

    @Autowired
    private FactService factService;

    @GetMapping
    ResponseEntity getFacts(){
        return ResponseEntity.ok().body("Hello from Facts Controller");
        // should probably add some simple page here or smth...
    }

    @GetMapping("/random")
    ResponseEntity getRandomFact(@RequestParam(required = false) Integer amount){
        if(amount == null){
            return ResponseEntity.ok(factService.getRandomFact());
        }
        return ResponseEntity.ok(factService.getRandomFact(amount));
    }
}
