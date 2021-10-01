package com.safetynet.alert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alert.DAO.DataCollectionJsonFileDAO;
import com.safetynet.alert.config.JsonDataConfig;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.Firestation;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DataCollectionJsonFileDAOTest {

	@Mock
	private ObjectMapper mockObjectMapper;
	@Mock
	private JsonDataConfig mockDataConfig;

	@InjectMocks
	private DataCollectionJsonFileDAO dataCollectionDAO;

	private static DataCollection dataCollection;

	@BeforeAll
	static private void setUp() {
		List<Firestation> firestations = new ArrayList<Firestation>();
		firestations.add(new Firestation("abc", "def"));
		dataCollection = new DataCollection();
		dataCollection.setFirestations(firestations);
	}

	@Test
	public void getAll_whenWorkingProperly_returnCorrectDataCollectionObject() {
		when(mockDataConfig.getPath()).thenReturn("nothing_to_see_here_move_along");
		try {
			when(mockObjectMapper.readValue(new File("nothing_to_see_here_move_along"), DataCollection.class))
					.thenReturn(dataCollection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DataCollection toTest = dataCollectionDAO.getAll();
		assertThat(toTest.getFirestations().get(0).getAddress())
				.isEqualTo(dataCollection.getFirestations().get(0).getAddress());
		assertThat(toTest.getFirestations().get(0).getStation())
				.isEqualTo(dataCollection.getFirestations().get(0).getStation());
	}

	@Test
	public void getAll_whenAProblemOccurs_returnNull() {
		when(mockDataConfig.getPath()).thenReturn("THIS_FILE_DOESNT_EXIST_AND_IF_IT_DOES_THERE_IS_A_PROBLEM");
		try {
			when(mockObjectMapper.readValue(new File("THIS_FILE_DOESNT_EXIST_AND_IF_IT_DOES_THERE_IS_A_PROBLEM"),
					DataCollection.class)).thenThrow(new MockitoException("Unit test exception"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		DataCollection toTest = dataCollectionDAO.getAll();

		assertThat(toTest).isNull();
	}

	@Test
	public void update_whenWorkingProperly_returnTrue() {
		when(mockDataConfig.getPath()).thenReturn("heh");

		boolean toTest = dataCollectionDAO.update(dataCollection);

		assertThat(toTest).isTrue();
	}

	@Test
	public void update_whenAProblemOccurs_returnFalse() {
		when(mockDataConfig.getPath()).thenReturn("heh");

		try {
			doThrow(new MockitoException("Unit test exception")).when(mockObjectMapper).writeValue(new File("heh"),
					dataCollection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean toTest = dataCollectionDAO.update(dataCollection);

		assertThat(toTest).isFalse();
	}

}
