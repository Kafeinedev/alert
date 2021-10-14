package com.safetynet.alert.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.FirestationRepository;
import com.safetynet.alert.repository.MedicalRecordRepository;
import com.safetynet.alert.repository.PersonRepository;

@Service
public class SearchService {

	private PersonRepository personRepository;

	private FirestationRepository firestationRepository;

	private MedicalRecordRepository medicalRecordRepository;

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	public SearchService(PersonRepository personRep, FirestationRepository firestationRep,
			MedicalRecordRepository medicalRecordRep) {
		this.personRepository = personRep;
		this.firestationRepository = firestationRep;
		this.medicalRecordRepository = medicalRecordRep;
	}

	public ObjectNode firestation(String station) {
		ArrayNode habitants = mapper.createArrayNode();
		int adultCount = 0;
		int childCount = 0;

		// Get all the address covered by the station
		List<String> addresses = firestationRepository.findByStation(station);
		for (String s : addresses) {// for each address we look for everybody living there
			List<Person> persons = personRepository.findByAddress(s);
			for (Person p : persons) {

				ObjectNode person = mapper.createObjectNode();
				person.put("firstName", p.getFirstName());
				person.put("lastName", p.getLastName());
				person.put("address", p.getAddress());
				person.put("phone", p.getPhone());

				MedicalRecord mr = medicalRecordRepository.findByFirstNameAndLastName(p.getFirstName(),
						p.getLastName());
				if (mr != null) {// check that medical record is actually present while this should not throw an
									// error if absent this may need to be logged
					if (calculateAge(mr) >= 18) {
						++adultCount;
					} else {
						++childCount;
					}
				}
				habitants.add(person);// we add the person to the array node.
			}
		}

		ObjectNode ret = mapper.createObjectNode();// we return the array of person + the two counts
		ret.set("habitants", habitants);
		ret.put("adultCount", adultCount);
		ret.put("childCount", childCount);
		return ret;
	}

	public ObjectNode childAlert(String address) {
		ArrayNode childrens = mapper.createArrayNode();
		ArrayNode adults = mapper.createArrayNode();
		List<Person> persons = personRepository.findByAddress(address);

		for (Person p : persons) {
			ObjectNode person = mapper.createObjectNode();
			MedicalRecord mr = medicalRecordRepository.findByFirstNameAndLastName(p.getFirstName(), p.getLastName());
			long age = calculateAge(mr);

			person.put("firstName", p.getFirstName());
			person.put("lastName", p.getLastName());
			if (age < 18) {
				person.put("age", age);
				childrens.add(person);
			} else {
				adults.add(person);
			}
		}

		ObjectNode ret = mapper.createObjectNode();
		ret.set("childrens", childrens);
		ret.set("adults", adults);
		return ret;
	}

	public ArrayNode phoneAlert(String station) {
		ArrayNode ret = mapper.createArrayNode();
		List<String> addresses = firestationRepository.findByStation(station);
		for (String s : addresses) {
			List<Person> persons = personRepository.findByAddress(s);
			for (Person p : persons) {
				ret.add(p.getPhone());
			}
		}
		return ret;
	}

	public ObjectNode fire(String address) {
		ObjectNode ret = mapper.createObjectNode();
		ArrayNode habitants = mapper.createArrayNode();
		List<Person> persons = personRepository.findByAddress(address);

		for (Person p : persons) {
			habitants.add(personInfoForFireAndStation(p));
		}

		ret.set("habitants", habitants);
		ret.put("firestation", firestationRepository.findByAddress(address));
		return ret;
	}

	public ObjectNode station(String station) {
		ArrayNode houses = mapper.createArrayNode();
		List<String> addresses = firestationRepository.findByStation(station);

		for (String address : addresses) {
			ObjectNode house = mapper.createObjectNode();
			ArrayNode habitants = mapper.createArrayNode();
			List<Person> persons = personRepository.findByAddress(address);

			house.put("address", address);
			for (Person p : persons) {
				habitants.add(personInfoForFireAndStation(p));
			}
			house.set("habitants", habitants);
			houses.add(house);
		}
		ObjectNode ret = mapper.createObjectNode();
		ret.set("houses", houses);
		return ret;
	}

	public ObjectNode personInfo(String firstName, String lastName) {
		Person p = personRepository.findByFirstNameAndLastName(firstName, lastName);
		MedicalRecord mr = medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName);
		ObjectNode ret = mapper.createObjectNode();

		ret.put("firstName", p.getFirstName());
		ret.put("lastName", p.getLastName());
		ret.put("address", p.getAddress());
		ret.put("age", calculateAge(mr));
		ret.put("mail", p.getEmail());
		ArrayNode medications = mapper.createArrayNode();
		for (String s : mr.getMedications()) {
			medications.add(s);
		}
		ArrayNode allergies = mapper.createArrayNode();
		for (String s : mr.getAllergies()) {
			allergies.add(s);
		}

		ret.set("medications", medications);
		ret.set("allergies", allergies);

		return ret;
	}

	public ArrayNode communityEmail(String city) {
		ArrayNode ret = mapper.createArrayNode();
		List<Person> persons = personRepository.findByCity(city);
		for (Person p : persons) {
			ret.add(p.getEmail());
		}
		return ret;
	}

	private long calculateAge(MedicalRecord mr) {
		LocalDate current = LocalDate.now();
		LocalDate birthdate = LocalDate.parse(mr.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));

		return ChronoUnit.YEARS.between(birthdate, current);
	}

	// Return an objectNode containing the person full name + phone + age +
	// medications and allergies
	private ObjectNode personInfoForFireAndStation(Person p) {
		ObjectNode person = mapper.createObjectNode();
		person.put("firstName", p.getFirstName());
		person.put("lastName", p.getLastName());
		person.put("phone", p.getPhone());
		MedicalRecord mr = medicalRecordRepository.findByFirstNameAndLastName(p.getFirstName(), p.getLastName());
		person.put("age", calculateAge(mr));
		ArrayNode medications = mapper.createArrayNode();
		ArrayNode allergies = mapper.createArrayNode();

		for (String s : mr.getMedications()) {
			medications.add(s);
		}
		for (String s : mr.getAllergies()) {
			allergies.add(s);
		}

		person.set("medications", medications);
		person.set("allergies", allergies);

		return person;
	}
}
