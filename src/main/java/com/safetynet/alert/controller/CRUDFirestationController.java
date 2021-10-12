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
import com.safetynet.alert.service.CRUDFirestationService;

@RestController
public class CRUDFirestationController {

	@Autowired
	private CRUDFirestationService firestationService;

	@PostMapping("/firestation")
	public ResponseEntity<Firestation> postFirestationMapping(@RequestBody Firestation firestation) {
		firestationService.postFirestationMapping(firestation);
		return new ResponseEntity<>(firestation, HttpStatus.CREATED);
	}

	@PutMapping("/firestation")
	public ResponseEntity<Firestation> putFirestationMapping(@RequestBody Firestation firestation) {
		firestationService.putFirestationMapping(firestation);
		return new ResponseEntity<>(firestation, HttpStatus.OK);
	}

	@DeleteMapping("/firestation")
	public ResponseEntity<Firestation> deleteFirestationMapping(@RequestBody Firestation firestation) {
		firestationService.deleteFirestationMapping(firestation);
		return new ResponseEntity<>(firestation, HttpStatus.OK);
	}
}
