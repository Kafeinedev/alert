package com.safetynet.alert;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alert.controller.CRUDPersonController;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.service.CRUDPersonService;

@WebMvcTest(controllers = CRUDPersonController.class)
class CRUDPersonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CRUDPersonService personService;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void postPerson_whenWorkingCorrectly_send2xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");

		mockMvc.perform(
				post("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());

	}

	@Test
	public void postPerson_whenPersonAlreadyPresent_send4xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");
		doThrow(new EntityAlreadyPresentException()).when(personService).postPerson(any(Person.class));

		mockMvc.perform(
				post("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void postPerson_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");
		doThrow(new FileAccessException()).when(personService).postPerson(any(Person.class));

		mockMvc.perform(
				post("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

	@Test
	public void putPerson_whenWorkingCorrectly_send2xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");

		mockMvc.perform(
				put("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void putPerson_whenPersonDoesntExist_send4xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");
		doThrow(new EntityMissingException()).when(personService).putPerson(any(Person.class));

		mockMvc.perform(
				put("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void putPerson_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");
		doThrow(new FileAccessException()).when(personService).putPerson(any(Person.class));

		mockMvc.perform(
				put("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

	@Test
	public void deletePerson_whenWorkingCorrectly_send2xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");

		mockMvc.perform(
				delete("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void deletePerson_whenPersonDoesntExist_send4xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");
		doThrow(new EntityMissingException()).when(personService).deletePerson(any(Person.class));

		mockMvc.perform(
				delete("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void deletePerson_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");
		doThrow(new FileAccessException()).when(personService).deletePerson(any(Person.class));

		mockMvc.perform(
				delete("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}
}
