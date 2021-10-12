package com.safetynet.alert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alert.DAO.DataCollectionJsonFileDAO;
import com.safetynet.alert.config.JsonDataConfig;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.model.Firestation;

@ExtendWith(MockitoExtension.class)
class DataCollectionJsonFileDAOTest {

	@Mock
	private ObjectMapper mockObjectMapper;
	@Mock
	private JsonDataConfig mockDataConfig;

	private DataCollectionJsonFileDAO dataCollectionDAO;

	private static DataCollection dataCollection;

	@BeforeEach
	private void setUp() {
		dataCollectionDAO = new DataCollectionJsonFileDAO(mockObjectMapper, mockDataConfig);
		List<Firestation> firestations = new ArrayList<Firestation>();
		firestations.add(new Firestation("abc", "def"));
		dataCollection = new DataCollection();
		dataCollection.setFirestations(firestations);
	}

	@Test
	public void getAll_whenWorkingProperly_returnCorrectDataCollectionObject()
			throws JsonParseException, JsonMappingException, IOException {
		when(mockDataConfig.getPath()).thenReturn("nothing_to_see_here_move_along");

		when(mockObjectMapper.readValue(new File("nothing_to_see_here_move_along"), DataCollection.class))
				.thenReturn(dataCollection);

		DataCollection toTest = dataCollectionDAO.getAll();
		assertThat(toTest.getFirestations().get(0).getAddress())
				.isEqualTo(dataCollection.getFirestations().get(0).getAddress());
		assertThat(toTest.getFirestations().get(0).getStation())
				.isEqualTo(dataCollection.getFirestations().get(0).getStation());
	}

	@Test
	public void getAll_whenAProblemOccurs_throwFileAccessException()
			throws JsonParseException, JsonMappingException, IOException {
		when(mockDataConfig.getPath()).thenReturn("THIS_FILE_DOESNT_EXIST_AND_IF_IT_DOES_THERE_IS_A_PROBLEM");

		when(mockObjectMapper.readValue(new File("THIS_FILE_DOESNT_EXIST_AND_IF_IT_DOES_THERE_IS_A_PROBLEM"),
				DataCollection.class)).thenThrow(new MockitoException("Unit test exception"));

		assertThrows(FileAccessException.class, () -> {
			dataCollectionDAO.getAll();
		});
	}

	@Test
	public void updatePerson_whenAProblemOccurs_throwsFileAccessException()
			throws JsonGenerationException, JsonMappingException, IOException {
		when(mockDataConfig.getPath()).thenReturn("heh");

		doThrow(new MockitoException("Unit test exception")).when(mockObjectMapper).writeValue(any(File.class),
				any(DataCollection.class));

		assertThrows(FileAccessException.class, () -> {
			dataCollectionDAO.updatePerson(new ArrayList<Person>());
		});
	}

	@Test
	public void updateFirestation_whenAProblemOccurs_throwsFileAccessException()
			throws JsonGenerationException, JsonMappingException, IOException {
		when(mockDataConfig.getPath()).thenReturn("heh");

		doThrow(new MockitoException("Unit test exception")).when(mockObjectMapper).writeValue(any(File.class),
				any(DataCollection.class));

		assertThrows(FileAccessException.class, () -> {
			dataCollectionDAO.updateFirestation(new ArrayList<Firestation>());
		});

	}

	@Test
	public void updateMedicalRecord_whenAProblemOccurs_throwsFileAccessException()
			throws JsonGenerationException, JsonMappingException, IOException {
		when(mockDataConfig.getPath()).thenReturn("heh");

		doThrow(new MockitoException("Unit test exception")).when(mockObjectMapper).writeValue(any(File.class),
				any(DataCollection.class));

		assertThrows(FileAccessException.class, () -> {
			dataCollectionDAO.updateMedicalRecord(new ArrayList<MedicalRecord>());
		});
	}
}
