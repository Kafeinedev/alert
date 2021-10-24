package com.safetynet.alert;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alert.controller.PersonController;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.service.PersonService;

@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PersonService mockPersonService;

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
		doThrow(new EntityAlreadyPresentException()).when(mockPersonService).postPerson(any(Person.class));

		mockMvc.perform(
				post("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void postPerson_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");
		doThrow(new FileAccessException()).when(mockPersonService).postPerson(any(Person.class));

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
		doThrow(new EntityMissingException()).when(mockPersonService).putPerson(any(Person.class));

		mockMvc.perform(
				put("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void putPerson_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");
		doThrow(new FileAccessException()).when(mockPersonService).putPerson(any(Person.class));

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
		doThrow(new EntityMissingException()).when(mockPersonService).deletePerson(any(Person.class));

		mockMvc.perform(
				delete("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void deletePerson_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");
		doThrow(new FileAccessException()).when(mockPersonService).deletePerson(any(Person.class));

		mockMvc.perform(
				delete("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

	@Test
	public void getCommunityEmail_whenWorkingCorrectly_send200WithProperContent() throws Exception {
		ArrayNode emails = mapper.createArrayNode();
		emails.add("blabla");
		emails.add("car");
		when(mockPersonService.getCommunityEmail("test")).thenReturn(emails);
		mockMvc.perform(get("/communityEmail?city=test")).andExpect(status().isOk())
				.andExpect(content().string(emails.toString()));
	}

	@Test
	public void getCommunityEmail_whenDataBaseAccessError_send5xx() throws Exception {
		when(mockPersonService.getCommunityEmail("la bégude")).thenThrow(new FileAccessException());
		mockMvc.perform(get("/communityEmail?city=la bégude")).andExpect(status().is5xxServerError());
	}

	@Test
	public void getPersonInfo_whenWorkingProperly_send200WithProperContent() throws Exception {
		ArrayNode personInfo = mapper.createArrayNode();
		personInfo.add("yes its correct");
		when(mockPersonService.getPersonInfo("nawak", "test")).thenReturn(personInfo);
		mockMvc.perform(get("/personInfo?firstName=nawak&lastName=test")).andExpect(status().isOk())
				.andExpect(content().string(personInfo.toString()));
	}

	@Test
	public void getPersonInfo_whenDataBaseAccessError_send5xx() throws Exception {
		when(mockPersonService.getPersonInfo("nawak", "NAAAAAAME")).thenThrow(new FileAccessException());
		mockMvc.perform(get("/personInfo?firstName=nawak&lastName=NAAAAAAME")).andExpect(status().is5xxServerError());
	}

	@Test
	public void getChildAlert_whenWorkingProperly_send200WithProperContent() throws Exception {
		ObjectNode childrensList = mapper.createObjectNode();
		childrensList.put("pretend I'm a list", "Yes a list");
		when(mockPersonService.getChildAlert("test")).thenReturn(childrensList);
		mockMvc.perform(get("/childAlert?address=test")).andExpect(status().isOk())
				.andExpect(content().string(childrensList.toString()));
	}

	@Test
	public void getChildAlert_whenDataBaseAccessError_send5xx() throws Exception {
		when(mockPersonService.getChildAlert("no dress")).thenThrow(new FileAccessException());
		mockMvc.perform(get("/childAlert?address=no dress")).andExpect(status().is5xxServerError());
	}

}
