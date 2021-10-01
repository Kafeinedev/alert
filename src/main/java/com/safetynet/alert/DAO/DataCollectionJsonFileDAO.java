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

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private JsonDataConfig config;

	public DataCollection getAll() {
		DataCollection ret = null;
		try {
			ret = objectMapper.readValue(new File(config.getPath()), DataCollection.class);
		} catch (Exception e) {
			log.error("Error while reading json file", e);
		}
		return ret;
	}

	public boolean update(DataCollection dataCollection) {
		boolean ret = true;
		try {
			objectMapper.writeValue(new File(config.getPath()), dataCollection);
		} catch (Exception e) {
			ret = false;
			log.error("Error while writing json file", e);
		}
		return ret;
	}
}
