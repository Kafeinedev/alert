package com.safetynet.alert.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.MedicalRecordRepository;
import com.safetynet.alert.repository.PersonRepository;
import com.safetynet.alert.util.MedicalRecordUtil;
import com.safetynet.alert.util.PersonUtil;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Autowired
	private ObjectMapper mapper;

	public void postPerson(Person person) {
		personRepository.add(person);
	}

	public void putPerson(Person person) {
		personRepository.update(person);
	}

	public void deletePerson(Person person) {
		personRepository.delete(person);
	}

	public ArrayNode communityEmail(String city) {
		List<Person> persons = personRepository.findByCity(city);
		ArrayNode emails = mapper.createArrayNode();

		for (Person person : persons) {
			emails.add(person.getEmail());
		}
		return emails;
	}

	public ArrayNode personInfo(String lastName) {
		ArrayNode personsInformation = mapper.createArrayNode();
		List<Person> persons = personRepository.findByLastName(lastName);

		for (Person person : persons) {
			MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(),
					person.getLastName());
			ObjectNode personInfo = mapper.createObjectNode();
			PersonUtil.addNameToNode(personInfo, person);
			personInfo.put("address", person.getAddress());
			personInfo.put("age", MedicalRecordUtil.calculateAge(medicalRecord));
			personInfo.put("email", person.getEmail());
			personInfo.set("patientHistory", MedicalRecordUtil.patientHistory(medicalRecord));

			personsInformation.add(personInfo);
		}

		return personsInformation;
	}

	public ObjectNode childAlert(String address) {
		ArrayNode adults = mapper.createArrayNode();
		ArrayNode childrens = mapper.createArrayNode();

		for (Person person : personRepository.findByAddress(address)) {
			MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(),
					person.getLastName());
			long age = MedicalRecordUtil.calculateAge(medicalRecord);
			ObjectNode inhabitant = mapper.createObjectNode();
			PersonUtil.addNameToNode(inhabitant, person);

			if (age <= 18) {
				inhabitant.put("age", age);
				childrens.add(inhabitant);
			} else {
				adults.add(inhabitant);
			}
		}
		ObjectNode house = mapper.createObjectNode();
		house.set("childrens", childrens);
		house.set("adults", adults);
		return house;
	}
}
