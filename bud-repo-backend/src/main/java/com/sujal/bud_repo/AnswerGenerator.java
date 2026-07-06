package com.sujal.bud_repo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class AnswerGenerator {

    static final Gson gson = new Gson();

    public static String answerGenerator(String question, List<CodeChunk> relevantChunks) throws Exception {
        String apiKey = System.getenv("OPENAI_API_KEY");

        StringBuilder context = new StringBuilder();
        for (CodeChunk chunk : relevantChunks) {
            context.append("File: ").append(chunk.getFilename()).append("\n");
            context.append(chunk.getContent()).append("\n\n");
        }

        String prompt = "You are an expert at explaining codebases to new developers.\n\n"
                + "Here are relevant code snippets from the repository:\n"
                + context
                + "\nBased on these snippets, answer this question clearly:\n"
                + question
                + "\n\nBe specific about file names and methods. If unsure, say so.";

        // Build JSON safely using Gson — no manual escaping
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);

        JsonArray messages = new JsonArray();
        messages.add(message);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-4o-mini");
        requestBody.add("messages", messages);
        requestBody.addProperty("max_tokens", 500);

        String json = gson.toJson(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject body = gson.fromJson(response.body(), JsonObject.class);
        return body
                .getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();
    }
}