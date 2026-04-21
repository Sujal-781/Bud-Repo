package com.sujal.bud_repo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class AnswerGenerator {
    public static String answerGenerator(String question, List<CodeChunk> relevantChunks) throws Exception{
        String apiKey = System.getenv("OPENAI_API_KEY");

        StringBuilder context = new StringBuilder();
        for (CodeChunk chunk : relevantChunks) {
            context.append("File: ").append(chunk.getFilename()).append("\n");
            context.append(chunk.getContent()).append("\n\n");
        }

        String prompt = String.format("""
            You are an expert at explaining codebases to new developers.
            
            Here are relevant code snippets from the repository:
            %s
            
            Based on these snippets, answer this question clearly:
            %s
            
            Be specific about file names and methods. If unsure, say so.
            """, context, question);

        String json = String.format("""
            {
              "model": "gpt-4o-mini",
              "messages": [{"role": "user", "content": %s}],
              "max_tokens": 500
            }
            """, "\"" + prompt.replace("\"", "\\\"").replace("\n", "\\n") + "\"");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        // Parse the response — extract "content" field
        // For now just print raw, add proper JSON parsing next
        return response.body();
    }
}
