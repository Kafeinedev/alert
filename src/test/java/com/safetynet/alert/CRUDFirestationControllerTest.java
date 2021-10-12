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
import com.safetynet.alert.controller.CRUDFirestationController;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.service.CRUDFirestationService;

@WebMvcTest(controllers = CRUDFirestationController.class)
class CRUDFirestationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CRUDFirestationService firestationService;

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
		doThrow(new EntityAlreadyPresentException()).when(firestationService)
				.postFirestationMapping(any(Firestation.class));

		mockMvc.perform(post("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
	}

	@Test
	public void postFirestationMapping_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");
		doThrow(new FileAccessException()).when(firestationService).postFirestationMapping(any(Firestation.class));

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
		doThrow(new EntityMissingException()).when(firestationService).putFirestationMapping(any(Firestation.class));

		mockMvc.perform(put("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
	}

	@Test
	public void putFirestationMapping_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");
		doThrow(new FileAccessException()).when(firestationService).putFirestationMapping(any(Firestation.class));

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
		doThrow(new EntityMissingException()).when(firestationService).deleteFirestationMapping(any(Firestation.class));

		mockMvc.perform(delete("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
	}

	@Test
	public void deleteFirestationMapping_whenDataBaseAccessError_send5xx() throws JsonProcessingException, Exception {
		Firestation firestation = new Firestation("address", "1");
		doThrow(new FileAccessException()).when(firestationService).deleteFirestationMapping(any(Firestation.class));

		mockMvc.perform(delete("/firestation").content(mapper.writeValueAsString(firestation))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());
	}

}
