/**
 * 
 */
package com.codecavaliers.lendalot.rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * @author nicu
 *
 */
@Path("/lendalot")
public class LendALotService {

	@GET
	@Path("/restoreDebts/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response restoreDebts(@PathParam("param") String fromNumber) {

		int statusCode;
		StringBuilder output = new StringBuilder();

		try {

			MongoClient mongo = new MongoClient("localhost", 27017);

			DB db = mongo.getDB("lendalot");

			DBCollection table = db.getCollection("lendalot");

			BasicDBObject query = new BasicDBObject("fromNumber", fromNumber);

			DBCursor cursor = table.find(query);

			Map<String, Renter> renters = new HashMap<String, Renter>();

			while (cursor.hasNext()) {

				DBObject row = cursor.next();

				String toNumber = (String) row.get("toNumber");
				String product = (String) row.get("product");
				Integer quantity = (Integer) row.get("quantity");

				Renter renter = renters.get(toNumber);

				if (renter == null) {
					renter = new Renter();
					renter.setToNumber(toNumber);
					renters.put(toNumber, renter);
				}

				Map<String, Integer> products = renter.getProducts();

				if (products == null) {
					products = new HashMap<String, Integer>();
					renter.setProducts(products);
				}

				Integer prdQuatity = products.get(product);

				if (prdQuatity == null) {
					prdQuatity = 0;
				}

				prdQuatity += quantity;

				products.put(product, prdQuatity);
			}

			output.append("[");

			Iterator<Renter> it = renters.values().iterator();

			while (it.hasNext()) {
				Renter renter = it.next();

				output.append(renter.toString());

				if (it.hasNext()) {
					output.append(",");
				}
			}

			output.append("]");

			statusCode = 200;

		} catch (Exception e) {

			e.printStackTrace();

			statusCode = 400;
		}

		return Response.status(statusCode).entity(output.toString()).build();

	}

	@POST
	@Path("/persistDebts")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addRow(Row row) {

		int statusCode;

		try {

			MongoClient mongo = new MongoClient("localhost", 27017);

			DB db = mongo.getDB("lendalot");

			DBCollection table = db.getCollection("lendalot");

			BasicDBObject document = new BasicDBObject();

			document.put("fromNumber", row.getFromNumber());
			document.put("toNumber", row.getToNumber());
			document.put("product", row.getProduct());
			document.put("quantity", row.getQuantity());

			table.insert(document);

			statusCode = 201;

		} catch (Exception e) {

			e.printStackTrace();

			statusCode = 400;
		}

		return Response.status(statusCode).build();
	}
}