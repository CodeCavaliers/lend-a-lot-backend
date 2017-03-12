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
public class Debt {

	private String number;
	private Collection<Renter> renters;

	/**
	 * @return the fromNumber
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param fromNumber
	 *            the fromNumber to set
	 */
	public void setNumber(String fromNumber) {
		this.number = fromNumber;
	}

	/**
	 * @return the renters
	 */
	public Collection<Renter> getRenters() {
		return renters;
	}

	/**
	 * @param renters
	 *            the renters to set
	 */
	public void setRenters(Collection<Renter> renters) {
		this.renters = renters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append("{");
		strBuilder.append("\"number\"").append(":")
				.append("\"" + number + "\"");
		strBuilder.append(",");
		strBuilder.append("\"renters\"").append(":").append("[");

		if (renters != null) {

			Iterator<Renter> it = renters.iterator();

			while (it.hasNext()) {
				Renter renter = it.next();

				strBuilder.append(renter.toString());

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