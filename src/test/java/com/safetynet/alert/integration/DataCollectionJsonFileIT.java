package com.safetynet.alert.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.safetynet.alert.DAO.DataCollectionJsonFileDAO;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.Firestation;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DataCollectionJsonFileIT {

	@Autowired
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
	public void writeThenReadTest() {
		dataCollectionDAO.update(dataCollection);

		DataCollection toTest = dataCollectionDAO.getAll();

		assertThat(toTest.getFirestations().get(0).getAddress())
				.isEqualTo(dataCollection.getFirestations().get(0).getAddress());
		assertThat(toTest.getFirestations().get(0).getStation())
				.isEqualTo(dataCollection.getFirestations().get(0).getStation());
	}
}
