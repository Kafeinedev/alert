package com.safetynet.alert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alert.controller.CRUDController;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.service.CRUDService;

@WebMvcTest(controllers = CRUDController.class)
class CRUDControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CRUDService service;

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
		doThrow(new EntityAlreadyPresentException()).when(service).postPerson(any(Person.class));

		mockMvc.perform(
				post("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void postPerson_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");
		doThrow(new FileAccessException()).when(service).postPerson(any(Person.class));

		mockMvc.perform(
				post("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

	@Test
	public void postFirestationMapping_whenWorkingCorrectly_send2xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");

		mockMvc.perform(post("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void postFirestationMapping_whenMappingAlreadyExist_send4xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");
		doThrow(new EntityAlreadyPresentException()).when(service).postFirestationMapping(any(Firestation.class));

		mockMvc.perform(post("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
	}

	@Test
	public void postFirestationMapping_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");
		doThrow(new FileAccessException()).when(service).postFirestationMapping(any(Firestation.class));

		mockMvc.perform(post("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());
	}

	@Test
	public void postMedicalRecord_whenWorkingCorrectly_send2xx() throws JsonProcessingException, Exception {
		MedicalRecord mr = new MedicalRecord("john", "doe", "01-01-1900", List.of("test", "test"), List.of("test"));

		mockMvc.perform(
				post("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void postMedicalRecord_whenMedicalRecordAlreadyExist_send4xx() throws JsonProcessingException, Exception {
		MedicalRecord mr = new MedicalRecord("john", "doe", "01-01-1900", List.of("test", "test"), List.of("test"));
		doThrow(new EntityAlreadyPresentException()).when(service).postMedicalRecord(any(MedicalRecord.class));

		mockMvc.perform(
				post("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void postMedicalRecord_whenDataBaseAccessError_send_5xx() throws JsonProcessingException, Exception {
		MedicalRecord mr = new MedicalRecord("john", "doe", "01-01-1900", List.of("test", "test"), List.of("test"));
		doThrow(new FileAccessException()).when(service).postMedicalRecord(any(MedicalRecord.class));

		mockMvc.perform(
				post("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
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
		doThrow(new EntityMissingException()).when(service).putPerson(any(Person.class));

		mockMvc.perform(
				put("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void putPerson_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");
		doThrow(new FileAccessException()).when(service).putPerson(any(Person.class));

		mockMvc.perform(
				put("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

	@Test
	public void putFirestationMapping_whenWorkingCorrectly_send2xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");

		mockMvc.perform(put("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void putFirestationMapping_whenMappingDoesntExist_send4xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");
		doThrow(new EntityMissingException()).when(service).putFirestationMapping(any(Firestation.class));

		mockMvc.perform(put("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
	}

	@Test
	public void putFirestationMapping_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");
		doThrow(new FileAccessException()).when(service).putFirestationMapping(any(Firestation.class));

		mockMvc.perform(put("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());
	}

	@Test
	public void putMedicalRecord_whenWorkingCorrectly_send2xx() throws JsonProcessingException, Exception {
		MedicalRecord mr = new MedicalRecord("john", "doe", "01-01-1900", List.of("test", "test"), List.of("test"));

		mockMvc.perform(
				put("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void putMedicalRecord_whenMedicalRecordDoesntExist_send4xx() throws JsonProcessingException, Exception {
		MedicalRecord mr = new MedicalRecord("john", "doe", "01-01-1900", List.of("test", "test"), List.of("test"));
		doThrow(new EntityMissingException()).when(service).putMedicalRecord(any(MedicalRecord.class));

		mockMvc.perform(
				put("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void putMedicalRecord_whenDataBaseAccessError_send_5xx() throws JsonProcessingException, Exception {
		MedicalRecord mr = new MedicalRecord("john", "doe", "01-01-1900", List.of("test", "test"), List.of("test"));
		doThrow(new FileAccessException()).when(service).putMedicalRecord(any(MedicalRecord.class));

		mockMvc.perform(
				put("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
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
		doThrow(new EntityMissingException()).when(service).deletePerson(any(Person.class));

		mockMvc.perform(
				delete("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void deletePerson_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Person person = new Person("non", "nonnon", "ce n'est pas", "une chanson", "monotone", "nonnon", "nonnon");
		doThrow(new FileAccessException()).when(service).deletePerson(any(Person.class));

		mockMvc.perform(
				delete("/person").content(mapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

	@Test
	public void deleteFirestationMapping_whenWorkingCorrectly_send2xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");

		mockMvc.perform(delete("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void deleteFirestationMapping_whenMappingDoesntExist_send4xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");
		doThrow(new EntityMissingException()).when(service).deleteFirestationMapping(any(Firestation.class));

		mockMvc.perform(delete("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
	}

	@Test
	public void deleteFirestationMapping_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");
		doThrow(new FileAccessException()).when(service).deleteFirestationMapping(any(Firestation.class));

		mockMvc.perform(delete("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());
	}

	@Test
	public void deleteMedicalRecord_whenWorkingCorrectly_send2xx() throws JsonProcessingException, Exception {
		MedicalRecord mr = new MedicalRecord("john", "doe", "01-01-1900", List.of("test", "test"), List.of("test"));

		mockMvc.perform(
				delete("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void deleteMedicalRecord_whenMedicalRecordDoesntExist_send4xx() throws JsonProcessingException, Exception {
		MedicalRecord mr = new MedicalRecord("john", "doe", "01-01-1900", List.of("test", "test"), List.of("test"));
		doThrow(new EntityMissingException()).when(service).deleteMedicalRecord(any(MedicalRecord.class));

		mockMvc.perform(
				delete("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void deleteMedicalRecord_whenDataBaseAccessError_send_5xx() throws JsonProcessingException, Exception {
		MedicalRecord mr = new MedicalRecord("john", "doe", "01-01-1900", List.of("test", "test"), List.of("test"));
		doThrow(new FileAccessException()).when(service).deleteMedicalRecord(any(MedicalRecord.class));

		mockMvc.perform(
				delete("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

}
