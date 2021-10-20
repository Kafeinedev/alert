package com.safetynet.alert.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.model.Person;

public class PersonUtil {

	public static void addNameToNode(ObjectNode node, Person person) {
		node.put("firstName", person.getFirstName());
		node.put("lastName", person.getLastName());
	}

	public static void addPhoneAgePatientHistoryToNode(ObjectNode node, Person person, MedicalRecord medicalRecord) {
		node.put("phone", person.getPhone());
		node.put("age", MedicalRecordUtil.calculateAge(medicalRecord));
		node.set("patientHistory", MedicalRecordUtil.patientHistory(medicalRecord));
	}
}
