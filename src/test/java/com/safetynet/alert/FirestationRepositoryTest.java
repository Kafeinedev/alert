package com.safetynet.alert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alert.DAO.DataCollectionJsonFileDAO;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.repository.FirestationRepository;

@ExtendWith(MockitoExtension.class)
class FirestationRepositoryTest {

	@Mock
	private DataCollectionJsonFileDAO mockDataCollectionDAO;

	private FirestationRepository firestationRepository;

	private DataCollection dataCollection;

	@BeforeEach
	private void setUpPerTest() {
		dataCollection = new DataCollection();
		dataCollection.setFirestations(new ArrayList<Firestation>());
		dataCollection.getFirestations().add(new Firestation("this is an address", "2"));
		dataCollection.getFirestations().add(new Firestation("this is another address", "1"));
		dataCollection.getFirestations().add(new Firestation("uno dos tres", "3"));
		when(mockDataCollectionDAO.getAll()).thenReturn(dataCollection);
		firestationRepository = new FirestationRepository(mockDataCollectionDAO);
	}

	@Test
	public void getAll_whenWorkingProperly_returnListOfFirestation() {
		List<Firestation> toTest = firestationRepository.getAll();

		assertThat(toTest.size()).isEqualTo(3);
		assertThat(toTest.get(0).getAddress()).isEqualTo("this is an address");
		assertThat(toTest.get(0).getStation()).isEqualTo("2");
		assertThat(toTest.get(1).getAddress()).isEqualTo("this is another address");
		assertThat(toTest.get(1).getStation()).isEqualTo("1");
		assertThat(toTest.get(2).getAddress()).isEqualTo("uno dos tres");
		assertThat(toTest.get(2).getStation()).isEqualTo("3");
	}

	@Test
	public void getAll_whenWorkingIncorrectlyOrEmpty_returnEmptyList() {
		when(mockDataCollectionDAO.getAll()).thenReturn(null);

		List<Firestation> toTest = firestationRepository.getAll();

		assertThat(toTest.size()).isEqualTo(0);
	}

	@Test
	public void add_whenWorkingCorrectly_returnTrue() {
		Firestation fs = new Firestation("pin pon", "caymerkril");
		when(mockDataCollectionDAO.update(any(DataCollection.class))).thenReturn(true);

		boolean test = firestationRepository.add(fs);

		assertThat(test).isEqualTo(true);
	}

	@Test
	public void add_whenWorkingIncorrectly_returnFalse() {
		Firestation fs = new Firestation("ça pik", "pas");
		when(mockDataCollectionDAO.update(any(DataCollection.class))).thenReturn(false);

		boolean test = firestationRepository.add(fs);

		assertThat(test).isEqualTo(false);
	}

	@Test
	public void add_whenAddingAMappingThatAlreadyExist_returnFalse() {
		Firestation fs = new Firestation("this is an address", "2");

		boolean test = firestationRepository.add(fs);

		assertThat(test).isEqualTo(false);
	}

	@Test
	public void update_whenWorkingCorrectly_returnTrue() {
		Firestation fs = new Firestation("this is an address", "5");
		when(mockDataCollectionDAO.update(any(DataCollection.class))).thenReturn(true);

		boolean test = firestationRepository.update(fs);

		assertThat(test).isEqualTo(true);
	}

	@Test
	public void update_whenWorkingIncorrectly_returnFalse() {
		Firestation fs = new Firestation("bderg", "drh");

		boolean test = firestationRepository.update(fs);

		assertThat(test).isEqualTo(false);
	}

	@Test
	public void deleteAddressMapping_whenWorkingCorrectly_returnTrue() {
		Firestation fs = new Firestation("this is an address", null);
		when(mockDataCollectionDAO.update(any(DataCollection.class))).thenReturn(true);

		boolean test = firestationRepository.deleteAddressMapping(fs);

		assertThat(test).isEqualTo(true);
	}

	@Test
	public void deleteAddressMapping_whenWorkingIncorrectly_returnFalse() {
		Firestation fs = new Firestation("drgderg", null);

		boolean test = firestationRepository.deleteAddressMapping(fs);

		assertThat(test).isFalse();
	}

	@Test
	public void deleteStationNumberMapping_whenWorkingCorrectly_returnTrue() {
		Firestation fs = new Firestation(null, "2");
		when(mockDataCollectionDAO.update(any(DataCollection.class))).thenReturn(true);

		boolean test = firestationRepository.deleteStationNumberMapping(fs);

		assertThat(test).isTrue();
	}

	@Test
	public void deleteStationNumberMapping_whenWorkingIncorrectly_returnFalse() {
		Firestation fs = new Firestation(null, "drgh");

		boolean test = firestationRepository.deleteStationNumberMapping(fs);

		assertThat(test).isFalse();

	}

}
