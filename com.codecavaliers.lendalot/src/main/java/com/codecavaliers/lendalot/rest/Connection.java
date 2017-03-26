/**
 * 
 */
package com.codecavaliers.lendalot.rest;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author nicu
 *
 */
public class Connection {

	private String hostName;
	private int port;
	private String dbName;

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	@Autowired
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	@Autowired
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName
	 *            the dbName to set
	 */
	@Autowired
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

}