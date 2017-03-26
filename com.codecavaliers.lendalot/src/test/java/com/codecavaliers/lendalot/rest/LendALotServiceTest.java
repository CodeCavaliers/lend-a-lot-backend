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

	private static final String TEST_PHONE_NUMBER = "123456789";
	private static final String TEST_USER_NAME = "testName";
	private static String URL = "http://localhost:8080/lendalot/rest/lendalot/";
	private static String IMPUT = "{\"number\":\""
			+ TEST_PHONE_NUMBER
			+ "\",\"persons\":[{\"number\":\"987654322\",\"products\":[{\"product\":\"product2\",\"quantity\":\"20\"}]},{\"number\":\"987654321\",\"products\":[{\"product\":\"product1\",\"quantity\":\"60\"},{\"product\":\"product3\",\"quantity\":\"20\"}]}]}";

	/**
	 * 
	 */
	@Test
	public void testPersistDebts() {

		Client client = Client.create();

		WebResource webResource = client.resource(URL + "debts");

		String token = getToken(TEST_USER_NAME, TEST_PHONE_NUMBER);

		ClientResponse response = webResource.type("application/json")
				.header("token", token).post(ClientResponse.class, IMPUT);

		assertTrue(response.getStatus() == 201);
	}

	/**
	 * 
	 */
	@Test
	public void testRestoreDebts() {

		Client client = Client.create();

		WebResource webResource = client.resource(URL + "debts/"
				+ TEST_PHONE_NUMBER);

		String token = getToken(TEST_USER_NAME, TEST_PHONE_NUMBER);

		ClientResponse response = webResource.accept("application/json")
				.header("token", token).get(ClientResponse.class);

		assertTrue(response.getStatus() == 200);
	}

	/**
	 * 
	 */
	@Test
	public void testRestoreMyDebts() {

		Client client = Client.create();

		WebResource webResource = client.resource(URL + "myDebts/"
				+ TEST_PHONE_NUMBER);

		String token = getToken(TEST_USER_NAME, TEST_PHONE_NUMBER);

		ClientResponse response = webResource.accept("application/json")
				.header("token", token).get(ClientResponse.class);

		assertTrue(response.getStatus() == 200);
	}

	/**
	 * 
	 */
	@Test
	public void testAuthenticateCredentials() {

		Client client = Client.create();

		WebResource webResource = client.resource(URL + "authenticate");

		ClientResponse response = webResource.accept("application/json")
				.header("username", TEST_USER_NAME)
				.header("phoneNumber", TEST_PHONE_NUMBER)
				.get(ClientResponse.class);

		assertTrue(response.getStatus() == 200);
	}
	
	/**
	 * 
	 */
	@Test
	public void testMultiple() {

		String token1 = getToken(TEST_USER_NAME, TEST_PHONE_NUMBER);
		String token2 = getToken("test2", "987654321");
		
		Client client = Client.create();

		WebResource webResource = client.resource(URL + "debts/"
				+ TEST_PHONE_NUMBER);

		ClientResponse response = webResource.accept("application/json")
				.header("token", token1).get(ClientResponse.class);

		assertTrue(response.getStatus() == 200);
		
		webResource = client.resource(URL + "debts/"
				+ "987654321");

		response = webResource.accept("application/json")
				.header("token", token2).get(ClientResponse.class);
		
		assertTrue(response.getStatus() == 200);
	}

	/**
	 * @return
	 */
	private String getToken(String username, String phoneNumber) {

		Client client = Client.create();

		WebResource webResource = client.resource(URL + "authenticate");

		ClientResponse response = webResource.accept("application/json")
				.header("username", username)
				.header("phoneNumber", phoneNumber)
				.get(ClientResponse.class);

		return response.getHeaders().getFirst("token");
	}
}
