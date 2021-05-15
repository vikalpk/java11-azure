package com.vikalp.azure.model.text;

import java.util.ArrayList;
import java.util.List;

public class TextDocumentRequest {

    List<TextDocument> documents =  null;

    public TextDocumentRequest(List<TextDocument> documents) {
        this.documents = documents;
    }

    public TextDocumentRequest() {
        this.documents = new ArrayList<>();
    }

    public  void addDocument(TextDocument document){
        this.documents.add(document);
    }

    public List<TextDocument> getDocuments() {
        return documents;
    }
 }
