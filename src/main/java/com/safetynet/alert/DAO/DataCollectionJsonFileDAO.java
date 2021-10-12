package com.safetynet.alert.DAO;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alert.config.JsonDataConfig;
import com.safetynet.alert.exception.FileAccessException;
import com.safetynet.alert.model.DataCollection;
import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.model.Person;

@Repository
public class DataCollectionJsonFileDAO {

	private static Logger log = LogManager.getLogger("DataCollectionDAO logger");

	private ObjectMapper objectMapper;
	private JsonDataConfig config;

	@Autowired
	public DataCollectionJsonFileDAO(ObjectMapper objectMapper, JsonDataConfig config) {
		this.objectMapper = objectMapper;
		this.config = config;
	}

	private DataCollection readData() throws FileAccessException {
		DataCollection dataCollection = null;
		try {
			dataCollection = objectMapper.readValue(new File(config.getPath()), DataCollection.class);
		} catch (Exception e) {
			log.error("Error while reading json file", e);
			throw new FileAccessException();
		}
		return dataCollection != null ? dataCollection : new DataCollection();
	}

	private void writeData(DataCollection dataCollection) throws FileAccessException {
		try {
			objectMapper.writeValue(new File(config.getPath()), dataCollection);
		} catch (Exception e) {
			log.error("Error while writing json file", e);
			throw new FileAccessException();
		}
	}

	public DataCollection getAll() throws FileAccessException {
		return readData();
	}

	public void updatePerson(List<Person> persons) throws FileAccessException {
		DataCollection dataCollection = readData();
		dataCollection.setPersons(persons);
		writeData(dataCollection);
	}

	public void updateFirestation(List<Firestation> firestations) throws FileAccessException {
		DataCollection dataCollection = readData();
		dataCollection.setFirestations(firestations);
		writeData(dataCollection);
	}

	public void updateMedicalRecord(List<MedicalRecord> medicalRecords) throws FileAccessException {
		DataCollection dataCollection = readData();
		dataCollection.setMedicalrecords(medicalRecords);
		writeData(dataCollection);
	}

}
