package com.safetynet.alert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.FirestationRepository;
import com.safetynet.alert.repository.MedicalRecordRepository;
import com.safetynet.alert.repository.PersonRepository;

@Service
public class CRUDService {

	@Autowired
	private FirestationRepository firestationRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Autowired
	private PersonRepository personRepository;

	public void postPerson(Person person) {
		personRepository.add(person);
	}

	public void postFirestationMapping(Firestation firestation) {
		firestationRepository.add(firestation);
	}

	public void postMedicalRecord(MedicalRecord medicalRecord) {
		medicalRecordRepository.add(medicalRecord);
	}

	public void putPerson(Person person) {
		personRepository.update(person);
	}

	public void putFirestationMapping(Firestation firestation) {
		firestationRepository.update(firestation);
	}

	public void putMedicalRecord(MedicalRecord medicalRecord) {
		medicalRecordRepository.update(medicalRecord);
	}

	public void deletePerson(Person person) {
		personRepository.delete(person);
	}

	public void deleteFirestationMapping(Firestation firestation) {
		if (firestation.getAddress() != null) {
			firestationRepository.deleteAddressMapping(firestation);
		} else if (firestation.getStation() != null) {
			firestationRepository.deleteStationNumberMapping(firestation);
		}
	}

	public void deleteMedicalRecord(MedicalRecord medicalRecord) {
		medicalRecordRepository.delete(medicalRecord);
	}

}
