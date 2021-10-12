package com.safetynet.alert.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.safetynet.alert.DAO.DataCollectionJsonFileDAO;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.PersonRepository;

@SpringBootTest
class PersonRepositoryIT {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private DataCollectionJsonFileDAO dataCollectionDAO;

	@BeforeEach
	private void setUpPerTest() {
		dataCollectionDAO.updatePerson(new ArrayList<Person>());
	}

	@Test
	public void addTest() {
		Person toAdd = new Person("chuck", "norris", "behind you", "city", "00000", "phone", "email");

		personRepository.add(toAdd);

		Person test = personRepository.findByFirstNameAndLastName("chuck", "norris");
		assertThat(test.getAddress()).isEqualTo("behind you");
		assertThat(test.getCity()).isEqualTo("city");
		assertThat(test.getZip()).isEqualTo("00000");
		assertThat(test.getPhone()).isEqualTo("phone");
		assertThat(test.getEmail()).isEqualTo("email");
	}

	@Test
	public void updateTest() {
		Person toAdd = new Person("chuck", "norris", "behind you", "city", "00000", "phone", "email");
		personRepository.add(toAdd);
		Person toUpdate = new Person("chuck", "norris", "siping a cocktail", "ville", "11111", "telephone", "courriel");

		personRepository.update(toUpdate);

		Person test = personRepository.findByFirstNameAndLastName("chuck", "norris");
		assertThat(test.getAddress()).isEqualTo("siping a cocktail");
		assertThat(test.getCity()).isEqualTo("ville");
		assertThat(test.getZip()).isEqualTo("11111");
		assertThat(test.getPhone()).isEqualTo("telephone");
		assertThat(test.getEmail()).isEqualTo("courriel");
	}

	@Test
	public void deleteTest() {
		Person toAdd = new Person("chuck", "norris", "behind you", "city", "00000", "phone", "email");
		personRepository.add(toAdd);
		Person toDelete = new Person("chuck", "norris", "behind you", "city", "00000", "phone", "email");

		personRepository.delete(toDelete);

		Person test = personRepository.findByFirstNameAndLastName("chuck", "norris");

		assertThat(test).isNull();
	}
}
