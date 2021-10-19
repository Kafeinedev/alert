package com.safetynet.alert.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alert.DAO.JsonFileReaderWriter;
import com.safetynet.alert.controller.PersonController;
import com.safetynet.alert.model.Person;

@SpringBootTest
class PersonIntegrationTest {

	@Autowired
	private PersonController controller;

	@Autowired
	private JsonFileReaderWriter jsonFileIO;

	@Autowired
	private ObjectMapper mapper;

	private MockMvc mockMvc;

	@BeforeEach
	private void setUpPerTest() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		jsonFileIO.updatePerson(new ArrayList<Person>());
	}

	@Test
	public void addTest() throws JsonProcessingException, Exception {
		Person person = new Person("chuck", "norris", "behind you", "city", "00000", "phone", "email");

		mockMvc.perform(
				post("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON));

		List<Person> test = jsonFileIO.getDataCollection().getPersons();
		assertThat(test.size()).isEqualTo(1);
		assertThat(test.get(0).getFirstName()).isEqualTo("chuck");
		assertThat(test.get(0).getLastName()).isEqualTo("norris");
		assertThat(test.get(0).getAddress()).isEqualTo("behind you");
		assertThat(test.get(0).getCity()).isEqualTo("city");
		assertThat(test.get(0).getZip()).isEqualTo("00000");
		assertThat(test.get(0).getPhone()).isEqualTo("phone");
		assertThat(test.get(0).getEmail()).isEqualTo("email");
	}

	@Test
	public void updateTest() throws JsonProcessingException, Exception {
		Person personOne = new Person("chuck", "norris", "behind you", "city", "00000", "phone", "email");
		mockMvc.perform(
				post("/person").content(mapper.writeValueAsString(personOne)).contentType(MediaType.APPLICATION_JSON));

		Person personTwo = new Person("chuck", "norris", "siping a cocktail", "ville", "11111", "telephone",
				"courriel");
		mockMvc.perform(
				put("/person").content(mapper.writeValueAsString(personTwo)).contentType(MediaType.APPLICATION_JSON));

		List<Person> test = jsonFileIO.getDataCollection().getPersons();
		assertThat(test.size()).isEqualTo(1);
		assertThat(test.get(0).getFirstName()).isEqualTo("chuck");
		assertThat(test.get(0).getLastName()).isEqualTo("norris");
		assertThat(test.get(0).getAddress()).isEqualTo("siping a cocktail");
		assertThat(test.get(0).getCity()).isEqualTo("ville");
		assertThat(test.get(0).getZip()).isEqualTo("11111");
		assertThat(test.get(0).getPhone()).isEqualTo("telephone");
		assertThat(test.get(0).getEmail()).isEqualTo("courriel");
	}

	@Test
	public void deleteTest() throws JsonProcessingException, Exception {
		Person personOne = new Person("chuck", "norris", "behind you", "city", "00000", "phone", "email");
		Person personTwo = new Person("blabla", "car", "siping a cocktail", "ville", "11111", "telephone", "courriel");
		mockMvc.perform(
				post("/person").content(mapper.writeValueAsString(personOne)).contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(
				post("/person").content(mapper.writeValueAsString(personTwo)).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(delete("/person")
				.content(mapper.writeValueAsString(new Person("chuck", "norris", null, null, null, null, null)))
				.contentType(MediaType.APPLICATION_JSON));

		List<Person> test = jsonFileIO.getDataCollection().getPersons();
		assertThat(test.size()).isEqualTo(1);
		assertThat(test.get(0).getFirstName()).isEqualTo("blabla");
		assertThat(test.get(0).getLastName()).isEqualTo("car");
		assertThat(test.get(0).getAddress()).isEqualTo("siping a cocktail");
		assertThat(test.get(0).getCity()).isEqualTo("ville");
		assertThat(test.get(0).getZip()).isEqualTo("11111");
		assertThat(test.get(0).getPhone()).isEqualTo("telephone");
		assertThat(test.get(0).getEmail()).isEqualTo("courriel");
	}
}
