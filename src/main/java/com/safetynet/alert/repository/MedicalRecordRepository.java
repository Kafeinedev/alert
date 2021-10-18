package com.safetynet.alert.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safetynet.alert.DAO.JsonFileReaderWriter;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.MedicalRecord;

@Repository
public class MedicalRecordRepository {

	private static Logger log = LogManager.getLogger("MedicalRecordRepository logger");

	private JsonFileReaderWriter dataCollectionDAO;

	@Autowired
	public MedicalRecordRepository(JsonFileReaderWriter dataCollectionDAO) {
		this.dataCollectionDAO = dataCollectionDAO;
	}

	public List<MedicalRecord> getAllMedicalRecords() {
		DataCollection dataCollection = dataCollectionDAO.getDataCollection();

		return dataCollection.getMedicalrecords() != null ? dataCollection.getMedicalrecords()
				: new ArrayList<MedicalRecord>();
	}

	public void add(MedicalRecord medicalRecord) throws FileAccessException, EntityAlreadyPresentException {
		List<MedicalRecord> medicalRecords = getAllMedicalRecords();

		if (findIndex(medicalRecord, medicalRecords) != -1) {
			log.error("Error trying to add a medicalrecord that already exist");
			throw new EntityAlreadyPresentException();
		}

		medicalRecords.add(medicalRecord);
		dataCollectionDAO.updateMedicalRecord(medicalRecords);
	}

	public void update(MedicalRecord medicalRecord) throws FileAccessException, EntityMissingException {
		List<MedicalRecord> medicalRecords = getAllMedicalRecords();
		int index = findIndex(medicalRecord, medicalRecords);

		if (index < 0) {
			log.error("Error trying to update a missing medical record");
			throw new EntityMissingException();
		}

		medicalRecords.set(index, medicalRecord);
		dataCollectionDAO.updateMedicalRecord(medicalRecords);
	}

	public void delete(MedicalRecord medicalRecord) throws FileAccessException, EntityMissingException {
		List<MedicalRecord> medicalRecords = getAllMedicalRecords();
		int index = findIndex(medicalRecord, medicalRecords);

		if (index < 0) {
			log.error("Error trying to delete missing medical record");
			throw new EntityMissingException();
		}

		medicalRecords.remove(index);
		dataCollectionDAO.updateMedicalRecord(medicalRecords);
	}

	public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
		List<MedicalRecord> medicalRecords = getAllMedicalRecords();
		int index = findIndex(new MedicalRecord(firstName, lastName, null, null, null), medicalRecords);

		if (index >= 0) {
			return medicalRecords.get(index);
		}

		return null;
	}

	private int findIndex(MedicalRecord medicalRecord, List<MedicalRecord> medicalRecords) {
		for (int i = 0; i < medicalRecords.size(); i++) {
			if (medicalRecords.get(i).getFirstName().equals(medicalRecord.getFirstName())
					&& medicalRecords.get(i).getLastName().equals(medicalRecord.getLastName())) {
				return i;
			}
		}
		return -1;
	}
}
