import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifier;

import java.io.File;

/**
 * Created by harishvaswani on 5/31/16.
 */



public class MyNaturalLanguageClassifier {

    public static void main(String args[]) {
        NaturalLanguageClassifier service = new NaturalLanguageClassifier();
        service.setUsernameAndPassword("40982b27-3b08-4af8-9939-fccb1933535e", "OMeKvsugyLQl");

        //File file = new File("./song_classifier_training_data.csv");
        //System.out.println(file.exists());

        // Short Classifier - Just to try
        //Classifier classifier = service.createClassifier("STMClassifier", "en", new File("./song_classifier_training_data_short.csv")).execute();
        //System.out.println(classifier);
        //Classifier classifier = service.getClassifier("3a84d1x62-nlc-18503").execute();
        //System.out.println(classifier);
        //service.deleteClassifier("3a84d1x62-nlc-18503").execute();

        // The Full Classifier
        //Classifier classifier = service.createClassifier("STMClassifier", "en", new File("./song_classifier_training_data.csv")).execute();
        //System.out.println(classifier);
        //Classifier classifier = service.getClassifier("3a84cfx63-nlc-18782").execute();
        //System.out.println(classifier);

        Classification classification = service.classify("3a84cfx63-nlc-18782", "The heart beats for you").execute();
        System.out.println(classification);

    }
}


