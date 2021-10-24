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

import java.util.List;

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
import com.safetynet.alert.controller.FirestationController;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.service.FirestationService;

@WebMvcTest(controllers = FirestationController.class)
class FirestationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirestationService mockFirestationService;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void postFirestationMapping_whenWorkingCorrectly_send2xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");

		mockMvc.perform(post("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void postFirestationMapping_whenMappingAlreadyExist_send4xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");
		doThrow(new EntityAlreadyPresentException()).when(mockFirestationService)
				.postFirestationMapping(any(Firestation.class));

		mockMvc.perform(post("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
	}

	@Test
	public void postFirestationMapping_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");
		doThrow(new FileAccessException()).when(mockFirestationService).postFirestationMapping(any(Firestation.class));

		mockMvc.perform(post("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());
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
		doThrow(new EntityMissingException()).when(mockFirestationService)
				.putFirestationMapping(any(Firestation.class));

		mockMvc.perform(put("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
	}

	@Test
	public void putFirestationMapping_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");
		doThrow(new FileAccessException()).when(mockFirestationService).putFirestationMapping(any(Firestation.class));

		mockMvc.perform(put("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());
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
		doThrow(new EntityMissingException()).when(mockFirestationService)
				.deleteFirestationMapping(any(Firestation.class));

		mockMvc.perform(delete("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
	}

	@Test
	public void deleteFirestationMapping_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");
		doThrow(new FileAccessException()).when(mockFirestationService)
				.deleteFirestationMapping(any(Firestation.class));

		mockMvc.perform(delete("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());
	}

	@Test
	public void getFloodStations_whenWorkingCorrectly_send200WithProperContent() throws Exception {
		ArrayNode testDummy = mapper.createArrayNode();
		testDummy.add("yes its correct");
		when(mockFirestationService.getFloodStations(List.of("test", "unique"))).thenReturn(testDummy);
		mockMvc.perform(get("/flood/stations?stations=test,unique")).andExpect(status().isOk())
				.andExpect(content().string(testDummy.toString()));
	}

	@Test
	public void getFloodStations_whenDatabaseAccessError_send5xx() throws Exception {
		when(mockFirestationService.getFloodStations(List.of("nawak"))).thenThrow(new FileAccessException());
		mockMvc.perform(get("/flood/stations?stations=nawak")).andExpect(status().is5xxServerError());
	}

	@Test
	public void getPhoneAlert_whenWorkingCorrectly_send200WithProperContent() throws Exception {
		ArrayNode phones = mapper.createArrayNode();
		phones.add("none of this text matter");
		when(mockFirestationService.getPhoneAlert("1337")).thenReturn(phones);
		mockMvc.perform(get("/phoneAlert?firestation=1337")).andExpect(status().isOk())
				.andExpect(content().string(phones.toString()));
	}

	@Test
	public void getPhoneAlert_whenDatabaseAccessError_send5xx() throws Exception {
		when(mockFirestationService.getPhoneAlert("number")).thenThrow(new FileAccessException());
		mockMvc.perform(get("/phoneAlert?firestation=number")).andExpect(status().is5xxServerError());
	}

	@Test
	public void getFirestation_whenWorkingProperly_send200WithProperContent() throws Exception {
		ObjectNode personsList = mapper.createObjectNode();
		personsList.put("bip boop Im a top tier list", "pouet");
		when(mockFirestationService.getFirestation("number")).thenReturn(personsList);
		mockMvc.perform(get("/firestation?stationNumber=number")).andExpect(status().isOk())
				.andExpect(content().string(personsList.toString()));
	}

	@Test
	public void getFirestation_whenDatabaseAccessError_send5xx() throws Exception {
		when(mockFirestationService.getFirestation("1337")).thenThrow(new FileAccessException());
		mockMvc.perform(get("/firestation?stationNumber=1337")).andExpect(status().is5xxServerError());
	}

	@Test
	public void getFire_whenWorkingProperly_send200WithProperContent() throws Exception {
		ObjectNode house = mapper.createObjectNode();
		house.put("this is a test", "what's inside doesnt matter");
		when(mockFirestationService.getFire("a dress")).thenReturn(house);
		mockMvc.perform(get("/fire?address=a dress")).andExpect(status().isOk())
				.andExpect(content().string(house.toString()));
	}

	@Test
	public void getFire_whenDatabaseAccessError_send5xx() throws Exception {
		when(mockFirestationService.getFire("no dress")).thenThrow(new FileAccessException());
		mockMvc.perform(get("/fire?address=no dress")).andExpect(status().is5xxServerError());
	}
}
