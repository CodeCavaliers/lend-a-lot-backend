/**
 * 
 */
package com.codecavaliers.lendalot.rest;


/**
 * @author nicu
 *
 */
public class Row {

	private String fromNumber;
	private String toNumber;
	private String product;
	private int quantity;

	/**
	 * @return
	 */
	public String getFromNumber() {
		return fromNumber;
	}

	/**
	 * @param fromNumber
	 */
	public void setFromNumber(String fromNumber) {
		this.fromNumber = fromNumber;
	}

	/**
	 * @return
	 */
	public String getToNumber() {
		return toNumber;
	}

	/**
	 * @param toNumber
	 */
	public void setToNumber(String toNumber) {
		this.toNumber = toNumber;
	}

	/**
	 * @return
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
