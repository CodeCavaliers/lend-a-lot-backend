/**
 * 
 */
package com.codecavaliers.lendalot.rest;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * @author nicu
 *
 */
public class MongoDBSingleton {

	private static final MongoDBSingleton instance = new MongoDBSingleton();
	private static final String HOST_NAME = "localhost";
	private static final int PORT = 27017;
	private static final String DB_NAME = "lendalot";

	private static MongoClient mongoClient = null;
	private static DB mongoDatabase = null;

	/**
	 * @return
	 */
	public static MongoDBSingleton getInstance() {
		return instance;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public DB getDatabase() throws Exception {
		if (mongoDatabase == null) {
			mongoClient = getMongoClient();
		}
		return mongoClient.getDB(DB_NAME);
	}

	/**
	 * @return
	 * @throws Exception
	 */
	private MongoClient getMongoClient() throws Exception {
		mongoClient = new MongoClient(HOST_NAME, PORT);
		return mongoClient;
	}
}