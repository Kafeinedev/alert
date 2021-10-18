package com.safetynet.alert.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alert.model.MedicalRecord;

@Component
public class MedicalRecordUtil {

	@Autowired
	private static ObjectMapper mapper;

	public static long calculateAge(MedicalRecord medicalRecord) {
		LocalDate current = LocalDate.now();
		LocalDate birthdate = LocalDate.parse(medicalRecord.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));

		return ChronoUnit.YEARS.between(birthdate, current);
	}

	public static ObjectNode patientHistory(MedicalRecord medicalRecord) {
		ObjectNode patientHistory = mapper.createObjectNode();
		ArrayNode medications = mapper.createArrayNode();
		ArrayNode allergies = mapper.createArrayNode();

		for (String medication : medicalRecord.getMedications()) {
			medications.add(medication);
		}
		for (String allergy : medicalRecord.getAllergies()) {
			allergies.add(allergy);
		}

		patientHistory.set("medications", medications);
		patientHistory.set("allergies", allergies);
		return patientHistory;
	}
}
