package com.safetynet.alert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.repository.FirestationRepository;

@Service
public class CRUDFirestationService {

	@Autowired
	private FirestationRepository firestationRepository;

	public void postFirestationMapping(Firestation firestation) {
		firestationRepository.add(firestation);
	}

	public void putFirestationMapping(Firestation firestation) {
		firestationRepository.update(firestation);
	}

	public void deleteFirestationMapping(Firestation firestation) {
		if (firestation.getAddress() != null) {
			firestationRepository.deleteAddressMapping(firestation);
		} else if (firestation.getStation() != null) {
			firestationRepository.deleteStationNumberMapping(firestation);
		}
	}
}
