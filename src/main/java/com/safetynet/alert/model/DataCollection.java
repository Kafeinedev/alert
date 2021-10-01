package com.safetynet.alert.model;

import java.util.List;

public class DataCollection {

	List<Person> persons;
	List<Firestation> firestations;
	List<MedicalRecord> medicalrecords;

	public DataCollection() {
	}

	public DataCollection(List<Person> persons, List<Firestation> firestations, List<MedicalRecord> medicalrecords) {
		this.persons = persons;
		this.firestations = firestations;
		this.medicalrecords = medicalrecords;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public List<Firestation> getFirestations() {
		return firestations;
	}

	public void setFirestations(List<Firestation> firestations) {
		this.firestations = firestations;
	}

	public List<MedicalRecord> getMedicalrecords() {
		return medicalrecords;
	}

	public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
		this.medicalrecords = medicalrecords;
	}

	@Override
	public String toString() {
		return "DataCollection [persons=" + persons + ", firestations=" + firestations + ", medicalrecords="
				+ medicalrecords + "]";
	}

}
