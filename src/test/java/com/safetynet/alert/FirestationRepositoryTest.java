package com.safetynet.alert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alert.DAO.DataCollectionJsonFileDAO;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
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
	public void getAll_whenDatabaseEmpty_returnEmptyList() {
		when(mockDataCollectionDAO.getAll()).thenReturn(new DataCollection());

		List<Firestation> toTest = firestationRepository.getAll();

		assertThat(toTest.size()).isEqualTo(0);
	}

	@Test
	public void add_whenAddingAMappingThatAlreadyExist_throwEntityAlreadyPresentException() {
		Firestation fs = new Firestation("this is an address", "2");

		assertThrows(EntityAlreadyPresentException.class, () -> {
			firestationRepository.add(fs);
		});
	}

	@Test
	public void update_whenTryingToUpdateMissingMapping_throwEntityMissingException() {
		Firestation fs = new Firestation("bderg", "drh");
		assertThrows(EntityMissingException.class, () -> {
			firestationRepository.update(fs);
		});
	}

	@Test
	public void deleteAddressMapping_whenTryingToDeleteMissingMapping_throwEntityMissingException() {
		Firestation fs = new Firestation("bderg", "drh");
		assertThrows(EntityMissingException.class, () -> {
			firestationRepository.deleteAddressMapping(fs);
		});
	}

	@Test
	public void deleteStationNumberMapping_whenTryingToDeleteMissingMapping_throwEntityMissingException() {
		Firestation fs = new Firestation("bderg", "drh");
		assertThrows(EntityMissingException.class, () -> {
			firestationRepository.deleteStationNumberMapping(fs);
		});
	}

	// This test may fail if the way to return data is changed /!\
	@Test
	public void findByStation_whenWorkingProperly_returnListOfAddress() {
		dataCollection.getFirestations().add(new Firestation("testo domingo", "1"));

		List<String> test = firestationRepository.findByStation("1");

		assertThat(test.size()).isEqualTo(2);
		assertThat(test.get(0)).isEqualTo("this is another address");
		assertThat(test.get(1)).isEqualTo("testo domingo");
	}

	@Test
	public void findByStation_whenNoStationServeThatAddress_returnEmptyList() {
		List<String> test = firestationRepository.findByStation("yo n'axiste pas");

		assertThat(test.size()).isEqualTo(0);
	}

	@Test
	public void findByAddress_whenWorkingProperly_returnStationNumber() {
		String number = firestationRepository.findByAddress("uno dos tres");

		assertThat(number).isEqualTo("3");
	}

	@Test
	public void findByAddress_whenNoStationServingThatAddress_returnEmptyString() {
		String number = firestationRepository.findByAddress("nothing to see here move along");

		assertThat(number).isEqualTo("");
	}
}
