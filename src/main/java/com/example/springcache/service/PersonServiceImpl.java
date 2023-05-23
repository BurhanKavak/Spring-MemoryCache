package com.example.springcache.service;

import com.example.springcache.model.Person;
import com.example.springcache.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService{

    private final PersonRepository personRepository;

    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Cacheable("persons")
    public List<Person> getAllPerson() {

        try {
            logger.info("Cagirmadan once 2 saniye bekle");
            Thread.sleep(1000 * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return personRepository.findAll();
    }

    @Override
    @CachePut(value = "persons",key = "#person.name" )
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    @Cacheable(value = "persons",key = "#name",condition = "#name.length() > 4", unless = "#result.age < 24" )
    public Person getPersonByName(String name)  {

        try {
            logger.info("Cagirmadan once 2 saniye bekle");
            Thread.sleep(1000 * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return personRepository.findByName(name).orElseThrow(() -> new RuntimeException("Person bulunamadi"));
    }

    @Override
    @CachePut(value = "persons", key = "#person.name")
    public Person updatePerson(Long id, Person person) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        optionalPerson.ifPresent(value -> person.setId(id));
        return personRepository.save(person);
    }

    @Override
    @CacheEvict(value = "persons", allEntries = true)
    public String deleteAllPerson() {
         personRepository.deleteAll();
         return "Hepsi silindi";
    }

    @Override
    @CacheEvict(value = "persons", key = "#name")
    public void deletePerson(String name) {
        Optional<Person> optionalPerson = personRepository.findByName(name);
        optionalPerson.ifPresent(value -> personRepository.deleteById(value.getId()));
    }

    @Override
    @CacheEvict(value = "persons", allEntries = true)
    public String clearCache() {
        return "Cache tamamen temizlendi";
    }
}
