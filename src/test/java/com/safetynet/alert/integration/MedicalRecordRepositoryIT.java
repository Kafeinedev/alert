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
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.repository.MedicalRecordRepository;

@SpringBootTest
class MedicalRecordRepositoryIT {

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Autowired
	private DataCollectionJsonFileDAO dataCollectionDAO;

	@BeforeEach
	private void setUpPerTest() {
		DataCollection dc = new DataCollection();
		dc.setMedicalrecords(new ArrayList<MedicalRecord>());
		dataCollectionDAO.update(dc);
	}

	@Test
	public void addTest() {
		MedicalRecord toAdd = new MedicalRecord("bla", "blo", "date", List.of("ouin"), List.of("allergy", "caylemal"));

		medicalRecordRepository.add(toAdd);

		List<MedicalRecord> test = medicalRecordRepository.getAll();
		assertThat(test.size()).isEqualTo(1);
		assertThat(test.get(0).getFirstName()).isEqualTo("bla");
		assertThat(test.get(0).getLastName()).isEqualTo("blo");
		assertThat(test.get(0).getBirthdate()).isEqualTo("date");
		assertThat(test.get(0).getMedications()).isEqualTo(List.of("ouin"));
		assertThat(test.get(0).getAllergies()).isEqualTo(List.of("allergy", "caylemal"));
	}

	@Test
	public void updateTest() {
		MedicalRecord toAdd = new MedicalRecord("bla", "blo", "date", List.of("ouin"), List.of("allergy", "caylemal"));
		medicalRecordRepository.add(toAdd);
		MedicalRecord toUpdate = new MedicalRecord("bla", "blo", "jamais", List.of(), List.of("caylemal"));

		medicalRecordRepository.update(toUpdate);

		List<MedicalRecord> test = medicalRecordRepository.getAll();
		assertThat(test.size()).isEqualTo(1);
		assertThat(test.get(0).getFirstName()).isEqualTo("bla");
		assertThat(test.get(0).getLastName()).isEqualTo("blo");
		assertThat(test.get(0).getBirthdate()).isEqualTo("jamais");
		assertThat(test.get(0).getMedications()).isEqualTo(List.of());
		assertThat(test.get(0).getAllergies()).isEqualTo(List.of("caylemal"));
	}

	@Test
	public void deleteTest() {
		MedicalRecord toAdd = new MedicalRecord("bla", "blo", "date", List.of("ouin"), List.of("allergy", "caylemal"));
		medicalRecordRepository.add(toAdd);
		MedicalRecord toDelete = new MedicalRecord("bla", "blo", null, null, null);

		medicalRecordRepository.delete(toDelete);

		List<MedicalRecord> test = medicalRecordRepository.getAll();
		assertThat(test.size()).isEqualTo(0);
	}
}
