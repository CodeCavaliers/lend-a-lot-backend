/**
 * 
 */
package com.codecavaliers.lendalot.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author nicu
 *
 */
@Path("/lendalot")
public class LendALotService {

	private static final String VALUE_MISSING_MESSAGE = "Username or phone number is missing!!!";
	private static final String SUCCESS_MESSAGE = "Success";
	private static final String ERROR_MESSAGE = "Error";

	/**
	 * @param fNumber
	 * @return
	 * 
	 *         Produces a json that contains all the information about the
	 *         things that other people borrowed from you
	 */
	@GET
	@Path("/debts/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response restoreDebts(@HeaderParam("token") String token,
			@PathParam("param") String fNumber) {

		int statusCode;
		AggregatedDebts debts = null;

		try {
			// check if the phone number exist in the database and if it is
			// linked to the issuer name that is contained in the token
			if (LendALotServiceHelper.validateToken(token, fNumber)) {

				// returns an object that contain all the debts that were linked
				// to that phone number
				debts = LendALotServiceHelper.getDebts(fNumber,
						LendALotServiceHelper.COL_NAME_FROM_NUMBER,
						LendALotServiceHelper.COL_NAME_TO_NUMBER);
				statusCode = 200;
			} else {
				statusCode = 401;
			}

		} catch (Exception e) {
			statusCode = 500;
		}

		return Response.status(statusCode)
				.entity(debts == null ? ERROR_MESSAGE : debts.toJson()).build();
	}

	/**
	 * @param fNumber
	 * @return
	 * 
	 *         Produces a json that contains all the information about the
	 *         things that you borrowed from other people
	 */
	@GET
	@Path("/myDebts/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response restoreMyDebts(@HeaderParam("token") String token,
			@PathParam("param") String fNumber) {

		int statusCode;
		AggregatedDebts debts = null;

		try {
			// check if the phone number exist in the database and if it is
			// linked to the issuer name that is contained in the token
			if (LendALotServiceHelper.validateToken(token, fNumber)) {

				// returns an object that contain all the debts that were linked
				// to that phone number
				debts = LendALotServiceHelper.getDebts(fNumber,
						LendALotServiceHelper.COL_NAME_TO_NUMBER,
						LendALotServiceHelper.COL_NAME_FROM_NUMBER);
				statusCode = 200;

			} else {
				statusCode = 401;
			}

		} catch (Exception e) {
			statusCode = 500;
		}

		return Response.status(statusCode)
				.entity(debts == null ? ERROR_MESSAGE : debts.toJson()).build();
	}

	/**
	 * @param debts
	 * @return
	 * 
	 *         Replace the information from the data base with the one received
	 */
	@POST
	@Path("/debts/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response persistDebts(@HeaderParam("token") String token,
			AggregatedDebts debts) {

		int statusCode;

		try {
			// check if the phone number exist in the database and if it is
			// linked to the issuer name that is contained in the token
			if (LendALotServiceHelper.validateToken(token, debts.getNumber())) {

				// replace the deps from the data base with the ones received
				LendALotServiceHelper.saveDebts(debts);

				statusCode = 201;

			} else {
				statusCode = 401;
			}

		} catch (Exception e) {
			statusCode = 500;
		}

		return Response.status(statusCode).build();
	}

	/**
	 * @param username
	 * @param phoneNumber
	 * @return
	 * 
	 *         Saves the user name and the phone number if they don't exist and
	 *         returns a token
	 */
	@Path("/authenticate")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticateCredentials(
			@HeaderParam("username") String username,
			@HeaderParam("phoneNumber") String phoneNumber) {

		String responseMessage = null;
		String token = null;
		Integer responseCode = null;

		// both parameters must contain values
		if (username == null || phoneNumber == null) {
			responseMessage = VALUE_MISSING_MESSAGE;
			responseCode = 401;
		}

		try {
			// save the phone number and the user name in the database and
			// returns a token that contains the user name as the issuer
			token = LendALotServiceHelper.getToken(username, phoneNumber);
			responseMessage = SUCCESS_MESSAGE;
			responseCode = 200;

		} catch (Exception e) {
			responseMessage = ERROR_MESSAGE;
			responseCode = 500;
		}

		return Response.status(responseCode).header("token", token)
				.entity(responseMessage).build();
	}
}
