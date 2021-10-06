package com.safetynet.alert.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safetynet.alert.DAO.DataCollectionJsonFileDAO;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.Firestation;

@Repository
public class FirestationRepository {

	private DataCollectionJsonFileDAO dataCollectionDAO;

	@Autowired
	public FirestationRepository(DataCollectionJsonFileDAO dataCollectionDAO) {
		this.dataCollectionDAO = dataCollectionDAO;
	}

	public List<Firestation> getAll() {
		DataCollection dataCollection = dataCollectionDAO.getAll();

		return dataCollection != null ? dataCollection.getFirestations() : new ArrayList<Firestation>();
	}

	public boolean add(Firestation firestation) {
		List<Firestation> firestations = getAll();
		int index = findIndexByAddress(firestation, firestations);
		if (index != -1 && firestations.get(index).getStation().equals(firestation.getStation())) {
			return false;
		}
		firestations.add(firestation);
		DataCollection addedFirestation = new DataCollection();
		addedFirestation.setFirestations(firestations);
		return dataCollectionDAO.update(addedFirestation);
	}

	public boolean update(Firestation firestation) {
		List<Firestation> firestations = getAll();

		int index = findIndexByAddress(firestation, firestations);
		if (index >= 0) {
			firestations.set(index, firestation);

			DataCollection updatedFirestation = new DataCollection();
			updatedFirestation.setFirestations(firestations);
			return dataCollectionDAO.update(updatedFirestation);
		}
		return false;
	}

	public boolean deleteAddressMapping(Firestation firestation) {
		List<Firestation> firestations = getAll();

		int index = findIndexByAddress(firestation, firestations);
		if (index >= 0) {
			firestations.remove(index);

			DataCollection deletedAddress = new DataCollection();
			deletedAddress.setFirestations(firestations);
			return dataCollectionDAO.update(deletedAddress);
		}
		return false;
	}

	public boolean deleteStationNumberMapping(Firestation firestation) {
		List<Firestation> firestations = getAll();

		List<Integer> indexes = findIndexesByStationNumber(firestation, firestations);
		for (int index : indexes) {
			firestations.remove(index);
		}
		if (indexes.size() > 0) {
			DataCollection deletedAddress = new DataCollection();
			deletedAddress.setFirestations(firestations);
			return dataCollectionDAO.update(deletedAddress);
		}
		return false;
	}

	private int findIndexByAddress(Firestation firestation, List<Firestation> firestations) {
		for (int i = 0; i < firestations.size(); i++) {
			if (firestation.getAddress().equalsIgnoreCase(firestations.get(i).getAddress())) {
				return i;
			}
		}
		return -1;
	}

	private List<Integer> findIndexesByStationNumber(Firestation firestation, List<Firestation> firestations) {
		List<Integer> ret = new ArrayList<Integer>();

		for (int i = 0; i < firestations.size(); i++) {
			if (firestation.getStation().equals(firestations.get(i).getStation())) {
				ret.add(i);
			}
		}

		return ret;
	}
}
