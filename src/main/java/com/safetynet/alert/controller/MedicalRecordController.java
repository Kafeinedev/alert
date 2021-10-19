package com.safetynet.alert.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.service.MedicalRecordService;

@RestController
public class MedicalRecordController {

	private static Logger log = LogManager.getLogger("MedicalRecord Controller");

	@Autowired
	private MedicalRecordService medicalRecordService;

	@PostMapping("/medicalRecord")
	public ResponseEntity<MedicalRecord> postMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		log.info("Processing post request at \"/medicalRecord\" content =" + medicalRecord.toString());
		ResponseEntity<MedicalRecord> response;

		try {
			medicalRecordService.postMedicalRecord(medicalRecord);
			response = new ResponseEntity<>(medicalRecord, HttpStatus.CREATED);
			log.info("Post request at \"/medicalRecord\" successfull " + response.toString());
		} catch (FileAccessException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("Post request at \"/medicalRecord\" failure " + response.toString());
		} catch (EntityAlreadyPresentException e) {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			log.error("Post request at \"/medicalRecord\" failure " + response.toString());
		}

		return response;
	}

	@PutMapping("/medicalRecord")
	public ResponseEntity<MedicalRecord> putMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		log.info("Processing put request at \"/medicalRecord\" content =" + medicalRecord.toString());
		ResponseEntity<MedicalRecord> response;

		try {
			medicalRecordService.putMedicalRecord(medicalRecord);
			response = new ResponseEntity<>(medicalRecord, HttpStatus.OK);
			log.info("Put request at \"/medicalRecord\" successfull " + response.toString());
		} catch (FileAccessException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("Put request at \"/medicalRecord\" failure " + response.toString());
		} catch (EntityMissingException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			log.error("Put request at \"/medicalRecord\" failure " + response.toString());
		}

		return response;
	}

	@DeleteMapping("/medicalRecord")
	public ResponseEntity<MedicalRecord> deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		log.info("Processing delete request at \"/medicalRecord\" content =" + medicalRecord.toString());
		ResponseEntity<MedicalRecord> response;

		try {
			medicalRecordService.deleteMedicalRecord(medicalRecord);
			response = new ResponseEntity<>(medicalRecord, HttpStatus.OK);
			log.info("Delete request at \"/medicalRecord\" successfull " + response.toString());
		} catch (FileAccessException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("Delete request at \"/medicalRecord\" failure " + response.toString());
		} catch (EntityMissingException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			log.error("Delete request at \"/medicalRecord\" failure " + response.toString());
		}

		return response;
	}
}
