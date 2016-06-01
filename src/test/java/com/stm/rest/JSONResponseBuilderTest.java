package com.stm.rest;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

import com.google.gson.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hvaswani on 11/11/2015.
 */
public class JSONResponseBuilderTest {

    @Test
    public void getJSONResponse() {
        List<String> userList = new ArrayList<String>();
        userList.add("mary");
        userList.add("joe");
        String json = JSONResponseBuilder.getJSONResponse("200","success",userList);

        //Validate that this is a valid json
        JsonElement rootElement = null;
        try {
            rootElement = new JsonParser().parse(json);
        } catch (JsonParseException jpe) {
            // It should never get here
            fail();
        }

        // Validate the returned json structure
        JsonObject root = rootElement.getAsJsonObject();
        JsonElement statusElement = root.get("status");
        assertEquals("200",statusElement.getAsString());
        JsonElement messageElement = root.get("message");
        assertEquals("success", messageElement.getAsString());
        JsonArray payloadArray = root.getAsJsonArray("payload");
        assertEquals("mary",payloadArray.get(0).getAsString());
        assertEquals("joe",payloadArray.get(1).getAsString());

        //NOTE: Below is another way we could have asserted the JSON String coming back from the method but
        // since the order of the elements doesn't really determine the equality, the above approach
        // seemed more effective
        // assertEquals("{\"payload\":[\"mary\",\"joe\"],\"message\":\"success\",\"status\":\"200\"}", json);
    }
}
