package com.github.eunsiljo.parsertest.model;

import java.util.ArrayList;

/**
 * Created by EunsilJo on 2018. 1. 6..
 */

public class JSearchImageResult {
    private JMeta meta;
    private ArrayList<JImageItem> documents;

    public JMeta getMeta() {
        return meta;
    }

    public ArrayList<JImageItem> getDocuments() {
        return documents;
    }
}
