/**
 * 
 */
package com.codecavaliers.lendalot.rest;

/**
 * @author nicu
 *
 */
public class Product {

	private String product;
	private Integer quantity;

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product
	 *            the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * Converts the object to a json string
	 */
	public String toString() {

		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append("{");
		strBuilder.append("\"product\"").append(":")
				.append("\"" + product + "\"");
		strBuilder.append(",");
		strBuilder.append("\"quantity\"").append(":")
				.append("\"" + quantity + "\"");
		strBuilder.append("}");

		return strBuilder.toString();
	}
}
