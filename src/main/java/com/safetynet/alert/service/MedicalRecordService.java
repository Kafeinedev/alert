package com.safetynet.alert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService {

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	public void postMedicalRecord(MedicalRecord medicalRecord) {
		medicalRecordRepository.add(medicalRecord);
	}

	public void putMedicalRecord(MedicalRecord medicalRecord) {
		medicalRecordRepository.update(medicalRecord);
	}

	public void deleteMedicalRecord(MedicalRecord medicalRecord) {
		medicalRecordRepository.delete(medicalRecord);
	}
}
