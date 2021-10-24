package com.safetynet.alert.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.safetynet.alert.controller.FirestationController;
import com.safetynet.alert.controller.PersonController;

@SpringBootTest(properties = "com.safetynet.alert.jsonfileconfig.path=resources/get_request_test.json")
class GetRequestIntegrationTest {

	@Autowired
	private FirestationController firestationController;

	@Autowired
	private PersonController personController;

	private MockMvc mockMvc;

	@BeforeEach
	private void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(firestationController, personController).build();
	}

	@Test
	public void getCommunityEmailTest() throws Exception {
		mockMvc.perform(get("/communityEmail?city=city")).andExpect(status().isOk()).andExpect(content()
				.string("[\"chuck@norris.com\",\"super@norris.com\",\"other@norris.com\",\"paysan@ducoin.com\"]"));
	}

	@Test
	public void getPhoneAlertTest() throws Exception {
		mockMvc.perform(get("/phoneAlert?firestation=1")).andExpect(status().isOk())
				.andExpect(content().string("[\"000-000-0000\",\"111-111-1111\",\"999-999-9999\"]"));
	}

	@Test
	public void getPersonInfoTest() throws Exception {
		mockMvc.perform(get("/personInfo?firstName=super&lastName=norris")).andExpect(status().isOk())
				.andExpect(content().string("[{\"firstName\":\"super\",\"lastName\":\"norris\",\"address\":"
						+ "\"behind you\",\"age\":6,\"email\":\"super@norris.com\",\"patientHistory\":"
						+ "{\"medications\":[\"gummy bears\"],\"allergies\":[\"vegetables\",\"work\"]}},"
						+ "{\"firstName\":\"chuck\",\"lastName\":\"norris\",\"address\":\"behind you\",\"age\":"
						+ "81,\"email\":\"chuck@norris.com\",\"patientHistory\":{\"medications\":[],\"allergies\":[]}},"
						+ "{\"firstName\":\"other\",\"lastName\":\"norris\",\"address\":\"somewhere else\",\"age\":"
						+ "20,\"email\":\"other@norris.com\",\"patientHistory\":{\"medications\":[\"medication one,"
						+ " medication two\"],\"allergies\":[\"bullshit\"]}}]"));
	}

	@Test
	public void getFloodStationTest() throws Exception {
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
	public void getFireTest() throws Exception {
		mockMvc.perform(get("/fire?address=behind you")).andExpect(status().isOk())
				.andExpect(content().string("{\"station\":\"1\",\"inhabitants\":[{\"firstName\":\"chuck\",\"lastName\":"
						+ "\"norris\",\"phone\":\"000-000-0000\",\"age\":81,\"patientHistory\":"
						+ "{\"medications\":[],\"allergies\":[]}},{\"firstName\":\"super\",\"lastName\":"
						+ "\"norris\",\"phone\":\"111-111-1111\",\"age\":6,\"patientHistory\":{\"medications\":"
						+ "[\"gummy bears\"],\"allergies\":[\"vegetables\",\"work\"]}}]}"));
	}

	@Test
	public void getChildAlertTest() throws Exception {
		mockMvc.perform(get("/childAlert?address=behind you")).andExpect(status().isOk())
				.andExpect(content().string("{\"childrens\":[{\"firstName\":\"super\",\"lastName\""
						+ ":\"norris\",\"age\":6}],\"adults\":[{\"firstName\":\"chuck\",\"lastName\":\"norris\"}]}"));
	}

	@Test
	public void getFirestationTest() throws Exception {
		mockMvc.perform(get("/firestation?stationNumber=1")).andExpect(status().isOk())
				.andExpect(content().string("{\"adultCount\":2,\"childrenCount\":1,\"persons\":[{\"firstName\":"
						+ "\"chuck\",\"lastName\":\"norris\",\"address\":\"behind you\",\"phone\":\"000-000-0000\"},"
						+ "{\"firstName\":\"super\",\"lastName\":\"norris\",\"address\":\"behind you\",\"phone\":\"111-111-1111\"},"
						+ "{\"firstName\":\"paysan\",\"lastName\":\"ducoin\",\"address\":\"dans lcoin\",\"phone\""
						+ ":\"999-999-9999\"}]}"));
	}
}
