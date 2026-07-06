package com.sujal.bud_repo.service;

import com.sujal.bud_repo.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepoService {

    public List<CodeChunk> allChunks = new ArrayList<>();

    public String ingest(String repoUrl) throws Exception {
        String localPath = "/tmp/bud-repo-clone";

        // Delete old clone if it exists
        java.io.File dir = new java.io.File(localPath);
        if (dir.exists()) deleteDirectory(dir);

        CloneRepo.clone(repoUrl, localPath);

        List<String[]> rawChunks = ChunkFiles.getChunks(localPath);

        allChunks.clear();
        for (String[] chunk : rawChunks) {
            String fileName = chunk[0];
            String content = chunk[1];
            List<Double> embedding = EmbeddingExample.getEmbedding(content);
            allChunks.add(new CodeChunk(content, embedding, fileName));
            Thread.sleep(200);
        }

        return "Indexed " + allChunks.size() + " chunks from " + repoUrl;
    }

    public String chat(String question) throws Exception {
        if (allChunks.isEmpty()) {
            return "No repo indexed yet. Call POST /ingest?repoUrl=... first.";
        }
        List<Double> qEmbedding = EmbeddingExample.getEmbedding(question);
        List<CodeChunk> topChunks = SimilaritySearch.findTopK(qEmbedding, allChunks, 3);
        return AnswerGenerator.answerGenerator(question, topChunks);
    }

    private void deleteDirectory(java.io.File dir) {
        for (java.io.File file : dir.listFiles()) {
            if (file.isDirectory()) deleteDirectory(file);
            else file.delete();
        }
        dir.delete();
    }
}