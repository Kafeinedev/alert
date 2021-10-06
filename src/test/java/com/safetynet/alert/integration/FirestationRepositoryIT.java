package com.safetynet.alert.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.safetynet.alert.DAO.DataCollectionJsonFileDAO;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.repository.FirestationRepository;

@SpringBootTest
class FirestationRepositoryIT {

	@Autowired
	private FirestationRepository firestationRepository;

	@Autowired
	private DataCollectionJsonFileDAO dataCollectionDAO;

	@BeforeEach
	private void setUpPerTest() {
		DataCollection dc = new DataCollection();
		dc.setFirestations(new ArrayList<Firestation>());
		dataCollectionDAO.update(dc);
	}

	@Test
	void addTest() {
		Firestation toAdd = new Firestation("pin pon cay mercril", "1");

		firestationRepository.add(toAdd);

		List<Firestation> test = firestationRepository.getAll();
		assertThat(test.size()).isEqualTo(1);
		assertThat(test.get(0).getAddress()).isEqualTo("pin pon cay mercril");
		assertThat(test.get(0).getStation()).isEqualTo("1");
	}

	@Test
	public void updateTest() {
		Firestation toAdd = new Firestation("pin pon cay mercril", "1");
		firestationRepository.add(toAdd);
		Firestation toUpdate = new Firestation("pin pon cay mercril", "2");

		firestationRepository.update(toUpdate);

		List<Firestation> test = firestationRepository.getAll();
		assertThat(test.size()).isEqualTo(1);
		assertThat(test.get(0).getAddress()).isEqualTo("pin pon cay mercril");
		assertThat(test.get(0).getStation()).isEqualTo("2");
	}

	@Test
	public void deleteAddressMappingTest() {
		Firestation toAdd = new Firestation("pin pon cay mercril", "1");
		firestationRepository.add(toAdd);
		Firestation toDelete = new Firestation("pin pon cay mercril", null);

		firestationRepository.deleteAddressMapping(toDelete);

		List<Firestation> test = firestationRepository.getAll();
		assertThat(test.size()).isEqualTo(0);
	}

	@Test
	public void deleteStationNumberTest() {
		Firestation toAdd = new Firestation("pin pon cay mercril", "1");
		firestationRepository.add(toAdd);
		Firestation toDelete = new Firestation(null, "1");

		firestationRepository.deleteStationNumberMapping(toDelete);

		List<Firestation> test = firestationRepository.getAll();
		assertThat(test.size()).isEqualTo(0);
	}
}
