/**
 * 
 */
package com.codecavaliers.lendalot.rest;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author nicu
 *
 */
public class AggregatedDebts {

	private String number;
	private Collection<Person> persons;

	/**
	 * We need an empty constructor to instantiate from JSON object
	 */
	public AggregatedDebts() {

	}

	/**
	 * @param number
	 * @param persons
	 */
	public AggregatedDebts(String number, Collection<Person> persons) {
		this.number = number;
		this.persons = persons;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the persons
	 */
	public Collection<Person> getPersons() {
		return persons;
	}

	/**
	 * @param persons
	 *            the persons to set
	 */
	public void setPersons(Collection<Person> persons) {
		this.persons = persons;
	}

	/**
	 * Converts the object to a json string
	 */
	public String toJson() {

		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append("{");
		strBuilder.append("\"number\"").append(":")
				.append("\"" + number + "\"");
		strBuilder.append(",");
		strBuilder.append("\"persons\"").append(":").append("[");

		if (persons != null) {

			Iterator<Person> it = persons.iterator();

			while (it.hasNext()) {
				Person renter = it.next();

				strBuilder.append(renter.toJson());

				if (it.hasNext()) {
					strBuilder.append(",");
				}
			}
		}

		strBuilder.append("]");
		strBuilder.append("}");

		return strBuilder.toString();
	}
}