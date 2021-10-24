package com.safetynet.alert.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.model.Person;

public class PersonUtil {

	public void addNameToNode(ObjectNode node, Person person) {
		node.put("firstName", person.getFirstName());
		node.put("lastName", person.getLastName());
	}

	public void addPhoneAgePatientHistoryToNode(ObjectNode node, Person person, MedicalRecord medicalRecord) {
		MedicalRecordUtil medicalRecordUtil = new MedicalRecordUtil();
		node.put("phone", person.getPhone());
		node.put("age", medicalRecordUtil.calculateAge(medicalRecord));
		node.set("patientHistory", medicalRecordUtil.patientHistory(medicalRecord));
	}
}
