package com.vikalp.azure.service;

import ch.qos.logback.core.util.ContentTypeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vikalp.azure.model.text.TextDocument;
import com.vikalp.azure.model.text.TextDocumentRequest;
import com.vikalp.azure.response.SentimentAnalysis;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Service
public class AzureSentimentService {

    @Value("${AZURE_API_KEY}") //set in the environment variables
    private String azureAPIkey;

    //change to your azure host url from https://portal.azure.com
    private static final String AZURE_ENDPOINT = "https://{XXX-PLEASE UPDATE- XXXX}.cognitiveservices.azure.com/";

    private static final String AZURE_ENDPOINT_PATH = "text/analytics/v3.0/sentiment";

    private static final String API_KEY_HEADER_NAME = "Ocp-Apim-Subscription-Key";

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String APPLICATION_JSON = "application/json";

    @Autowired
    private ObjectMapper mapper ;

    public SentimentAnalysis requestSentimentAnalysis(String text , String language) throws IOException, InterruptedException {

        TextDocument document = new TextDocument(language, UUID.randomUUID().toString(), text);
        TextDocumentRequest requestBody = new TextDocumentRequest();
        requestBody.addDocument(document);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(API_KEY_HEADER_NAME, azureAPIkey)
                .uri(URI.create(AZURE_ENDPOINT+AZURE_ENDPOINT_PATH))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());

        String sentimentValue = this.mapper.readValue(response.body(), JsonNode.class)
                .get("documents")
                .get(0)
                .get("sentiment")
                .asText();

        return  new SentimentAnalysis(document, sentimentValue);
    }
}
