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

import com.safetynet.alert.DAO.JsonFileReaderWriter;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.PersonRepository;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;

@ExtendWith(MockitoExtension.class)
class PersonRepositoryTest {

	@Mock
	private JsonFileReaderWriter mockJsonFileIO;

	private PersonRepository personRepository;

	private DataCollection dataCollection;

	@BeforeEach
	public void setUpPerTest() {
		dataCollection = new DataCollection();
		Person person = new Person("jessuis", "groot", "this", "is", "a", "test", "!");
		dataCollection.setPersons(new ArrayList<Person>());
		dataCollection.getPersons().add(person);
		when(mockJsonFileIO.getDataCollection()).thenReturn(dataCollection);
		personRepository = new PersonRepository(mockJsonFileIO);
	}

	@Test
	public void getAll_whenWorkingProperly_returnListOfPersons() {
		List<Person> toTest = personRepository.getAllPersons();

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
		when(mockJsonFileIO.getDataCollection()).thenReturn(new DataCollection());

		List<Person> toTest = personRepository.getAllPersons();

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
	public void findByFirstNameAndLastName_whenEntityMissing_returnNull() {
		Person toTest = personRepository.findByFirstNameAndLastName("jenexiste", "poh");

		assertThat(toTest).isNull();
	}

	// This test may fail if the way to return data/search is changed /!\
	@Test
	public void findByAddress_whenWorkingProperly_returnListOfPerson() {
		dataCollection.getPersons().add(new Person("sge", "seg", "nulle part", "sg", "drg", "es", "f"));
		dataCollection.getPersons().add(new Person("vivele", "vendredi", "this", "another", "place", "to", "assert"));

		List<Person> test = personRepository.findByAddress("this");

		assertThat(test.size()).isEqualTo(2);
		assertThat(test.get(0).getFirstName()).isEqualTo("jessuis");
		assertThat(test.get(1).getFirstName()).isEqualTo("vivele");
	}

	@Test
	public void findByAddress_whenAddressMissing_returnEmptyList() {
		List<Person> test = personRepository.findByAddress("nawak");

		assertThat(test.size()).isEqualTo(0);
	}

	// This test may fail if the way to return data/search is changed /!\
	@Test
	public void findByCity_whenWorkingProperly_returnListOfPerson() {
		dataCollection.getPersons().add(new Person("sge", "seg", "nulle part", "ville", "drg", "es", "f"));
		dataCollection.getPersons().add(new Person("vivele", "vendredi", "this", "ville", "place", "to", "assert"));

		List<Person> test = personRepository.findByCity("ville");

		assertThat(test.size()).isEqualTo(2);
		assertThat(test.get(0).getFirstName()).isEqualTo("sge");
		assertThat(test.get(1).getFirstName()).isEqualTo("vivele");
	}

	@Test
	public void findByCity_whenCityMissing_returnEmptyList() {
		List<Person> test = personRepository.findByCity("nawak");

		assertThat(test.size()).isEqualTo(0);
	}

	@Test
	public void findByName_whenWorkingProperly_returnListOfPerson() {
		dataCollection.getPersons().add(new Person("sge", "groot", "nulle part", "ville", "drg", "es", "f"));
		dataCollection.getPersons().add(new Person("vivele", "vendredi", "this", "ville", "place", "to", "assert"));

		List<Person> test = personRepository.findByLastName("groot");

		assertThat(test.size()).isEqualTo(2);
		assertThat(test.get(0).getFirstName()).isEqualTo("jessuis");
		assertThat(test.get(1).getFirstName()).isEqualTo("sge");

	}

	@Test
	public void findByName_whenNobodyWithThatName_returnEmptyList() {
		List<Person> test = personRepository.findByLastName("nawak");

		assertThat(test.size()).isEqualTo(0);
	}

}
