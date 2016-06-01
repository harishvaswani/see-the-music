package com.stm.rest;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hvaswani on 11/11/2015.
 */
public class JSONResponseBuilder {

    /**
     *
     * @param status status to be included in the response
     * @param message message to be included in the response
     * @param payload payload to be included in the reponse
     * @return JSON formatted string
     */
    public static String getJSONResponse(String status, String message, Object payload) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("message", message);
        map.put("payload", payload);
        return new Gson().toJson(map);
    }
}
