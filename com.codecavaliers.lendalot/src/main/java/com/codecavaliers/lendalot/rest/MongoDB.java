/**
 * 
 */
package com.codecavaliers.lendalot.rest;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * @author nicu
 *
 */
public class MongoDB {

	@Autowired
	private Connection connection;
	private MongoClient mongoClient = null;
	private DB mongoDatabase = null;

	/**
	 * @param connection
	 */
	public MongoDB(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public DB getDatabase() throws Exception {
		if (mongoDatabase == null) {
			mongoClient = getMongoClient();
		}
		return mongoClient.getDB(connection.getDbName());
	}

	/**
	 * @return
	 * @throws Exception
	 */
	private MongoClient getMongoClient() throws Exception {
		mongoClient = new MongoClient(connection.getHostName(),
				connection.getPort());
		return mongoClient;
	}
}
