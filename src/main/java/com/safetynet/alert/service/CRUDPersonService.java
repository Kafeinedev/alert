package com.safetynet.alert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.PersonRepository;

@Service
public class CRUDPersonService {

	@Autowired
	private PersonRepository personRepository;

	public void postPerson(Person person) {
		personRepository.add(person);
	}

	public void putPerson(Person person) {
		personRepository.update(person);
	}

	public void deletePerson(Person person) {
		personRepository.delete(person);
	}
}
