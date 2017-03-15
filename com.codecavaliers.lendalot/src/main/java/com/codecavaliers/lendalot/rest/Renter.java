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
public class Renter {

	private String number;
	private Collection<Product> products;

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
	public String toString() {

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

				strBuilder.append(product.toString());

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
