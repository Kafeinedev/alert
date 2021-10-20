package com.safetynet.alert.controller;

import java.util.List;

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
import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.service.FirestationService;

@RestController
public class FirestationController {

	private static Logger log = LogManager.getLogger("Firestation Controller");

	@Autowired
	private FirestationService firestationService;

	@PostMapping("/firestation")
	public ResponseEntity<Firestation> postFirestationMapping(@RequestBody Firestation firestation) {
		log.info("Processing post request at \"/firestation\" content =" + firestation.toString());
		ResponseEntity<Firestation> response;

		try {
			firestationService.postFirestationMapping(firestation);
			response = new ResponseEntity<>(firestation, HttpStatus.CREATED);
			log.info("Post request at \"/firestation\" successfull " + response.toString());
		} catch (FileAccessException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("Post request at \"/firestation\" failure " + response.toString());
		} catch (EntityAlreadyPresentException e) {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			log.error("Post request at \"/firestation\" failure " + response.toString());
		}

		return response;
	}

	@PutMapping("/firestation")
	public ResponseEntity<Firestation> putFirestationMapping(@RequestBody Firestation firestation) {
		log.info("Processing put request at \"/firestation\" content =" + firestation.toString());
		ResponseEntity<Firestation> response;

		try {
			firestationService.putFirestationMapping(firestation);
			response = new ResponseEntity<>(firestation, HttpStatus.OK);
			log.info("Put request at \"/firestation\" successfull " + response.toString());
		} catch (FileAccessException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("Put request at \"/firestation\" failure " + response.toString());
		} catch (EntityMissingException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			log.error("Put request at \"/firestation\" failure " + response.toString());
		}

		return response;
	}

	@DeleteMapping("/firestation")
	public ResponseEntity<Firestation> deleteFirestationMapping(@RequestBody Firestation firestation) {
		log.info("Processing delete request at \"/firestation\" content =" + firestation.toString());
		ResponseEntity<Firestation> response;

		try {
			firestationService.deleteFirestationMapping(firestation);
			response = new ResponseEntity<>(firestation, HttpStatus.OK);
			log.info("Delete request at \"/firestation\" successfull " + response.toString());
		} catch (FileAccessException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("Delete request at \"/firestation\" failure " + response.toString());
		} catch (EntityMissingException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			log.error("Delete request at \"/firestation\" failure " + response.toString());
		}

		return response;
	}

	@GetMapping("/flood/stations")
	public ResponseEntity<ArrayNode> floodStations(@RequestParam("stations") List<String> stations) {
		log.info("Processing get request at \"/flood/stations\" stations=" + stations.toString());
		ResponseEntity<ArrayNode> response;

		try {
			ArrayNode content = firestationService.stations(stations);
			response = new ResponseEntity<>(content, HttpStatus.OK);
			log.info("get request at \"/flood/stations\" successfull " + response.toString());
		} catch (FileAccessException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("get request at \"/flood/stations\" failure " + response.toString());
		}

		return response;
	}

	@GetMapping("/phoneAlert")
	public ResponseEntity<ArrayNode> phoneAlert(@RequestParam("firestation") String stationNumber) {
		log.info("Processing get request at \"/phoneAlert\" firestation=" + stationNumber);
		ResponseEntity<ArrayNode> response;

		try {
			ArrayNode content = firestationService.phoneAlert(stationNumber);
			response = new ResponseEntity<>(content, HttpStatus.OK);
			log.info("get request at \"/phoneAlert\" successfull " + response.toString());
		} catch (FileAccessException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("get request at \"/phoneAlert\" failure " + response.toString());
		}

		return response;
	}

	@GetMapping("/firestation")
	public ResponseEntity<ObjectNode> firestation(@RequestParam("stationNumber") String stationNumber) {
		log.info("Processing get request at \"/firestation\" stationNumber=" + stationNumber);
		ResponseEntity<ObjectNode> response;

		try {
			ObjectNode content = firestationService.firestation(stationNumber);
			response = new ResponseEntity<>(content, HttpStatus.OK);
			log.info("get request at \"/firestation\" successfull " + response.toString());
		} catch (FileAccessException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("get request at \"/firestation\" failure " + response.toString());
		}

		return response;
	}

	@GetMapping("/fire")
	public ResponseEntity<ObjectNode> fire(@RequestParam("address") String address) {
		log.info("Processing get request at \"/fire\" address=" + address);
		ResponseEntity<ObjectNode> response;

		try {
			ObjectNode content = firestationService.fire(address);
			response = new ResponseEntity<>(content, HttpStatus.OK);
			log.info("get request at \"/fire\" successfull " + response.toString());
		} catch (FileAccessException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("get request at \"/fire\" failure " + response.toString());
		}

		return response;
	}
}
