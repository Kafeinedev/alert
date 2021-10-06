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
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.repository.MedicalRecordRepository;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordRepositoryTest {

	@Mock
	private DataCollectionJsonFileDAO mockDataCollectionDAO;

	private MedicalRecordRepository medicalRecordRepository;

	private DataCollection dataCollection;

	@BeforeEach
	private void setUpPerTest() {
		dataCollection = new DataCollection();
		dataCollection.setMedicalrecords(new ArrayList<MedicalRecord>());
		dataCollection.getMedicalrecords().add(new MedicalRecord("bonjour", "jemappelle", "datedenaissance",
				List.of("bla", "boa"), List.of("hihihi")));
		when(mockDataCollectionDAO.getAll()).thenReturn(dataCollection);
		medicalRecordRepository = new MedicalRecordRepository(mockDataCollectionDAO);
	}

	@Test
	public void getAll_whenWorkingProperly_returnListOfMedicalRecord() {
		List<MedicalRecord> toTest = medicalRecordRepository.getAll();

		assertThat(toTest.size()).isEqualTo(1);
		assertThat(toTest.get(0).getFirstName()).isEqualTo("bonjour");
		assertThat(toTest.get(0).getLastName()).isEqualTo("jemappelle");
		assertThat(toTest.get(0).getBirthdate()).isEqualTo("datedenaissance");
		assertThat(toTest.get(0).getMedications()).isEqualTo(List.of("bla", "boa"));
		assertThat(toTest.get(0).getAllergies()).isEqualTo(List.of("hihihi"));
	}

	@Test
	public void getAll_whenWorkingIncorrectly_returnEmptyList() {
		when(mockDataCollectionDAO.getAll()).thenReturn(null);

		List<MedicalRecord> toTest = medicalRecordRepository.getAll();

		assertThat(toTest.size()).isEqualTo(0);
	}

	@Test
	public void add_whenWorkingCorrectly_returnTrue() {
		MedicalRecord mr = new MedicalRecord("a", "b", "c", List.of(), List.of());
		when(mockDataCollectionDAO.update(any(DataCollection.class))).thenReturn(true);

		boolean test = medicalRecordRepository.add(mr);

		assertThat(test).isTrue();
	}

	@Test
	public void add_whenWorkingIncorrectly_returnFalse() {
		MedicalRecord mr = new MedicalRecord("a", "b", "c", List.of(), List.of());
		when(mockDataCollectionDAO.update(any(DataCollection.class))).thenReturn(false);

		boolean test = medicalRecordRepository.add(mr);

		assertThat(test).isFalse();
	}

	@Test
	public void add_whenMedicalRecordAlreadyExist_returnFalse() {
		MedicalRecord mr = new MedicalRecord("bonjour", "jemappelle", "datedenaissance", List.of("bla", "boa"),
				List.of("hihihi"));

		boolean test = medicalRecordRepository.add(mr);

		assertThat(test).isFalse();
	}

	@Test
	public void update_whenWorkingProperly_returnTrue() {
		MedicalRecord mr = new MedicalRecord("bonjour", "jemappelle", "upgrade", List.of("bla", "boa"),
				List.of("hihihi"));
		when(mockDataCollectionDAO.update(any(DataCollection.class))).thenReturn(true);

		boolean test = medicalRecordRepository.update(mr);

		assertThat(test).isTrue();
	}

	@Test
	public void update_whenWorkingIncorrectly_returnFalse() {
		MedicalRecord mr = new MedicalRecord("non", "jemappelle", "datedenaissance", List.of("bla", "boa"),
				List.of("hihihi"));

		boolean test = medicalRecordRepository.update(mr);

		assertThat(test).isFalse();
	}

	@Test
	public void delete_whenWorkingProperly_returnTrue() {
		MedicalRecord mr = new MedicalRecord("bonjour", "jemappelle", "upgrade", List.of("bla", "boa"),
				List.of("hihihi"));
		when(mockDataCollectionDAO.update(any(DataCollection.class))).thenReturn(true);

		boolean test = medicalRecordRepository.delete(mr);

		assertThat(test).isTrue();
	}

	@Test
	public void delete_whenWorkingIncorrectly_returnFalse() {
		MedicalRecord mr = new MedicalRecord("bonjour", "Ouin", "upgrade", List.of("bla", "boa"), List.of("hihihi"));

		boolean test = medicalRecordRepository.delete(mr);

		assertThat(test).isFalse();
	}
}
