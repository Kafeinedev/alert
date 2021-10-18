package com.safetynet.alert.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.safetynet.alert.DAO.JsonFileReaderWriter;
import com.safetynet.alert.controller.FirestationController;
import com.safetynet.alert.controller.PersonController;
import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.model.Person;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class MultiControllerIT {

	@Autowired
	private FirestationController firestationController;

	@Autowired
	private PersonController personController;

	@Autowired
	private JsonFileReaderWriter JsonFileIO;

	private MockMvc mockMvc;

	@BeforeEach
	private void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(firestationController, personController).build();
	}

	@BeforeAll
	private void setUpBeforeClass() {
		List<Person> persons = new ArrayList<>();
		List<Firestation> firestations = new ArrayList<>();
		List<MedicalRecord> medicalRecords = new ArrayList<>();

		persons.add(new Person("chuck", "norris", "behind you", "city", "00000", "000-000-0000", "chuck@norris.com"));
		persons.add(new Person("super", "norris", "behind you", "city", "00000", "111-111-1111", "super@norris.com"));
		persons.add(
				new Person("other", "norris", "somewhere else", "city", "00000", "222-222-2222", "other@norris.com"));
		persons.add(new Person("paysan", "ducoin", "dans lcoin", "city", "00000", "999-999-9999", "paysan@ducoin.com"));

		firestations.add(new Firestation("behind you", "1"));
		firestations.add(new Firestation("somewhere else", "2"));
		firestations.add(new Firestation("dans lcoin", "1"));

		medicalRecords.add(new MedicalRecord("chuck", "norris", "03/10/1940", List.of(), List.of()));
		medicalRecords.add(new MedicalRecord("super", "norris", "05/01/2015", List.of("gummy bears"),
				List.of("vegetables", "work")));
		medicalRecords.add(new MedicalRecord("other", "norris", "12/25/2000", List.of("medication one, medication two"),
				List.of("bullshit")));
		medicalRecords
				.add(new MedicalRecord("paysan", "ducoin", "11/11/1918", List.of("pinard", "travail"), List.of("eau")));

		JsonFileIO.updatePerson(persons);
		JsonFileIO.updateFirestation(firestations);
		JsonFileIO.updateMedicalRecord(medicalRecords);
	}

	@Test
	public void communityEmailTest() throws Exception {
		mockMvc.perform(get("/communityEmail?city=city")).andExpect(status().isOk()).andExpect(content()
				.string("[\"chuck@norris.com\",\"super@norris.com\",\"other@norris.com\",\"paysan@ducoin.com\"]"));
	}

	@Test
	public void phoneAlertTest() throws Exception {
		mockMvc.perform(get("/phoneAlert?firestation=1")).andExpect(status().isOk())
				.andExpect(content().string("[\"000-000-0000\",\"111-111-1111\",\"999-999-9999\"]"));
	}

	@Test
	public void personInfoTest() throws Exception {
		mockMvc.perform(get("/personInfo?firstName=Jeserreari1&lastName=norris")).andExpect(status().isOk()).andExpect(
				content().string("[{\"firstName\":\"chuck\",\"lastName\":\"norris\",\"address\":\"behind you\","
						+ "\"age\":81,\"email\":\"chuck@norris.com\",\"patientHistory\":"
						+ "{\"medications\":[],\"allergies\":[]}},{\"firstName\":\"super\","
						+ "\"lastName\":\"norris\",\"address\":\"behind you\",\"age\":6,\"email\""
						+ ":\"super@norris.com\",\"patientHistory\":{\"medications\":[\"gummy bears\"]"
						+ ",\"allergies\":[\"vegetables\",\"work\"]}},{\"firstName\":\"other\",\"lastName\""
						+ ":\"norris\",\"address\":\"somewhere else\",\"age\":20,\"email\":\"other@norris.com\""
						+ ",\"patientHistory\":{\"medications\":[\"medication one, medication two\"],\"allergies\":"
						+ "[\"bullshit\"]}}]"));
	}

	@Test
	public void floodStationTest() throws Exception {
		mockMvc.perform(get("/flood/stations?stations=1,2")).andExpect(status().isOk()).andExpect(
				content().string("[{\"address\":\"behind you\",\"inhabitants\":[{\"firstName\":\"chuck\",\"lastName\":"
						+ "\"norris\",\"phone\":\"000-000-0000\",\"age\":81,\"patientHistory\":{\"medications\""
						+ ":[],\"allergies\":[]}},{\"firstName\":\"super\",\"lastName\":\"norris\",\"phone\":"
						+ "\"111-111-1111\",\"age\":6,\"patientHistory\":{\"medications\":[\"gummy bears\"],\"allergies\""
						+ ":[\"vegetables\",\"work\"]}}]},{\"address\":\"dans lcoin\",\"inhabitants\":"
						+ "[{\"firstName\":\"paysan\",\"lastName\":\"ducoin\",\"phone\":\"999-999-9999\",\"age\":"
						+ "102,\"patientHistory\":{\"medications\":[\"pinard\",\"travail\"],\"allergies\":[\"eau\"]}}]},"
						+ "{\"address\":\"somewhere else\",\"inhabitants\":[{\"firstName\":\"other\",\"lastName\":"
						+ "\"norris\",\"phone\":\"222-222-2222\",\"age\":20,\"patientHistory\":{\"medications\":"
						+ "[\"medication one, medication two\"],\"allergies\":[\"bullshit\"]}}]}]"));
	}

	@Test
	public void fireTest() throws Exception {
		mockMvc.perform(get("/fire?address=behind you")).andExpect(status().isOk())
				.andExpect(content().string("{\"station\":\"1\",\"inhabitants\":[{\"firstName\":\"chuck\",\"lastName\":"
						+ "\"norris\",\"phone\":\"000-000-0000\",\"age\":81,\"patientHistory\":"
						+ "{\"medications\":[],\"allergies\":[]}},{\"firstName\":\"super\",\"lastName\":"
						+ "\"norris\",\"phone\":\"111-111-1111\",\"age\":6,\"patientHistory\":{\"medications\":"
						+ "[\"gummy bears\"],\"allergies\":[\"vegetables\",\"work\"]}}]}"));
	}

	@Test
	public void childAlertTest() throws Exception {
		mockMvc.perform(get("/childAlert?address=behind you")).andExpect(status().isOk())
				.andExpect(content().string("{\"childrens\":[{\"firstName\":\"super\",\"lastName\""
						+ ":\"norris\",\"age\":6}],\"adults\":[{\"firstName\":\"chuck\",\"lastName\":\"norris\"}]}"));
	}

	@Test
	public void firestationTest() throws Exception {
		mockMvc.perform(get("/firestation?stationNumber=1")).andExpect(status().isOk())
				.andExpect(content().string("{\"adultCount\":2,\"childrenCount\":1,\"persons\":[{\"firstName\":"
						+ "\"chuck\",\"lastName\":\"norris\",\"address\":\"behind you\",\"phone\":\"000-000-0000\"},"
						+ "{\"firstName\":\"super\",\"lastName\":\"norris\",\"address\":\"behind you\",\"phone\":\"111-111-1111\"},"
						+ "{\"firstName\":\"paysan\",\"lastName\":\"ducoin\",\"address\":\"dans lcoin\",\"phone\""
						+ ":\"999-999-9999\"}]}"));
	}
}
