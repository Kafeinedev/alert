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
import com.safetynet.alert.controller.MedicalRecordController;
import com.safetynet.alert.model.MedicalRecord;

@SpringBootTest
class MedicalRecordIntegrationTest {

	@Autowired
	private MedicalRecordController controller;

	@Autowired
	private JsonFileReaderWriter jsonFileIO;

	@Autowired
	private ObjectMapper mapper;

	private MockMvc mockMvc;

	@BeforeEach
	private void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		jsonFileIO.updateMedicalRecord(new ArrayList<MedicalRecord>());
	}

	@Test
	public void addTest() throws JsonProcessingException, Exception {
		MedicalRecord toAdd = new MedicalRecord("bla", "blo", "date", List.of("ouin"), List.of("allergy", "caylemal"));

		mockMvc.perform(post("/medicalRecord").content(mapper.writeValueAsString(toAdd))
				.contentType(MediaType.APPLICATION_JSON));

		List<MedicalRecord> test = jsonFileIO.getDataCollection().getMedicalrecords();
		assertThat(test.size()).isEqualTo(1);
		assertThat(test.get(0).getFirstName()).isEqualTo("bla");
		assertThat(test.get(0).getLastName()).isEqualTo("blo");
		assertThat(test.get(0).getBirthdate()).isEqualTo("date");
		assertThat(test.get(0).getMedications()).isEqualTo(List.of("ouin"));
		assertThat(test.get(0).getAllergies()).isEqualTo(List.of("allergy", "caylemal"));
	}

	@Test
	public void updateTest() throws JsonProcessingException, Exception {
		MedicalRecord mr = new MedicalRecord("bla", "blo", "date", List.of("ouin"), List.of("allergy", "caylemal"));
		mockMvc.perform(
				post("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON));

		mr.setBirthdate("jamais");
		mr.setMedications(List.of("allergy", "caylemal"));
		mr.setAllergies(List.of("Ouin"));
		mockMvc.perform(
				put("/medicalRecord").content(mapper.writeValueAsString(mr)).contentType(MediaType.APPLICATION_JSON));

		List<MedicalRecord> test = jsonFileIO.getDataCollection().getMedicalrecords();
		assertThat(test.size()).isEqualTo(1);
		assertThat(test.get(0).getFirstName()).isEqualTo("bla");
		assertThat(test.get(0).getLastName()).isEqualTo("blo");
		assertThat(test.get(0).getBirthdate()).isEqualTo("jamais");
		assertThat(test.get(0).getMedications()).isEqualTo(List.of("allergy", "caylemal"));
		assertThat(test.get(0).getAllergies()).isEqualTo(List.of("Ouin"));
	}

	@Test
	public void deleteTest() throws JsonProcessingException, Exception {
		MedicalRecord mrOne = new MedicalRecord("bla", "blo", "date", List.of("ouin"), List.of("allergy", "caylemal"));
		MedicalRecord mrTwo = new MedicalRecord("super", "man", "31/02/2021", List.of(), List.of());
		mockMvc.perform(post("/medicalRecord").content(mapper.writeValueAsString(mrOne))
				.contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(post("/medicalRecord").content(mapper.writeValueAsString(mrTwo))
				.contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(delete("/medicalRecord")
				.content(mapper.writeValueAsString(new MedicalRecord("bla", "blo", null, null, null)))
				.contentType(MediaType.APPLICATION_JSON));

		List<MedicalRecord> test = jsonFileIO.getDataCollection().getMedicalrecords();
		assertThat(test.size()).isEqualTo(1);
		assertThat(test.get(0).getFirstName()).isEqualTo("super");
		assertThat(test.get(0).getLastName()).isEqualTo("man");
		assertThat(test.get(0).getBirthdate()).isEqualTo("31/02/2021");
		assertThat(test.get(0).getMedications()).isEqualTo(List.of());
		assertThat(test.get(0).getAllergies()).isEqualTo(List.of());

	}
}