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
public class Person {

	private String number;
	private Collection<Product> products;
	
	/**
	 * We need an empty constructor to instantiate from JSON object
	 */
	public Person() {
		
	}

	/**
	 * @param number
	 * @param products
	 */
	public Person(String number, Collection<Product> products) {
		this.number = number;
		this.products = products;
	}

	/**
	 * @return the toNumber
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param toNumber
	 *            the toNumber to set
	 */
	public void setNumber(String toNumber) {
		this.number = toNumber;
	}

	/**
	 * @return the products
	 */
	public Collection<Product> getProducts() {
		return products;
	}

	/**
	 * @param products
	 *            the products to set
	 */
	public void setProducts(Collection<Product> products) {
		this.products = products;
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
		strBuilder.append("\"products\"").append(":").append("[");

		if (products != null) {

			Iterator<Product> it = products.iterator();

			while (it.hasNext()) {
				Product product = it.next();

				strBuilder.append(product.toJson());

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
