package com.safetynet.alert.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safetynet.alert.DAO.DataCollectionJsonFileDAO;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.Person;

@Repository
public class PersonRepository {

	private static Logger log = LogManager.getLogger("PersonRepository");

	private DataCollectionJsonFileDAO dataCollectionDAO;

	@Autowired
	public PersonRepository(DataCollectionJsonFileDAO dataCollectionDAO) {
		this.dataCollectionDAO = dataCollectionDAO;
	}

	public List<Person> getAll() {
		DataCollection dataCollection = dataCollectionDAO.getAll();

		return dataCollection != null && dataCollection.getPersons() != null ? dataCollection.getPersons()
				: new ArrayList<Person>();
	}

	public void add(Person person) throws FileAccessException, EntityAlreadyPresentException {
		List<Person> persons = getAll();

		if (findIndex(person, persons) != -1) {
			log.error("Error trying to add already present person");
			throw new EntityAlreadyPresentException();
		}

		persons.add(person);
		DataCollection addedPerson = new DataCollection();
		addedPerson.setPersons(persons);
		dataCollectionDAO.update(addedPerson);
	}

	public void update(Person person) throws FileAccessException, EntityMissingException {
		List<Person> persons = getAll();
		int index = findIndex(person, persons);

		if (index < 0) {
			log.error("Error trying to update missing person");
			throw new EntityMissingException();
		}

		persons.set(index, person);
		DataCollection updatedPerson = new DataCollection();
		updatedPerson.setPersons(persons);
		dataCollectionDAO.update(updatedPerson);
	}

	public void delete(Person person) throws FileAccessException, EntityMissingException {
		List<Person> persons = getAll();
		int index = findIndex(person, persons);

		if (index < 0) {
			log.error("Error trying to delete missing person");
			throw new EntityMissingException();
		}

		persons.remove(index);
		DataCollection deletedPerson = new DataCollection();
		deletedPerson.setPersons(persons);
		dataCollectionDAO.update(deletedPerson);
	}

	public Person findByFirstNameAndLastName(String firstName, String lastName) {
		List<Person> persons = getAll();

		int index = findIndex(new Person(firstName, lastName, null, null, null, null, null), persons);
		if (index >= 0) {
			return persons.get(index);
		}
		return null;
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
