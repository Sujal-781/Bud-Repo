package com.sujal.bud_repo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class EmbeddingExample {

    static final HttpClient client = HttpClient.newHttpClient();
    static final Gson gson = new Gson();

    public static List<Double> getEmbedding(String text) throws Exception {
        String apiKey = System.getenv("OPENAI_API_KEY");

        String json = String.format("""
        {
          "input": "%s",
          "model": "text-embedding-3-small"
        }
        """, text);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/embeddings"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject body = gson.fromJson(response.body(), JsonObject.class);
        JsonArray values = body
                .getAsJsonArray("data")
                .get(0).getAsJsonObject()
                .getAsJsonArray("embedding");

        List<Double> embedding = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            embedding.add(values.get(i).getAsDouble());
        }

        return embedding;
    }

    public static void main(String[] args) throws Exception {
        List<Double> embedding = getEmbedding("user login authentication logic");
        System.out.println("Vector length: " + embedding.size());
        System.out.println("First 5 values: " + embedding.subList(0, 5));
    }
}