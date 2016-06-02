package com.stm.service;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

/**
 * Created by harishvaswani on 6/1/16.
 */
public class STMServiceImplTest {

    @Test
    public void getConstructedURLs() {
        // Create a sample flikr response

        String flikrResponse= "{ \"photos\": { \"page\": 1, \"pages\": \"18122\", \"perpage\": 5, \"total\": \"90608\",\n" +
                "                \"photo\": [" +
                "           { \"id\": \"27329822001\", \"owner\": \"115130635@N04\", \"secret\": \"e3d7392e1b\", \"server\": \"7283\", \"farm\": 8, \"title\": \"Simply Rest\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 }," +
                "           { \"id\": \"27299132642\", \"owner\": \"85608594@N00\", \"secret\": \"d5b7138b89\", \"server\": \"7298\", \"farm\": 8, \"title\": \"Michael Bernard Beckwith When you wake up in the morning, let your first thought be one of thanksgiving\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 }" +
                "        ] }, \"stat\": \"ok\" }";

        STMServiceImpl stmService = new STMServiceImpl();
        List<String> urls = stmService.getConstructedURLs(flikrResponse);
        //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
        assertEquals("https://farm8.staticflickr.com/7283/27329822001_e3d7392e1b.jpg",urls.get(0));
        assertEquals("https://farm8.staticflickr.com/7298/27299132642_d5b7138b89.jpg",urls.get(1));
    }

    @Test
    public void getSTM() {
        //Let's just test that we get an STM bean populated.

        STMServiceImpl stmService = new STMServiceImpl();
        STMBean stm = stmService.getSTM("Madonna", "Holiday");
        assertEquals("Madonna",stm.getArtist());
        assertEquals("Holiday",stm.getSong());

        //Results from the API - Just ensure you get some data back. No need to test the content

        //ChartLyrics API
        assertFalse(stm.getLyrics().isEmpty());

        //Watson Natural Language Classifier API
        assertFalse(stm.getConfidence().isEmpty());
        assertFalse(stm.getTopClass().isEmpty());

        //Flickr API
        assertFalse(stm.getPhotoURLs().isEmpty());
    }
}
