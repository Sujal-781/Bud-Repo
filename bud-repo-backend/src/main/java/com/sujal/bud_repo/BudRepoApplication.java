package com.sujal.bud_repo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class BudRepoApplication {

	public static void main(String[] args) throws Exception {

		// Comment this out once you add Spring properly
		// SpringApplication.run(BudRepoApplication.class, args);

		// STEP 1 — Clone repo
		String repoUrl = "https://github.com/Sujal-781/Expensio";
		String localPath = "/Users/sujalchoudhary/Desktop/Expensio";
		CloneRepo.clone(repoUrl, localPath);

		// STEP 2+3 — Read and chunk
		List<String[]> rawChunks = ChunkFiles.getChunks(localPath);

        // STEP 4+5 — Embed and store
		List<CodeChunk> allChunks = new ArrayList<>();
		for (String[] chunk : rawChunks) {
			String fileName = chunk[0];
			String content = chunk[1];

			List<Double> embedding = EmbeddingExample.getEmbedding(content);
			allChunks.add(new CodeChunk(content, embedding, fileName));
		}

		System.out.println("✅ Indexed " + allChunks.size() + " chunks!");

		// STEP 6 — Chat loop
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("\nAsk: ");
			String question = scanner.nextLine();
			List<Double> qEmbedding = EmbeddingExample.getEmbedding(question);
			List<CodeChunk> topChunks = SimilaritySearch.findTopK(qEmbedding, allChunks, 3);
			String answer = AnswerGenerator.answerGenerator(question, topChunks);
			System.out.println("\n" + answer);
		}
	}
}