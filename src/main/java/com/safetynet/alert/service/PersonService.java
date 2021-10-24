package com.safetynet.alert.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
import com.safetynet.alert.exception.FileAccessException;
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

	public void postPerson(Person person) throws FileAccessException, EntityAlreadyPresentException {
		personRepository.add(person);
	}

	public void putPerson(Person person) throws FileAccessException, EntityMissingException {
		personRepository.update(person);
	}

	public void deletePerson(Person person) throws FileAccessException, EntityMissingException {
		personRepository.delete(person);
	}

	public ArrayNode getCommunityEmail(String city) throws FileAccessException {
		List<Person> persons = personRepository.findByCity(city);
		ArrayNode emails = mapper.createArrayNode();

		for (Person person : persons) {
			emails.add(person.getEmail());
		}
		return emails;
	}

	public ArrayNode getPersonInfo(String firstName, String lastName) throws FileAccessException {
		ArrayNode personsInformation = mapper.createArrayNode();
		List<Person> persons = personRepository.findByLastName(lastName);

		for (Person person : persons) {
			MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(),
					person.getLastName());
			ObjectNode personInfo = mapper.createObjectNode();
			PersonUtil personUtil = new PersonUtil();
			MedicalRecordUtil medicalRecordUtil = new MedicalRecordUtil();
			personUtil.addNameToNode(personInfo, person);
			personInfo.put("address", person.getAddress());
			personInfo.put("age", medicalRecordUtil.calculateAge(medicalRecord));
			personInfo.put("email", person.getEmail());
			personInfo.set("patientHistory", medicalRecordUtil.patientHistory(medicalRecord));

			if (person.getFirstName().equals(firstName)) {
				personsInformation.insert(-1, personInfo);// Javadoc comment error insert(0,) do NOT insert the data in
															// front -1 work as advertised
			} else {
				personsInformation.add(personInfo);
			}
		}

		return personsInformation;
	}

	public ObjectNode getChildAlert(String address) throws FileAccessException {
		ArrayNode adults = mapper.createArrayNode();
		ArrayNode childrens = mapper.createArrayNode();

		for (Person person : personRepository.findByAddress(address)) {
			MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(),
					person.getLastName());
			MedicalRecordUtil medicalRecordUtil = new MedicalRecordUtil();
			PersonUtil personUtil = new PersonUtil();
			long age = medicalRecordUtil.calculateAge(medicalRecord);
			ObjectNode inhabitant = mapper.createObjectNode();
			personUtil.addNameToNode(inhabitant, person);

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
