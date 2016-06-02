package com.stm.rest;

import com.stm.service.STMBean;
import com.stm.service.STMService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by harishvaswani on 6/1/16.
 */

@Path("/stm")
public class STMRest {

    private STMService stmService;

    public void setStmService(STMService stmService) {
        this.stmService = stmService;
    }

    public STMService getStmService() {
        return stmService;
    }

    /**
     * API to get the response from the STM service
     * @return JSON response
     */
    @GET
    @Path("/response")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSTMResponse(@QueryParam("artist") String artist, @QueryParam("song") String song) {

        STMBean stm = stmService.getSTM(artist,song);
        return Response.status(Response.Status.OK).entity(
                JSONResponseBuilder.getJSONResponse(Response.Status.OK.toString(),null,stm)).build();
    }
}
