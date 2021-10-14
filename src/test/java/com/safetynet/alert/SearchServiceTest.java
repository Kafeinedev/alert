package com.safetynet.alert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.FirestationRepository;
import com.safetynet.alert.repository.MedicalRecordRepository;
import com.safetynet.alert.repository.PersonRepository;
import com.safetynet.alert.service.SearchService;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

	@Mock
	private PersonRepository mockPersonRep;

	@Mock
	private FirestationRepository mockFirestationRep;

	@Mock
	private MedicalRecordRepository mockMedicalRecordRep;

	private SearchService service;

	@BeforeEach
	private void setUp() throws Exception {
		service = new SearchService(mockPersonRep, mockFirestationRep, mockMedicalRecordRep);
	}

	@Test
	public void firestation_whenWorkingProperly_returnCorrectJsonObject() {
		when(mockFirestationRep.findByStation("1")).thenReturn(List.of("this is an address"));
		when(mockPersonRep.findByAddress("this is an address")).thenReturn(List.of(
				new Person("Pierre", "Paul", "this is an address", "gotham", "25500", "555-555-5555",
						"batman@mail.com"),
				new Person("Jean", "Paul", "this is an address", "gotham", "25500", "555-555-5559", "robin@mail.com")));
		when(mockMedicalRecordRep.findByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(
				new MedicalRecord("Pierre", "Paul", "01/01/1900", List.of(), List.of()),
				new MedicalRecord("Jean", "Paul", "01/01/2021", List.of(), List.of()));

		ObjectNode test = service.firestation("1");
		JsonNode testHabitants = test.get("habitants");
		JsonNode habitantOne = testHabitants.get(0);
		JsonNode habitantTwo = testHabitants.get(1);

		assertThat(habitantOne.get("firstName").textValue()).isEqualTo("Pierre");
		assertThat(habitantOne.get("lastName").textValue()).isEqualTo("Paul");
		assertThat(habitantOne.get("address").textValue()).isEqualTo("this is an address");
		assertThat(habitantOne.get("phone").textValue()).isEqualTo("555-555-5555");
		assertThat(habitantTwo.get("firstName").textValue()).isEqualTo("Jean");
		assertThat(habitantTwo.get("lastName").textValue()).isEqualTo("Paul");
		assertThat(habitantTwo.get("address").textValue()).isEqualTo("this is an address");
		assertThat(habitantTwo.get("phone").textValue()).isEqualTo("555-555-5559");
		assertThat(test.get("adultCount").asInt()).isEqualTo(1);
		assertThat(test.get("childCount").asInt()).isEqualTo(1);
	}

	// this test will fail in the future since it use system time /!\
	@Test
	public void childAlert_whenWorkingProperly_returnCorrectJsonObject() {
		when(mockPersonRep.findByAddress("a dress")).thenReturn(
				List.of(new Person("Pierre", "Paul", "a dress", "gotham", "25500", "555-555-5555", "batman@mail.com"),
						new Person("Jean", "Paul", "a dress", "gotham", "25500", "555-555-5559", "robin@mail.com")));
		when(mockMedicalRecordRep.findByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(
				new MedicalRecord("Pierre", "Paul", "01/01/1900", List.of(), List.of()),
				new MedicalRecord("Jean", "Paul", "12/01/2019", List.of(), List.of()));

		ObjectNode test = service.childAlert("a dress");
		JsonNode childrens = test.get("childrens");
		JsonNode adults = test.get("adults");

		assertThat(childrens.get(0).get("firstName").textValue()).isEqualTo("Jean");
		assertThat(childrens.get(0).get("lastName").textValue()).isEqualTo("Paul");
		assertThat(childrens.get(0).get("age").asInt()).isEqualTo(1);
		assertThat(adults.get(0).get("firstName").textValue()).isEqualTo("Pierre");
		assertThat(adults.get(0).get("lastName").textValue()).isEqualTo("Paul");
	}

	@Test
	public void phoneAlert_whenWorkingProperly_returnCorrectJsonObject() {
		when(mockFirestationRep.findByStation("1")).thenReturn(List.of("this is an address"));
		when(mockPersonRep.findByAddress("this is an address")).thenReturn(List.of(
				new Person("Pierre", "Paul", "this is an address", "gotham", "25500", "555-555-5555",
						"batman@mail.com"),
				new Person("Jean", "Paul", "this is an address", "gotham", "25500", "555-555-5559", "robin@mail.com")));

		ArrayNode test = service.phoneAlert("1");
		assertThat(test.get(0).asText()).isEqualTo("555-555-5555");
		assertThat(test.get(1).asText()).isEqualTo("555-555-5559");
	}

	@Test
	public void fire_whenWorkingProperly_returnCorrectJsonObject() {
		when(mockPersonRep.findByAddress("a dress")).thenReturn(
				List.of(new Person("Pierre", "Paul", "a dress", "gotham", "25500", "555-555-5555", "batman@mail.com"),
						new Person("Jean", "Paul", "a dress", "gotham", "25500", "555-555-5559", "robin@mail.com")));
		when(mockMedicalRecordRep.findByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(
				new MedicalRecord("Pierre", "Paul", "01/01/1900", List.of("G mal", "Oh piet"),
						List.of("ouille ouille")),
				new MedicalRecord("Jean", "Paul", "12/01/2019", List.of("Il était un"),
						List.of("petit navire", "jamais navigué")));
		when(mockFirestationRep.findByAddress("a dress")).thenReturn("1");

		ObjectNode test = service.fire("a dress");

		assertThat(test.get("habitants").get(0).get("firstName").asText()).isEqualTo("Pierre");
		assertThat(test.get("habitants").get(0).get("lastName").asText()).isEqualTo("Paul");
		assertThat(test.get("habitants").get(0).get("phone").asText()).isEqualTo("555-555-5555");
		assertThat(test.get("habitants").get(0).get("age").asInt()).isEqualTo(121);
		assertThat(test.get("habitants").get(0).get("medications").get(0).asText()).isEqualTo("G mal");
		assertThat(test.get("habitants").get(0).get("medications").get(1).asText()).isEqualTo("Oh piet");
		assertThat(test.get("habitants").get(0).get("allergies").get(0).asText()).isEqualTo("ouille ouille");
		assertThat(test.get("habitants").get(1).get("firstName").asText()).isEqualTo("Jean");
		assertThat(test.get("habitants").get(1).get("lastName").asText()).isEqualTo("Paul");
		assertThat(test.get("habitants").get(1).get("phone").asText()).isEqualTo("555-555-5559");
		assertThat(test.get("habitants").get(1).get("age").asInt()).isEqualTo(1);
		assertThat(test.get("habitants").get(1).get("medications").get(0).asText()).isEqualTo("Il était un");
		assertThat(test.get("habitants").get(1).get("allergies").get(0).asText()).isEqualTo("petit navire");
		assertThat(test.get("habitants").get(1).get("allergies").get(1).asText()).isEqualTo("jamais navigué");
		assertThat(test.get("firestation").asText()).isEqualTo("1");
	}

	@Test
	public void station_whenWorkingProperly_returnCorrectJsonObject() {
		when(mockFirestationRep.findByStation("1")).thenReturn(List.of("a dress"));
		when(mockPersonRep.findByAddress("a dress")).thenReturn(
				List.of(new Person("Pierre", "Paul", "a dress", "gotham", "25500", "555-555-5555", "batman@mail.com"),
						new Person("Jean", "Paul", "a dress", "gotham", "25500", "555-555-5559", "robin@mail.com")));
		when(mockMedicalRecordRep.findByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(
				new MedicalRecord("Pierre", "Paul", "01/01/1900", List.of("G mal", "Oh piet"),
						List.of("ouille ouille")),
				new MedicalRecord("Jean", "Paul", "12/01/2019", List.of("Il était un"),
						List.of("petit navire", "jamais navigué")));

		ObjectNode test = service.station("1");
		JsonNode house = test.get("houses").get(0);

		assertThat(house.get("address").asText()).isEqualTo("a dress");
		assertThat(house.get("habitants").get(0).get("firstName").asText()).isEqualTo("Pierre");
		assertThat(house.get("habitants").get(0).get("lastName").asText()).isEqualTo("Paul");
		assertThat(house.get("habitants").get(0).get("phone").asText()).isEqualTo("555-555-5555");
		assertThat(house.get("habitants").get(0).get("age").asInt()).isEqualTo(121);
		assertThat(house.get("habitants").get(0).get("medications").get(0).asText()).isEqualTo("G mal");
		assertThat(house.get("habitants").get(0).get("medications").get(1).asText()).isEqualTo("Oh piet");
		assertThat(house.get("habitants").get(0).get("allergies").get(0).asText()).isEqualTo("ouille ouille");
		assertThat(house.get("habitants").get(1).get("firstName").asText()).isEqualTo("Jean");
		assertThat(house.get("habitants").get(1).get("lastName").asText()).isEqualTo("Paul");
		assertThat(house.get("habitants").get(1).get("phone").asText()).isEqualTo("555-555-5559");
		assertThat(house.get("habitants").get(1).get("age").asInt()).isEqualTo(1);
		assertThat(house.get("habitants").get(1).get("medications").get(0).asText()).isEqualTo("Il était un");
		assertThat(house.get("habitants").get(1).get("allergies").get(0).asText()).isEqualTo("petit navire");
		assertThat(house.get("habitants").get(1).get("allergies").get(1).asText()).isEqualTo("jamais navigué");
	}

	@Test
	public void personInfo_whenWorkingProperly_returnCorrectJsonObject() {
		when(mockPersonRep.findByFirstNameAndLastName("Pierre", "Paul")).thenReturn(
				new Person("Pierre", "Paul", "a dress", "gotham", "25500", "555-555-5555", "batman@mail.com"));
		when(mockMedicalRecordRep.findByFirstNameAndLastName("Pierre", "Paul")).thenReturn(new MedicalRecord("Pierre",
				"Paul", "01/01/1900", List.of("G mal", "Oh piet"), List.of("ouille ouille")));

		ObjectNode test = service.personInfo("Pierre", "Paul");

		assertThat(test.get("firstName").asText()).isEqualTo("Pierre");
		assertThat(test.get("lastName").asText()).isEqualTo("Paul");
		assertThat(test.get("address").asText()).isEqualTo("a dress");
		assertThat(test.get("age").asInt()).isEqualTo(121);
		assertThat(test.get("mail").asText()).isEqualTo("batman@mail.com");
		assertThat(test.get("medications").get(0).asText()).isEqualTo("G mal");
		assertThat(test.get("medications").get(1).asText()).isEqualTo("Oh piet");
		assertThat(test.get("allergies").get(0).asText()).isEqualTo("ouille ouille");
	}

	@Test
	public void communityEmail_whenWorkingProperly_returnCorrectJsonObject() {
		when(mockPersonRep.findByCity("gotham")).thenReturn(
				List.of(new Person("Pierre", "Paul", "a dress", "gotham", "25500", "555-555-5555", "batman@mail.com"),
						new Person("Jean", "Paul", "a dress", "gotham", "25500", "555-555-5559", "robin@mail.com")));

		ArrayNode test = service.communityEmail("gotham");

		assertThat(test.get(0).asText()).isEqualTo("batman@mail.com");
		assertThat(test.get(1).asText()).isEqualTo("robin@mail.com");
	}
}
