/**
 * 
 */
package com.codecavaliers.lendalot.rest;

import java.util.Iterator;
import java.util.Map;

/**
 * @author nicu
 *
 */
public class Renter {

	private String toNumber;
	private Map<String, Integer> products;

	/**
	 * @return the toNumber
	 */
	public String getToNumber() {
		return toNumber;
	}

	/**
	 * @param toNumber
	 *            the toNumber to set
	 */
	public void setToNumber(String toNumber) {
		this.toNumber = toNumber;
	}

	/**
	 * @return the products
	 */
	public Map<String, Integer> getProducts() {
		return products;
	}

	/**
	 * @param products
	 *            the products to set
	 */
	public void setProducts(Map<String, Integer> products) {
		this.products = products;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append("{");
		strBuilder.append("\"toNumber\"").append(":")
				.append("\"" + toNumber + "\"");
		strBuilder.append(",");
		strBuilder.append("\"products\"").append(":").append("[");

		if (products != null) {
			
			Iterator<Map.Entry<String, Integer>> it = products.entrySet().iterator();
			
			while (it.hasNext()) {
				Map.Entry<String, Integer> entry = it.next();
				
				strBuilder.append("{");
				strBuilder.append("\"product\"").append(":")
						.append("\"" + entry.getKey() + "\"");
				strBuilder.append(",");
				strBuilder.append("\"quantity\"").append(":")
						.append("\"" + entry.getValue() + "\"");
				strBuilder.append("}");
				
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
