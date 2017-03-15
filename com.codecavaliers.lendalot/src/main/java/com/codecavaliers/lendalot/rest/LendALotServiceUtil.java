/**
 * 
 */
package com.codecavaliers.lendalot.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * @author nicu
 *
 * Used for extracting the required information from the data base
 */
public class LendALotServiceUtil {

	public static final String COL_NAME_FROM_NUMBER = "fromNumber";
	public static final String COL_NAME_TO_NUMBER = "toNumber";
	public static final String COL_NAME_PRODUCT = "product";
	public static final String COL_NAME_QUANTITY = "quantity";

	private static final String HOST_NAME = "localhost";
	private static final String DB_NAME = "lendalot";
	private static final String TABLE_NAME = "lendalot";

	/**
	 * @param nr
	 * @param fromColName
	 * @param toColName
	 * @return
	 * @throws Exception
	 * 
	 * returns an object that contains all the information about the things that people borrowed
	 */
	public static Debt getDebts(String nr, String fromColName, String toColName)
			throws Exception {

		MongoClient mongo = new MongoClient(HOST_NAME, 27017);
		DB db = mongo.getDB(DB_NAME);
		DBCollection table = db.getCollection(TABLE_NAME);

		BasicDBObject query = new BasicDBObject(fromColName, nr);

		DBCursor cursor = table.find(query);

		Map<String, Renter> renters = new HashMap<String, Renter>();

		while (cursor.hasNext()) {

			DBObject row = cursor.next();

			String number = (String) row.get(toColName);
			String product = (String) row
					.get(LendALotServiceUtil.COL_NAME_PRODUCT);
			Integer quantity = (Integer) row
					.get(LendALotServiceUtil.COL_NAME_QUANTITY);

			Renter renter = renters.get(number);

			if (renter == null) {
				renter = new Renter();
				renter.setNumber(number);
				renters.put(number, renter);
			}

			Collection<Product> products = renter.getProducts();

			if (products == null) {
				products = new HashSet<Product>();
				renter.setProducts(products);
			}

			Product foundProduct = null;

			for (Product prd : products) {
				if (prd.getProduct().equals(product)) {
					foundProduct = prd;
					break;
				}
			}

			if (foundProduct == null) {
				foundProduct = new Product();

				foundProduct.setProduct(product);
				foundProduct.setQuantity(0);
				products.add(foundProduct);
			}

			Integer prdQuatity = foundProduct.getQuantity();

			prdQuatity += quantity;

			foundProduct.setQuantity(prdQuatity);
		}

		Debt debts = new Debt();

		debts.setNumber(nr);
		debts.setRenters(renters.values());

		return debts;
	}

	/**
	 * @param debts
	 * @throws Exception
	 * 
	 * Replace the information from the data base with the one received
	 */
	public static void saveDebts(Debt debts) throws Exception {

		if (debts != null && debts.getRenters() != null
				&& !debts.getRenters().isEmpty()) {

			MongoClient mongo = new MongoClient("localhost", 27017);

			DB db = mongo.getDB("lendalot");

			DBCollection table = db.getCollection("lendalot");
			
			//clears the collection
			table.remove(new BasicDBObject());

			for (Renter renter : debts.getRenters()) {

				Collection<Product> products = renter.getProducts();

				if (products != null) {

					for (Product product : products) {

						BasicDBObject document = new BasicDBObject();

						document.put("fromNumber", debts.getNumber());
						document.put("toNumber", renter.getNumber());
						document.put("product", product.getProduct());
						document.put("quantity", product.getQuantity());

						table.insert(document);
					}
				}
			}
		}
	}
}