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
import com.safetynet.alert.service.CRUDFirestationService;
import com.safetynet.alert.service.CRUDMedicalRecordService;
import com.safetynet.alert.service.CRUDPersonService;

@RestController
public class CRUDController {

	@Autowired
	private CRUDPersonService personService;

	@Autowired
	private CRUDFirestationService firestationService;

	@Autowired
	private CRUDMedicalRecordService medicalRecordService;

	@PostMapping("/person")
	public ResponseEntity<Person> postPerson(@RequestBody Person person) {
		// TODO: Maybe make ResponseEntity body return url to created entity
		personService.postPerson(person);
		return new ResponseEntity<>(person, HttpStatus.CREATED);
	}

	@PostMapping("/firestation")
	public ResponseEntity<Firestation> postFirestationMapping(@RequestBody Firestation firestation) {
		// TODO: Maybe make ResponseEntity body return url to created entity
		firestationService.postFirestationMapping(firestation);
		return new ResponseEntity<>(firestation, HttpStatus.CREATED);
	}

	@PostMapping("/medicalRecord")
	public ResponseEntity<MedicalRecord> postMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		// TODO: Maybe make ResponseEntity body return url to created entity
		medicalRecordService.postMedicalRecord(medicalRecord);
		return new ResponseEntity<>(medicalRecord, HttpStatus.CREATED);
	}

	@PutMapping("/person")
	public ResponseEntity<Person> putPerson(@RequestBody Person person) {
		personService.putPerson(person);
		return new ResponseEntity<>(person, HttpStatus.OK);
	}

	@PutMapping("/firestation")
	public ResponseEntity<Firestation> putFirestationMapping(@RequestBody Firestation firestation) {
		firestationService.putFirestationMapping(firestation);
		return new ResponseEntity<>(firestation, HttpStatus.OK);
	}

	@PutMapping("/medicalRecord")
	public ResponseEntity<MedicalRecord> putMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		medicalRecordService.putMedicalRecord(medicalRecord);
		return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
	}

	@DeleteMapping("/person")
	public ResponseEntity<Person> deletePerson(@RequestBody Person person) {
		personService.deletePerson(person);
		return new ResponseEntity<>(person, HttpStatus.OK);
	}

	@DeleteMapping("/firestation")
	public ResponseEntity<Firestation> deleteFirestationMapping(@RequestBody Firestation firestation) {
		firestationService.deleteFirestationMapping(firestation);
		return new ResponseEntity<>(firestation, HttpStatus.OK);
	}

	@DeleteMapping("/medicalRecord")
	public ResponseEntity<MedicalRecord> deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		medicalRecordService.deleteMedicalRecord(medicalRecord);
		return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
	}
}
