package com.safetynet.alert.DAO;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alert.config.JsonDataConfig;
import com.safetynet.alert.model.DataCollection;

@Repository
public class DataCollectionJsonFileDAO {

	private static Logger log = LogManager.getLogger("DataCollectionDAO logger");

	private ObjectMapper objectMapper;
	private JsonDataConfig config;
	private DataCollection dataCollection;

	@Autowired
	public DataCollectionJsonFileDAO(ObjectMapper objectMapper, JsonDataConfig config) {
		this.objectMapper = objectMapper;
		this.config = config;
	}

	private void readData() {
		try {
			dataCollection = objectMapper.readValue(new File(config.getPath()), DataCollection.class);
		} catch (Exception e) {
			log.error("Error while reading json file", e);
		}
	}

	public DataCollection getAll() {
		readData();
		return dataCollection;
	}

	public boolean update(DataCollection updatedDataCollection) {
		boolean ret = true;
		readData();
		if (dataCollection == null) {
			dataCollection = new DataCollection();
		}
		if (updatedDataCollection.getPersons() != null) {
			dataCollection.setPersons(updatedDataCollection.getPersons());
		}
		if (updatedDataCollection.getFirestations() != null) {
			dataCollection.setFirestations(updatedDataCollection.getFirestations());
		}
		if (updatedDataCollection.getMedicalrecords() != null) {
			dataCollection.setMedicalrecords(updatedDataCollection.getMedicalrecords());
		}
		try {
			objectMapper.writeValue(new File(config.getPath()), dataCollection);
		} catch (Exception e) {
			ret = false;
			log.error("Error while writing json file", e);
		}
		return ret;
	}
}
