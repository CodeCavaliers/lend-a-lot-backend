/**
 * 
 */
package com.codecavaliers.lendalot.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

	private static final String ERROR_MESSAGE = "Error";

	/**
	 * @param fNumber
	 * @return
	 * 
	 * Produces a json that contains all the information about the things that other people borrowed from you
	 */
	@GET
	@Path("/restoreDebts/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response restoreDebts(@PathParam("param") String fNumber) {

		int statusCode;
		Debt debts = null;

		try {

			debts = LendALotServiceUtil.getDebts(fNumber,
					LendALotServiceUtil.COL_NAME_FROM_NUMBER,
					LendALotServiceUtil.COL_NAME_TO_NUMBER);
			statusCode = 200;

		} catch (Exception e) {
			statusCode = 500;
		}

		return Response.status(statusCode)
				.entity(debts == null ? ERROR_MESSAGE : debts.toString())
				.build();
	}

	/**
	 * @param fNumber
	 * @return
	 * Copy-paste much? semnat boogie
	 * Produces a json that contains all the information about the things that you borrowed from other people
	 */
	@GET
	@Path("/restoreMyDebts/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response restoreMyDebts(@PathParam("param") String fNumber) {

		int statusCode;
		Debt debts = null;

		try {

			debts = LendALotServiceUtil.getDebts(fNumber,
					LendALotServiceUtil.COL_NAME_TO_NUMBER,
					LendALotServiceUtil.COL_NAME_FROM_NUMBER);
			statusCode = 200;

		} catch (Exception e) {
			statusCode = 500;
		}

		return Response.status(statusCode)
				.entity(debts == null ? ERROR_MESSAGE : debts.toString())
				.build();
	}

	/**
	 * @param debts
	 * @return
	 * 
	 * Replace the information from the data base with the one received
	 */
	@POST
	@Path("/persistDebts")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response persistDebts(Debt debts) {

		int statusCode;

		try {

			LendALotServiceUtil.saveDebts(debts);

			statusCode = 201;

		} catch (Exception e) {
			statusCode = 500;
		}

		return Response.status(statusCode).build();
	}
}
