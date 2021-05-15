package com.vikalp.azure;

import com.vikalp.azure.service.AzureSentimentService;
import com.vikalp.azure.response.SentimentAnalysis;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AzureCognitiveApplicationTests {

	@Test
	void contextLoads() {
	}

	@Value("${AZURE_API_KEY}")
	private String azureAPIkey;

	@Autowired
	private AzureSentimentService sentimentService;

	private static final String AZURE_ENDPOINT = "UPDATE END POINT ";

	private static final String AZURE_ENDPOINT_PATH = "text/analytics/v3.0/sentiment";

	private static final String API_KEY_HEADER_NAME = "Ocp-Apim-Subscription-Key";

	private static final String CONTENT_TYPE = "Content-Type";

	private static final String APPLICATION_JSON = "application/json";

	private static final String EXAMPLE_JSON = "{\"documents\":[{\"language\":\"en\",\"id\":\"1\",\"text\":\"Hello world. This is some input text that I love.\"}]}";

	//@Test
	public void getEntities() throws IOException, InterruptedException {

		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder()
				.header(CONTENT_TYPE, APPLICATION_JSON)
				.header(API_KEY_HEADER_NAME, azureAPIkey)
				.uri(URI.create(AZURE_ENDPOINT+AZURE_ENDPOINT_PATH))
				.POST(HttpRequest.BodyPublishers.ofString(EXAMPLE_JSON))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println(response.statusCode());
		System.out.println(response.body());

	}


	@Test
	void testPositiveSentiment() throws IOException, InterruptedException {

		SentimentAnalysis analysis = this.sentimentService.requestSentimentAnalysis("I love icecream!", "en");
		assertNotNull(analysis);
		assertEquals("positive", analysis.getSentiment());
	}

	@Test
	void testNegativeSentiment() throws IOException, InterruptedException {

		SentimentAnalysis analysis = this.sentimentService.requestSentimentAnalysis("Icecream is horrible", "en");
		assertNotNull(analysis);
		assertEquals("negative", analysis.getSentiment());
	}


}
