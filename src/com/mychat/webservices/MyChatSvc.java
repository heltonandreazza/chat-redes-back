package com.mychat.webservices;

/**
 * @author helton.andreazza
 */

import java.io.IOException;
import java.net.UnknownHostException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import br.furb.principal.TrocaDeMensagen;

@Path("/mychatsvc")
public class MyChatSvc {

	@Path("getUsers")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(final User user) throws JSONException {
		String result; 
		  
		try {
			result = TrocaDeMensagen.getUsers(user.id, user.password);
			return Response.status(200).entity(result).build();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		return null;
	}
	
	@Path("getMessages")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMessages(final User user) throws JSONException {
		String result;
		
		try {
			result = TrocaDeMensagen.getMessage(user.id, user.password);
			return Response.status(200).entity(result).build();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Path("sendMessage")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendMessage(final User user) throws JSONException {
		String result;
		
		try {
			result = TrocaDeMensagen.sendMessage(user.id, user.password, user.sendId, user.message);
			return Response.status(200).entity(result).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
