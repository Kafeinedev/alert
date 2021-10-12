package com.safetynet.alert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alert.model.Person;
import com.safetynet.alert.service.CRUDPersonService;

@RestController
public class CRUDPersonController {

	@Autowired
	private CRUDPersonService personService;

	@PostMapping("/person")
	public ResponseEntity<Person> postPerson(@RequestBody Person person) {
		personService.postPerson(person);
		return new ResponseEntity<>(person, HttpStatus.CREATED);
	}

	@PutMapping("/person")
	public ResponseEntity<Person> putPerson(@RequestBody Person person) {
		personService.putPerson(person);
		return new ResponseEntity<>(person, HttpStatus.OK);
	}

	@DeleteMapping("/person")
	public ResponseEntity<Person> deletePerson(@RequestBody Person person) {
		personService.deletePerson(person);
		return new ResponseEntity<>(person, HttpStatus.OK);
	}
}
