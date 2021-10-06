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
	public void getAll_whenDatabaseEmpty_returnEmptyList() {
		when(mockDataCollectionDAO.getAll()).thenReturn(null);

		List<MedicalRecord> toTest = medicalRecordRepository.getAll();

		assertThat(toTest.size()).isEqualTo(0);
	}

	@Test
	public void add_whenMedicalRecordAlreadyPresent_throwEntityAlreadyPresentException() {
		MedicalRecord mr = new MedicalRecord("bonjour", "jemappelle", "datedenaissance", List.of("bla", "boa"),
				List.of("hihihi"));

		assertThrows(EntityAlreadyPresentException.class, () -> {
			medicalRecordRepository.add(mr);
		});
	}

	@Test
	public void update_whenMedicalRecordMissing_throwEntityMissingException() {
		MedicalRecord mr = new MedicalRecord("drgdgh", "jemappelle", "salut", List.of("bla", "boa"), List.of("hihihi"));

		assertThrows(EntityMissingException.class, () -> {
			medicalRecordRepository.update(mr);
		});
	}

	@Test
	public void delete_whenMedicalRecordMissing_throwEntityMissingException() {
		MedicalRecord mr = new MedicalRecord("drgdgh", "jemappelle", "salut", List.of("bla", "boa"), List.of("hihihi"));

		assertThrows(EntityMissingException.class, () -> {
			medicalRecordRepository.delete(mr);
		});
	}
}
