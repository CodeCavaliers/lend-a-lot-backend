/**
 * 
 */
package com.codecavaliers.lendalot.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author nicu
 *
 *         Used for extracting the required information from the data base
 */
public class LendALotServiceHelper {

	private static final String ISSUER_NAME = "lendalot-backend";
	public static final String COL_NAME_FROM_NUMBER = "fromNumber";
	public static final String COL_NAME_TO_NUMBER = "toNumber";
	public static final String COL_NAME_PRODUCT = "product";
	public static final String COL_NAME_QUANTITY = "quantity";
	public static final String COL_NAME_USER = "user";
	public static final String COL_NAME_PHONE_NUMBER = "phoneNumber";
	public static final String COL_NAME_TOKEN = "token";

	private static final String RENTERS_TABLE_NAME = "lendalot";
	private static final String USERS_TABLE_NAME = "users";

	private static RsaJsonWebKey JWK = null;
	private static ApplicationContext context = new ClassPathXmlApplicationContext(
			new String[] { "Spring-Module.xml" });
	private static MongoDB mongoDB = (MongoDB) context.getBean("MongoDB");

	static {
		try {
			JWK = RsaJwkGenerator.generateJwk(2048);
			JWK.setKeyId("k1");
		} catch (JoseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param nr
	 * @param fromColName
	 * @param toColName
	 * @return
	 * @throws Exception
	 * 
	 *             returns an object that contains all the information about the
	 *             things that people borrowed
	 */
	public static AggregatedDebts getDebts(String nr, String fromColName,
			String toColName) throws Exception {

		DB db = mongoDB.getDatabase();
		DBCollection table = db.getCollection(RENTERS_TABLE_NAME);

		BasicDBObject query = new BasicDBObject(fromColName, nr);

		// get all the debts that are linked to received phone number
		DBCursor cursor = table.find(query);

		Map<String, Person> renters = new HashMap<String, Person>();

		// aggregate the debts
		while (cursor.hasNext()) {

			DBObject row = cursor.next();

			String number = (String) row.get(toColName);
			String product = (String) row
					.get(LendALotServiceHelper.COL_NAME_PRODUCT);
			Integer quantity = (Integer) row
					.get(LendALotServiceHelper.COL_NAME_QUANTITY);

			Person renter = renters.get(number);

			// if there isn't a person linked to that number, create one
			if (renter == null) {
				renter = new Person(number, new HashSet<Product>());
				renters.put(number, renter);
			}

			Collection<Product> products = renter.getProducts();

			Product foundProduct = null;

			// returns the product with the given name
			for (Product prd : products) {
				if (prd.getProduct().equals(product)) {
					foundProduct = prd;
					break;
				}
			}

			// if the product wasn't found, that means that it's a new product
			if (foundProduct == null) {
				foundProduct = new Product(product, 0);

				products.add(foundProduct);
			}

			foundProduct.addQuantity(quantity);
		}

		return new AggregatedDebts(nr, renters.values());
	}

	/**
	 * @param debts
	 * @throws Exception
	 * 
	 *             Replace the information from the data base with the one
	 *             received
	 */
	public static void saveDebts(AggregatedDebts debts) throws Exception {

		if (debts != null && debts.getPersons() != null
				&& !debts.getPersons().isEmpty()) {

			DB db = mongoDB.getDatabase();

			DBCollection table = db.getCollection(RENTERS_TABLE_NAME);

			// clears the collection
			table.remove(new BasicDBObject());

			for (Person renter : debts.getPersons()) {

				Collection<Product> products = renter.getProducts();

				if (products != null) {

					for (Product product : products) {

						BasicDBObject document = new BasicDBObject();

						document.put(COL_NAME_FROM_NUMBER, debts.getNumber());
						document.put(COL_NAME_TO_NUMBER, renter.getNumber());
						document.put(COL_NAME_PRODUCT, product.getProduct());
						document.put(COL_NAME_QUANTITY, product.getQuantity());

						table.insert(document);
					}
				}
			}
		}
	}

	/**
	 * @param username
	 * @param phoneNumber
	 * @return
	 * @throws Exception
	 * 
	 *             Save the phone number and the user name in the database and
	 *             returns a token that contains the user name as the issuer
	 */
	public static String getToken(String username, String phoneNumber)
			throws Exception {

		DB db = mongoDB.getDatabase();
		DBCollection table = db.getCollection(USERS_TABLE_NAME);

		BasicDBObject fields = new BasicDBObject();

		fields.put(COL_NAME_USER, username);
		fields.put(COL_NAME_PHONE_NUMBER, phoneNumber);

		DBCursor cursor = table.find(fields);

		// add them to the data base if they don't exist
		if (cursor.size() == 0) {

			BasicDBObject document = new BasicDBObject();

			document.put(COL_NAME_USER, username);
			document.put(COL_NAME_PHONE_NUMBER, phoneNumber);

			table.insert(document);
		}

		// Create the Claims, which will be the content of the JWT
		JwtClaims claims = new JwtClaims();

		claims.setIssuer(ISSUER_NAME);
		claims.setAudience(username);
		claims.setGeneratedJwtId();

		JsonWebSignature jws = new JsonWebSignature();

		jws.setPayload(claims.toJson());

		jws.setKeyIdHeaderValue(JWK.getKeyId());
		jws.setKey(JWK.getPrivateKey());

		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

		return jws.getCompactSerialization();
	}

	/**
	 * @param token
	 * @param phoneNumber
	 * @return
	 * @throws Exception
	 * 
	 *             Checks if the phone number exist in the database and if it is
	 *             linked to the issuer name that is contained in the token
	 */
	public static boolean validateToken(String token, String phoneNumber)
			throws Exception {

		boolean result = false;

		if (token != null && phoneNumber != null) {

			DB db = mongoDB.getDatabase();
			DBCollection table = db.getCollection(USERS_TABLE_NAME);

			BasicDBObject fields = new BasicDBObject();

			fields.put(COL_NAME_PHONE_NUMBER, phoneNumber);

			DBCursor cursor = table.find(fields);
			DBObject row = cursor.next();

			String username = (String) row.get(COL_NAME_USER);

			JwtConsumer jwtConsumer = new JwtConsumerBuilder()
					.setExpectedIssuer(ISSUER_NAME)
					.setExpectedAudience(username)
					.setVerificationKey(JWK.getKey()).build();

			try {
				// Validate the JWT and process it to the Claims
				jwtConsumer.processToClaims(token);

				result = true;

			} catch (InvalidJwtException e) {
				result = false;
			}
		}

		return result;
	}
}