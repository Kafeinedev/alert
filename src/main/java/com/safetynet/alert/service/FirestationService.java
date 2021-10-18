package com.safetynet.alert.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.FirestationRepository;
import com.safetynet.alert.repository.MedicalRecordRepository;
import com.safetynet.alert.repository.PersonRepository;
import com.safetynet.alert.util.MedicalRecordUtil;
import com.safetynet.alert.util.PersonUtil;

@Service
public class FirestationService {

	@Autowired
	private FirestationRepository firestationRepository;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Autowired
	private ObjectMapper mapper;

	public void postFirestationMapping(Firestation firestation) {
		firestationRepository.add(firestation);
	}

	public void putFirestationMapping(Firestation firestation) {
		firestationRepository.update(firestation);
	}

	public void deleteFirestationMapping(Firestation firestation) {
		if (firestation.getAddress() != null) {
			firestationRepository.deleteAddressMapping(firestation);
		} else if (firestation.getStation() != null) {
			firestationRepository.deleteStationNumberMapping(firestation);
		}
	}

	public ArrayNode stations(List<String> stationNumbers) {
		ArrayNode houses = mapper.createArrayNode();
		for (String station : stationNumbers) {
			houses.add(station(station));
		}
		return houses;
	}

	private ArrayNode station(String stationNumber) {
		ArrayNode houses = mapper.createArrayNode();

		for (String address : firestationRepository.findByStation(stationNumber)) {
			ObjectNode house = mapper.createObjectNode();
			house.put("address", address);
			ArrayNode inhabitants = mapper.createArrayNode();

			for (Person person : personRepository.findByAddress(address)) {
				MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(),
						person.getLastName());
				ObjectNode inhabitant = mapper.createObjectNode();
				PersonUtil.addNameToNode(inhabitant, person);
				inhabitant.put("phone", person.getPhone());
				inhabitant.put("age", MedicalRecordUtil.calculateAge(medicalRecord));
				inhabitant.set("patientHistory", MedicalRecordUtil.patientHistory(medicalRecord));

				inhabitants.add(inhabitant);
			}
			house.set("inhabitants", inhabitants);
			houses.add(house);
		}
		return houses;
	}

	public ArrayNode phoneAlert(String stationNumber) {
		ArrayNode phones = mapper.createArrayNode();

		for (String address : firestationRepository.findByStation(stationNumber)) {
			for (Person person : personRepository.findByAddress(address)) {
				phones.add(person.getPhone());
			}
		}
		return phones;
	}

	public ObjectNode firestation(String stationNumber) {
		ArrayNode persons = mapper.createArrayNode();
		long adultCount = 0;
		long childrenCount = 0;

		for (String address : firestationRepository.findByStation(stationNumber)) {
			for (Person person : personRepository.findByAddress(address)) {
				ObjectNode inhabitant = mapper.createObjectNode();
				PersonUtil.addNameToNode(inhabitant, person);
				inhabitant.put("address", address);
				inhabitant.put("phone", person.getPhone());

				persons.add(inhabitant);
				MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(),
						person.getLastName());
				if (MedicalRecordUtil.calculateAge(medicalRecord) <= 18) {
					++childrenCount;
				} else {
					++adultCount;
				}
			}
		}
		ObjectNode coveredPersons = mapper.createObjectNode();
		coveredPersons.put("adultCount", adultCount);
		coveredPersons.put("childrenCount", childrenCount);
		coveredPersons.set("persons", persons);
		return coveredPersons;
	}

	public ObjectNode fire(String address) {
		ObjectNode house = mapper.createObjectNode();
		house.put("station", firestationRepository.findByAddress(address));
		ArrayNode inhabitants = mapper.createArrayNode();

		for (Person person : personRepository.findByAddress(address)) {
			ObjectNode inhabitant = mapper.createObjectNode();
			MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(),
					person.getLastName());
			PersonUtil.addNameToNode(inhabitant, person);
			inhabitant.put("age", MedicalRecordUtil.calculateAge(medicalRecord));
			inhabitant.set("patientHistory", MedicalRecordUtil.patientHistory(medicalRecord));

			inhabitants.add(inhabitant);
		}
		house.set("inhabitants", inhabitants);
		return house;
	}
}