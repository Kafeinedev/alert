package com.safetynet.alert.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safetynet.alert.DAO.DataCollectionJsonFileDAO;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.MedicalRecord;

@Repository
public class MedicalRecordRepository {

	private DataCollectionJsonFileDAO dataCollectionDAO;

	@Autowired
	public MedicalRecordRepository(DataCollectionJsonFileDAO dataCollectionDAO) {
		this.dataCollectionDAO = dataCollectionDAO;
	}

	public List<MedicalRecord> getAll() {
		DataCollection dataCollection = dataCollectionDAO.getAll();

		return dataCollection != null && dataCollection.getMedicalrecords() != null ? dataCollection.getMedicalrecords()
				: new ArrayList<MedicalRecord>();
	}

	public boolean add(MedicalRecord medicalRecord) {
		List<MedicalRecord> medicalRecords = getAll();

		int index = findIndex(medicalRecord, medicalRecords);
		if (index >= 0) {
			return false;
		}
		medicalRecords.add(medicalRecord);
		DataCollection dataCollection = new DataCollection();
		dataCollection.setMedicalrecords(medicalRecords);

		return dataCollectionDAO.update(dataCollection);
	}

	public boolean update(MedicalRecord medicalRecord) {
		List<MedicalRecord> medicalRecords = getAll();

		int index = findIndex(medicalRecord, medicalRecords);
		if (index >= 0) {
			medicalRecords.set(index, medicalRecord);
			DataCollection dataCollection = new DataCollection();
			dataCollection.setMedicalrecords(medicalRecords);

			return dataCollectionDAO.update(dataCollection);
		}
		return false;
	}

	public boolean delete(MedicalRecord medicalRecord) {
		List<MedicalRecord> medicalRecords = getAll();

		int index = findIndex(medicalRecord, medicalRecords);
		if (index >= 0) {
			medicalRecords.remove(index);
			DataCollection dataCollection = new DataCollection();
			dataCollection.setMedicalrecords(medicalRecords);

			return dataCollectionDAO.update(dataCollection);
		}

		return false;
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
