package com.safetynet.alert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.service.CRUDService;

@RestController
public class CRUDController {

	@Autowired
	private CRUDService service;

	@PostMapping("/person")
	public ResponseEntity<Person> postPerson(@RequestBody Person person) {
		// TODO: Maybe make ResponseEntity body return url to created entity
		service.postPerson(person);
		return new ResponseEntity<>(person, HttpStatus.CREATED);
	}

	@PostMapping("/firestation")
	public ResponseEntity<Firestation> postFirestationMapping(@RequestBody Firestation firestation) {
		// TODO: Maybe make ResponseEntity body return url to created entity
		service.postFirestationMapping(firestation);
		return new ResponseEntity<>(firestation, HttpStatus.CREATED);
	}

	@PostMapping("/medicalRecord")
	public ResponseEntity<MedicalRecord> postMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		// TODO: Maybe make ResponseEntity body return url to created entity
		service.postMedicalRecord(medicalRecord);
		return new ResponseEntity<>(medicalRecord, HttpStatus.CREATED);
	}

	@PutMapping("/person")
	public ResponseEntity<Person> putPerson(@RequestBody Person person) {
		service.putPerson(person);
		return new ResponseEntity<>(person, HttpStatus.OK);
	}

	@PutMapping("/firestation")
	public ResponseEntity<Firestation> putFirestationMapping(@RequestBody Firestation firestation) {
		service.putFirestationMapping(firestation);
		return new ResponseEntity<>(firestation, HttpStatus.OK);
	}

	@PutMapping("/medicalRecord")
	public ResponseEntity<MedicalRecord> putMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		service.putMedicalRecord(medicalRecord);
		return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
	}

	@DeleteMapping("/person")
	public ResponseEntity<Person> deletePerson(@RequestBody Person person) {
		service.deletePerson(person);
		return new ResponseEntity<>(person, HttpStatus.OK);
	}

	@DeleteMapping("/firestation")
	public ResponseEntity<Firestation> deleteFirestationMapping(@RequestBody Firestation firestation) {
		service.deleteFirestationMapping(firestation);
		return new ResponseEntity<>(firestation, HttpStatus.OK);
	}

	@DeleteMapping("/medicalRecord")
	public ResponseEntity<MedicalRecord> deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		service.deleteMedicalRecord(medicalRecord);
		return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
	}
}
