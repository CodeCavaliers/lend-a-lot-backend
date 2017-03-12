/**
 * 
 */
package com.codecavaliers.lendalot.rest;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author nicu
 *
 */
public class LendALotServiceTest {

	private static String URL = "http://localhost:8080/lendalot/rest/lendalot/";
	private static String IMPUT = "{\"fromNumber\":\"123456789\",\"toNumber\":\"987654322\",\"product\":\"product2\",\"quantity\":\"20\"}";

	@Test
	public void testGet() {

		Client client = Client.create();

		WebResource webResource = client.resource(URL + "restoreDebts/123456789");

		ClientResponse response = webResource.accept("application/json").get(
				ClientResponse.class);

		assertTrue(response.getStatus() == 200);
	}

	@Test
	public void testPost() {

		Client client = Client.create();

		WebResource webResource = client.resource(URL + "persistDebts");

		ClientResponse response = webResource.type("application/json").post(
				ClientResponse.class, IMPUT);

		assertTrue(response.getStatus() == 201);
	}
}
