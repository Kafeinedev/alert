package com.safetynet.alert.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.service.PersonService;

@RestController
public class PersonController {

	private static Logger log = LogManager.getLogger();

	@Autowired
	private PersonService personService;

	@PostMapping("/person")
	public ResponseEntity<Person> postPerson(@RequestBody Person person) {
		log.info("Processing post request at \"/person\" content =" + person.toString());
		ResponseEntity<Person> response;

		try {
			personService.postPerson(person);
			response = new ResponseEntity<>(person, HttpStatus.CREATED);
			log.info("Post request at \"/person\" successfull " + response.toString());
		} catch (FileAccessException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("Post request at \"/person\" failure " + response.toString());
		} catch (EntityAlreadyPresentException e) {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			log.error("Post request at \"/person\" failure " + response.toString());
		}

		return response;
	}

	@PutMapping("/person")
	public ResponseEntity<Person> putPerson(@RequestBody Person person) {
		log.info("Processing put request at \"/person\" content =" + person.toString());
		ResponseEntity<Person> response;

		try {
			personService.putPerson(person);
			response = new ResponseEntity<>(person, HttpStatus.OK);
			log.info("Put request at \"/person\" successfull " + response.toString());
		} catch (FileAccessException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("Put request at \"/person\" failure " + response.toString());
		} catch (EntityMissingException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			log.error("Put request at \"/person\" failure " + response.toString());
		}

		return response;
	}

	@DeleteMapping("/person")
	public ResponseEntity<Person> deletePerson(@RequestBody Person person) {
		log.info("Processing delete request at \"/person\" content =" + person.toString());
		ResponseEntity<Person> response;

		try {
			personService.deletePerson(person);
			response = new ResponseEntity<>(person, HttpStatus.OK);
			log.info("Delete request at \"/person\" successfull " + response.toString());
		} catch (FileAccessException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("Delete request at \"/person\" failure " + response.toString());
		} catch (EntityMissingException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			log.error("Delete request at \"/person\" failure " + response.toString());
		}

		return response;
	}

	@GetMapping("/communityEmail")
	public ResponseEntity<ArrayNode> communityEmail(@RequestParam("city") String city) {
		log.info("Processing get request at \"/communityEmail\" city=" + city);
		ResponseEntity<ArrayNode> response;

		ArrayNode content = personService.communityEmail(city);
		response = new ResponseEntity<>(content, HttpStatus.OK);
		log.info("get request at \"/communityEmail\" successfull " + response.toString());

		return response;
	}

	@GetMapping("/personInfo")
	public ResponseEntity<ArrayNode> personInfo(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName) {
		log.info("Processing get request at \"/personInfo\" firstName=" + firstName + " lastName=" + lastName);
		ResponseEntity<ArrayNode> response;

		ArrayNode content = personService.personInfo(lastName);
		response = new ResponseEntity<>(content, HttpStatus.OK);
		log.info("get request at \"/personInfo\" successfull " + response.toString());

		return response;
	}

	@GetMapping("/childAlert")
	public ResponseEntity<ObjectNode> childAlert(@RequestParam("address") String address) {
		log.info("Processing get request at \"/childAlert\" address" + address);
		ResponseEntity<ObjectNode> response;

		ObjectNode content = personService.childAlert(address);
		response = new ResponseEntity<>(content, HttpStatus.OK);
		log.info("get request at \"/childAlert\" successfull " + response.toString());

		return response;
	}
}
