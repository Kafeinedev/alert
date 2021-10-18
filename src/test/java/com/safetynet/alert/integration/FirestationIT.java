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
import com.safetynet.alert.controller.FirestationController;
import com.safetynet.alert.model.Firestation;

@SpringBootTest
class FirestationIT {

	@Autowired
	private FirestationController controller;

	@Autowired
	private JsonFileReaderWriter jsonFileIO;

	@Autowired
	private ObjectMapper mapper;

	private MockMvc mockMvc;

	@BeforeEach
	private void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		jsonFileIO.updateFirestation(new ArrayList<Firestation>());
	}

	@Test
	public void addTest() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("pin pon çay merkril", "3");

		mockMvc.perform(post("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON));

		List<Firestation> test = jsonFileIO.getDataCollection().getFirestations();
		assertThat(test.size()).isEqualTo(1);
		assertThat(test.get(0).getAddress()).isEqualTo("pin pon çay merkril");
		assertThat(test.get(0).getStation()).isEqualTo("3");
	}

	@Test
	public void updateTest() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("pin pon çay merkril", "3");
		mockMvc.perform(post("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON));

		firestation.setStation("5");
		mockMvc.perform(put("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON));

		List<Firestation> test = jsonFileIO.getDataCollection().getFirestations();
		assertThat(test.size()).isEqualTo(1);
		assertThat(test.get(0).getAddress()).isEqualTo("pin pon çay merkril");
		assertThat(test.get(0).getStation()).isEqualTo("5");
	}

	@Test
	public void deleteAddressMapping() throws JsonProcessingException, Exception {
		mockMvc.perform(
				post("/firestation").content(mapper.writeValueAsString(new Firestation("pin pon çay merkril", "3")))
						.contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(post("/firestation").content(mapper.writeValueAsString(new Firestation("douce France", "1")))
				.contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(
				post("/firestation").content(mapper.writeValueAsString(new Firestation("Je suis un génie", "2")))
						.contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(delete("/firestation").content(mapper.writeValueAsString(new Firestation("douce France", "2")))
				.contentType(MediaType.APPLICATION_JSON));

		List<Firestation> test = jsonFileIO.getDataCollection().getFirestations();
		assertThat(test.size()).isEqualTo(2);
		assertThat(test.get(0).getAddress()).isEqualTo("pin pon çay merkril");
		assertThat(test.get(1).getAddress()).isEqualTo("Je suis un génie");
	}

	@Test
	public void deleteStationMapping() throws JsonProcessingException, Exception {
		mockMvc.perform(
				post("/firestation").content(mapper.writeValueAsString(new Firestation("pin pon çay merkril", "2")))
						.contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(post("/firestation").content(mapper.writeValueAsString(new Firestation("douce France", "1")))
				.contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(
				post("/firestation").content(mapper.writeValueAsString(new Firestation("Je suis un génie", "2")))
						.contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(delete("/firestation").content(mapper.writeValueAsString(new Firestation(null, "2")))
				.contentType(MediaType.APPLICATION_JSON));

		List<Firestation> test = jsonFileIO.getDataCollection().getFirestations();
		assertThat(test.size()).isEqualTo(1);
		assertThat(test.get(0).getAddress()).isEqualTo("douce France");
		assertThat(test.get(0).getStation()).isEqualTo("1");
	}
}
