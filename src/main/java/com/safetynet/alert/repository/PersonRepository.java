package com.safetynet.alert.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safetynet.alert.DAO.JsonFileReaderWriter;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.Person;

@Repository
public class PersonRepository {

	private static Logger log = LogManager.getLogger("PersonRepository");

	private JsonFileReaderWriter dataCollectionDAO;

	@Autowired
	public PersonRepository(JsonFileReaderWriter dataCollectionDAO) {
		this.dataCollectionDAO = dataCollectionDAO;
	}

	public List<Person> getAllPersons() {
		DataCollection dataCollection = dataCollectionDAO.getDataCollection();

		return dataCollection.getPersons() != null ? dataCollection.getPersons() : new ArrayList<Person>();
	}

	public void add(Person person) throws FileAccessException, EntityAlreadyPresentException {
		List<Person> persons = getAllPersons();

		if (findIndex(person, persons) != -1) {
			log.error("Error trying to add already present person");
			throw new EntityAlreadyPresentException();
		}

		persons.add(person);
		dataCollectionDAO.updatePerson(persons);
	}

	public void update(Person person) throws FileAccessException, EntityMissingException {
		List<Person> persons = getAllPersons();
		int index = findIndex(person, persons);

		if (index < 0) {
			log.error("Error trying to update missing person");
			throw new EntityMissingException();
		}

		persons.set(index, person);
		dataCollectionDAO.updatePerson(persons);
	}

	public void delete(Person person) throws FileAccessException, EntityMissingException {
		List<Person> persons = getAllPersons();
		int index = findIndex(person, persons);

		if (index < 0) {
			log.error("Error trying to delete missing person");
			throw new EntityMissingException();
		}

		persons.remove(index);
		dataCollectionDAO.updatePerson(persons);
	}

	public Person findByFirstNameAndLastName(String firstName, String lastName) throws FileAccessException {
		List<Person> persons = getAllPersons();

		int index = findIndex(new Person(firstName, lastName, null, null, null, null, null), persons);
		if (index >= 0) {
			return persons.get(index);
		}
		return null;
	}

	public List<Person> findByAddress(String address) throws FileAccessException {
		List<Person> persons = getAllPersons();
		List<Person> personsFound = new ArrayList<Person>();

		for (Person person : persons) {
			if (person.getAddress().equals(address)) {
				personsFound.add(person);
			}
		}

		return personsFound;
	}

	public List<Person> findByCity(String city) throws FileAccessException {
		List<Person> persons = getAllPersons();
		List<Person> personsFound = new ArrayList<Person>();

		for (Person person : persons) {
			if (person.getCity().equals(city)) {
				personsFound.add(person);
			}
		}

		return personsFound;
	}

	public List<Person> findByLastName(String lastName) throws FileAccessException {
		List<Person> persons = getAllPersons();
		List<Person> personsFound = new ArrayList<Person>();

		for (Person person : persons) {
			if (person.getLastName().equals(lastName)) {
				personsFound.add(person);
			}
		}
		return personsFound;
	}

	private int findIndex(Person person, List<Person> persons) {
		for (int i = 0; i < persons.size(); i++) {
			if (person.getFirstName().equals(persons.get(i).getFirstName())
					&& person.getLastName().equals(persons.get(i).getLastName())) {
				return i;
			}
		}
		return -1;
	}

}
