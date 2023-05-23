package com.example.springcache.controller;

import com.example.springcache.model.Person;
import com.example.springcache.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {


    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Person>> getPersons() {
        logger.info("Tum liste taraniyor");
        return ResponseEntity.ok(personService.getAllPerson());
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        logger.info("Person olusturuluyor: {}", person);
        return new ResponseEntity<>(personService.createPerson(person), HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Person> findByPersonId(@PathVariable("name") String name) {
        logger.info("Isim araniyor: {}",name);
        return ResponseEntity.ok(personService.getPersonByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson (@PathVariable("id") Long id,@RequestBody Person person) {
        logger.info("Guncellenen Person Id'si: {}",id);
        return new ResponseEntity<>(personService.updatePerson(id, person),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<String> deleteAllPerson() {
        personService.deleteAllPerson();
        return new ResponseEntity<>("Tum liste silindi!",HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deletePerson (@PathVariable("name") String name) {
        logger.info("{} Silindi",name);
        personService.deletePerson(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/cacheClear")
    public ResponseEntity<String> clearCache() {
        personService.clearCache();
        return new ResponseEntity<>("Cache Temizlendi",HttpStatus.NO_CONTENT);
    }

}
