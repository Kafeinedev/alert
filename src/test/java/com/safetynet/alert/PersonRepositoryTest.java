package com.safetynet.alert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alert.DAO.DataCollectionJsonFileDAO;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.PersonRepository;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;

@ExtendWith(MockitoExtension.class)
class PersonRepositoryTest {

	@Mock
	private DataCollectionJsonFileDAO mockDataCollectionDAO;

	private PersonRepository personRepository;

	private DataCollection dataCollection;

	@BeforeEach
	public void setUpPerTest() {
		dataCollection = new DataCollection();
		Person person = new Person("jessuis", "groot", "this", "is", "a", "test", "!");
		dataCollection.setPersons(new ArrayList<Person>());
		dataCollection.getPersons().add(person);
		when(mockDataCollectionDAO.getAll()).thenReturn(dataCollection);
		personRepository = new PersonRepository(mockDataCollectionDAO);
	}

	@Test
	public void getAll_whenWorkingProperly_returnListOfPersons() {
		List<Person> toTest = personRepository.getAll();

		assertThat(toTest.size()).isEqualTo(1);
		assertThat(toTest.get(0).getFirstName()).isEqualTo("jessuis");
		assertThat(toTest.get(0).getLastName()).isEqualTo("groot");
		assertThat(toTest.get(0).getAddress()).isEqualTo("this");
		assertThat(toTest.get(0).getCity()).isEqualTo("is");
		assertThat(toTest.get(0).getZip()).isEqualTo("a");
		assertThat(toTest.get(0).getPhone()).isEqualTo("test");
		assertThat(toTest.get(0).getEmail()).isEqualTo("!");
	}

	@Test
	public void getAll_whenDatabaseEmpty_returnEmptyList() {
		when(mockDataCollectionDAO.getAll()).thenReturn(null);

		List<Person> toTest = personRepository.getAll();

		assertThat(toTest.size()).isEqualTo(0);
	}

	@Test
	public void add_whenTryingToAddPersonThatAlreadyExist_throwEntityAlreadyPresentException() {
		Person johnDoe = new Person("jessuis", "groot", "this", "is", "a", "test", "!");

		assertThrows(EntityAlreadyPresentException.class, () -> {
			personRepository.add(johnDoe);
		});
	}

	@Test
	public void update_whenTryingToUpdateMissingPerson_throwEntityMissingException() {
		Person johnDoe = new Person("john", "doe", "this", "is", "a", "true", "test");

		assertThrows(EntityMissingException.class, () -> {
			personRepository.update(johnDoe);
		});
	}

	@Test
	public void delete_whenTryingToDeleteMissingPerson_throwEntityMissingException() {
		Person johnDoe = new Person("john", "doe", "this", "is", "a", "true", "test");

		assertThrows(EntityMissingException.class, () -> {
			personRepository.delete(johnDoe);
		});
	}

	@Test
	public void findByFirstNameAndLastName_whenWorkingProperly_returnCorrectPerson() {
		Person toTest = personRepository.findByFirstNameAndLastName("jessuis", "groot");

		assertThat(toTest.getFirstName()).isEqualTo("jessuis");
		assertThat(toTest.getLastName()).isEqualTo("groot");
		assertThat(toTest.getAddress()).isEqualTo("this");
		assertThat(toTest.getCity()).isEqualTo("is");
		assertThat(toTest.getZip()).isEqualTo("a");
		assertThat(toTest.getPhone()).isEqualTo("test");
		assertThat(toTest.getEmail()).isEqualTo("!");
	}

	@Test
	public void findByFirstNameAndLastName_whenWorkingIncorrectly_returnNull() {
		Person toTest = personRepository.findByFirstNameAndLastName("jenexiste", "poh");

		assertThat(toTest).isNull();
	}

}
