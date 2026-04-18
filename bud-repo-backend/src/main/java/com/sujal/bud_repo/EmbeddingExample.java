package com.sujal.bud_repo;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EmbeddingExample {

    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv("OPENAI_API_KEY");

        String inputText = "user login authentication logic";

        String json = String.format("""
        {
          "input": "%s",
          "model": "text-embedding-3-small"
        }
        """, inputText);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/embeddings"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}