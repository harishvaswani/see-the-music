package com.stm.service;

import java.util.List;

/**
 * Created by harishvaswani on 6/1/16.
 */
public class STMBean {

    private String classifier;
    private String accuracy;
    private List<String> photoURLs;

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public List<String> getPhotoURLs() {
        return photoURLs;
    }

    public void setPhotoURLs(List<String> photoURLs) {
        this.photoURLs = photoURLs;
    }
}
