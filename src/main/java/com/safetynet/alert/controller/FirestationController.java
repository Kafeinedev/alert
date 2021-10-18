package com.safetynet.alert.controller;

import java.util.List;

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
import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.service.FirestationService;

@RestController
public class FirestationController {

	@Autowired
	private FirestationService firestationService;

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

	@GetMapping("/flood/stations")
	public ResponseEntity<ArrayNode> floodStations(@RequestParam("stations") List<String> stations) {
		return new ResponseEntity<>(firestationService.stations(stations), HttpStatus.OK);
	}

	@GetMapping("/phoneAlert")
	public ResponseEntity<ArrayNode> phoneAlert(@RequestParam("firestation") String stationNumber) {
		return new ResponseEntity<>(firestationService.phoneAlert(stationNumber), HttpStatus.OK);
	}

	@GetMapping("/firestation")
	public ResponseEntity<ObjectNode> firestation(@RequestParam("stationNumber") String stationNumber) {
		return new ResponseEntity<>(firestationService.firestation(stationNumber), HttpStatus.OK);
	}

	@GetMapping("/fire")
	public ResponseEntity<ObjectNode> fire(@RequestParam("address") String address) {
		return new ResponseEntity<>(firestationService.fire(address), HttpStatus.OK);
	}
}
