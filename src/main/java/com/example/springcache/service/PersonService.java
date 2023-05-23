package com.example.springcache.service;

import com.example.springcache.model.Person;

import java.util.List;

public interface PersonService {

    List<Person> getAllPerson();

    Person createPerson(Person person);

    Person getPersonByName(String name);

    Person updatePerson(Long id, Person person);

    String deleteAllPerson();

    void deletePerson(String name);

    String clearCache();
}
