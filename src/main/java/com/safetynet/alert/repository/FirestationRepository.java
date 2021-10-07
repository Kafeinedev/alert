package com.safetynet.alert.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safetynet.alert.DAO.DataCollectionJsonFileDAO;
import com.safetynet.alert.exception.EntityAlreadyPresentException;
import com.safetynet.alert.exception.EntityMissingException;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.Firestation;

@Repository
public class FirestationRepository {

	private static Logger log = LogManager.getLogger("FirestationRepository logger");

	private DataCollectionJsonFileDAO dataCollectionDAO;

	@Autowired
	public FirestationRepository(DataCollectionJsonFileDAO dataCollectionDAO) {
		this.dataCollectionDAO = dataCollectionDAO;
	}

	public List<Firestation> getAll() throws FileAccessException {
		DataCollection dataCollection = dataCollectionDAO.getAll();

		return dataCollection != null && dataCollection.getFirestations() != null ? dataCollection.getFirestations()
				: new ArrayList<Firestation>();
	}

	public void add(Firestation firestation) throws FileAccessException, EntityAlreadyPresentException {
		List<Firestation> firestations = getAll();
		int index = findIndexByAddress(firestation, firestations);

		if (index != -1 && firestations.get(index).getStation().equals(firestation.getStation())) {
			log.error("Error trying to add a mapping already present");
			throw new EntityAlreadyPresentException();
		}

		firestations.add(firestation);
		DataCollection addedFirestation = new DataCollection();
		addedFirestation.setFirestations(firestations);
		dataCollectionDAO.update(addedFirestation);
	}

	public void update(Firestation firestation) throws FileAccessException, EntityMissingException {
		List<Firestation> firestations = getAll();
		int index = findIndexByAddress(firestation, firestations);

		if (index < 0) {
			log.error("Error trying to update a non existant mapping");
			throw new EntityMissingException();
		}

		firestations.set(index, firestation);
		DataCollection updatedFirestation = new DataCollection();
		updatedFirestation.setFirestations(firestations);
		dataCollectionDAO.update(updatedFirestation);
	}

	public void deleteAddressMapping(Firestation firestation) throws FileAccessException, EntityMissingException {
		List<Firestation> firestations = getAll();
		int index = findIndexByAddress(firestation, firestations);

		if (index < 0) {
			log.error("Error trying to delete a non existent address mapping");
			throw new EntityMissingException();
		}

		firestations.remove(index);
		DataCollection deletedAddress = new DataCollection();
		deletedAddress.setFirestations(firestations);
		dataCollectionDAO.update(deletedAddress);
	}

	public void deleteStationNumberMapping(Firestation firestation) throws FileAccessException, EntityMissingException {
		List<Firestation> firestations = getAll();
		List<Integer> indexes = findIndexesByStationNumber(firestation, firestations);

		if (indexes.size() < 1) {
			log.error("Error trying to delete non existent station number mapping");
			throw new EntityMissingException();
		}

		for (int index : indexes) {
			firestations.remove(index);
		}
		DataCollection deletedAddress = new DataCollection();
		deletedAddress.setFirestations(firestations);
		dataCollectionDAO.update(deletedAddress);
	}

	private int findIndexByAddress(Firestation firestation, List<Firestation> firestations) {
		for (int i = 0; i < firestations.size(); i++) {
			if (firestation.getAddress().equals(firestations.get(i).getAddress())) {
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
