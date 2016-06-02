package com.stm.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by harishvaswani on 6/1/16.
 */
public class STMServiceImpl implements STMService {

    private static final int MAX_LYRICS_LENGTH=1024;
    /**
     * Returns an STMBean object which is a composite of data collected from ChartLyrics, Watson and Flickr APIs.
     * It users the artist and the song names to get the lyrics of the song and pass that on to Watson
     * to derive the classification (mood) from the lyrics. The mood is then used to query the Flickr API
     * to get relevant images.
     * @param artist The name of the artist
     * @param song The name of the song
     * @return STMBean object composed of Watson and Flikr data
     */
    public STMBean getSTM(String artist, String song) {
        //Return hardcoded data
        STMBean stm = new STMBean();
        stm.setArtist(artist);
        stm.setSong(song);

        // Get lyrics
        String lyrics = getLyrics(artist, song);
        if (!lyrics.isEmpty()) {
            stm.setLyrics(lyrics);
           //Get the top matched class and confidence from Watson
            Classification classification = getClassification(lyrics);
            String topMatchedClass = classification.getTopClass();
            stm.setTopClass(topMatchedClass);

            List<ClassifiedClass> classes = classification.getClasses();
            for (ClassifiedClass aClass : classes) {
                if (topMatchedClass.equals(aClass.getName())) {
                    stm.setConfidence(aClass.getConfidence().toString());
                    break;
                }
            }

            stm.setPhotoURLs(getPhotoURLs(topMatchedClass));
        }
        return stm;
    }

    /**
     * Get lyrics of a song using the ChartLyrics API and parse the XML response
     * @param artist artist name
     * @param song song name
     * @return
     */
    private String getLyrics(String artist, String song) {

        String lyrics = null;
        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse("http://api.chartlyrics.com/apiv1.asmx/SearchLyricDirect?artist=" + artist + "&song=" + song);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        NodeList nodes = doc.getElementsByTagName("Lyric");
        lyrics = nodes.item(0).getTextContent();
        return lyrics;
    }

    /**
     * Returns classification data from Watson's NLC API
     * @param lyrics lyrics of a song
     * @return classification
     */
    private Classification getClassification(String lyrics) {
        NaturalLanguageClassifier service = new NaturalLanguageClassifier();
        service.setUsernameAndPassword("40982b27-3b08-4af8-9939-fccb1933535e", "OMeKvsugyLQl");
        // trim down the lyrics to the max length that classifier service can accept
        if (lyrics.length() >  MAX_LYRICS_LENGTH) {
            lyrics = lyrics.substring(0,1023);
        }
        Classification classification = service.classify("3a84cfx63-nlc-18782", lyrics).execute();
        return classification;
    }

    /**
     * Returns a list of photo URLs using the Flikr API based on the tag
     * @param tag the photo tag
     * @return list of photo URLs
     */
    private List<String> getPhotoURLs(String tag) {
        List<String> photoURLs = new ArrayList<String>();

        Client client = Client.create();

        WebResource webResource = client
                .resource("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=d332527aa9fd2b864ce83813c5ca32f1&tags=" + tag + "&per_page=10&page=1&format=json&&nojsoncallback=1");

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        return getConstructedURLs(response.getEntity(String.class));
    }

    protected List<String> getConstructedURLs(String flikrResponse) {

        /*
        Example flikr response
        { "photos": { "page": 1, "pages": "18122", "perpage": 5, "total": "90608",
                "photo": [
           { "id": "27329822001", "owner": "115130635@N04", "secret": "e3d7392e1b", "server": "7283", "farm": 8, "title": "Simply Rest", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
           { "id": "27299132642", "owner": "85608594@N00", "secret": "d5b7138b89", "server": "7298", "farm": 8, "title": "Michael Bernard Beckwith When you wake up in the morning, let your first thought be one of thanksgiving", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
           { "id": "27362366296", "owner": "85608594@N00", "secret": "da7b540c14", "server": "7409", "farm": 8, "title": "Eckhart Tolle The way in which you perceive the other is determined by your own thought forms", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
           { "id": "26784417044", "owner": "85608594@N00", "secret": "9235df94fa", "server": "7226", "farm": 8, "title": "Mandy Hale You don't always need a plan. Sometimes you just need to breathe, trust, let go and see what happens", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
           { "id": "26782758893", "owner": "121900055@N05", "secret": "61dc463f47", "server": "7094", "farm": 8, "title": "The Little Things In Life", "ispublic": 1, "isfriend": 0, "isfamily": 0 }
           ] }, "stat": "ok" }
        */

        List<String> photoURLs = new ArrayList<String>();
        //Construct the URLs
        JsonReader reader = new JsonReader(new StringReader(flikrResponse));
        reader.setLenient(true);
        JsonElement jelement = new JsonParser().parse(reader);
        JsonObject jobject = jelement.getAsJsonObject();
        jobject = jobject.getAsJsonObject("photos");
        JsonArray jarray = jobject.getAsJsonArray("photo");
        for (int i = 0; i<jarray.size(); i++) {
            jobject = jarray.get(i).getAsJsonObject();
            String farmId = jobject.get("farm").getAsString();
            String serverId = jobject.get("server").getAsString();
            String id = jobject.get("id").getAsString();
            String secret = jobject.get("secret").getAsString();

            //Example URL: https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
            String url = "https://farm" + farmId + ".staticflickr.com/" + serverId + "/" + id + "_" + secret + ".jpg";
            photoURLs.add(url);
        }
        return photoURLs;
    }
}
