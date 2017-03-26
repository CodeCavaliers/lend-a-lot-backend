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
	private int quantity;
	
	/**
	 * We need an empty constructor to instantiate from JSON object
	 */
	public Product() {
		
	}

	/**
	 * @param product
	 * @param quantity
	 */
	public Product(String product, int quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}

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
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * @param quantity
	 */
	public void addQuantity(int quantity) {
		this.quantity += quantity;
	}

	/**
	 * Converts the object to a json string
	 */
	public String toJson() {

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
