package com.safetynet.alert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.service.PersonService;

@RestController
public class PersonController {

	@Autowired
	private PersonService personService;

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

	@GetMapping("/communityEmail")
	public ResponseEntity<ArrayNode> communityEmail(@RequestParam("city") String city) {
		return new ResponseEntity<>(personService.communityEmail(city), HttpStatus.OK);
	}

	@GetMapping("/personInfo")
	public ResponseEntity<ArrayNode> personInfo(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName) {
		return new ResponseEntity<>(personService.personInfo(lastName), HttpStatus.OK);
	}

	@GetMapping("/childAlert")
	public ResponseEntity<ObjectNode> childAlert(@RequestParam("address") String address) {
		return new ResponseEntity<>(personService.childAlert(address), HttpStatus.OK);
	}
}
