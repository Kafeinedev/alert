package com.safetynet.alert.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alert.model.Person;

@Component
public class PersonUtil {

	public static void addNameToNode(ObjectNode node, Person person) {
		node.put("firstName", person.getFirstName());
		node.put("lastName", person.getLastName());
	}
}