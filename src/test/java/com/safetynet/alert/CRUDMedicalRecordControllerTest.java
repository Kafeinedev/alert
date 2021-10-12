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
import com.safetynet.alert.controller.CRUDMedicalRecordController;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.service.CRUDMedicalRecordService;

@WebMvcTest(controllers = CRUDMedicalRecordController.class)
class CRUDMedicalRecordControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CRUDMedicalRecordService medicalrecordService;

	private ObjectMapper mapper = new ObjectMapper();

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
		doThrow(new EntityAlreadyPresentException()).when(medicalrecordService)
				.postMedicalRecord(any(MedicalRecord.class));

		mockMvc.perform(
				post("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void postMedicalRecord_whenDataBaseAccessError_send_5xx() throws JsonProcessingException, Exception {
		MedicalRecord mr = new MedicalRecord("john", "doe", "01-01-1900", List.of("test", "test"), List.of("test"));
		doThrow(new FileAccessException()).when(medicalrecordService).postMedicalRecord(any(MedicalRecord.class));

		mockMvc.perform(
				post("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
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
		doThrow(new EntityMissingException()).when(medicalrecordService).putMedicalRecord(any(MedicalRecord.class));

		mockMvc.perform(
				put("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void putMedicalRecord_whenDataBaseAccessError_send_5xx() throws JsonProcessingException, Exception {
		MedicalRecord mr = new MedicalRecord("john", "doe", "01-01-1900", List.of("test", "test"), List.of("test"));
		doThrow(new FileAccessException()).when(medicalrecordService).putMedicalRecord(any(MedicalRecord.class));

		mockMvc.perform(
				put("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
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
		doThrow(new EntityMissingException()).when(medicalrecordService).deleteMedicalRecord(any(MedicalRecord.class));

		mockMvc.perform(
				delete("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void deleteMedicalRecord_whenDataBaseAccessError_send_5xx() throws JsonProcessingException, Exception {
		MedicalRecord mr = new MedicalRecord("john", "doe", "01-01-1900", List.of("test", "test"), List.of("test"));
		doThrow(new FileAccessException()).when(medicalrecordService).deleteMedicalRecord(any(MedicalRecord.class));

		mockMvc.perform(
				delete("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

}
