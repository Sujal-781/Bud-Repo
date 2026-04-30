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

        CloneRepo.clone(repoUrl, localPath);

        List<String[]> rawChunks = ChunkFiles.getChunks(localPath);

        allChunks.clear();
        for (String[] chunk : rawChunks) {
            String fileName = chunk[0];
            String content = chunk[1];
            List<Double> embedding = EmbeddingExample.getEmbedding(content);
            allChunks.add(new CodeChunk(content, embedding, fileName));
        }

        return "✅ Indexed " + allChunks.size() + " chunks from " + repoUrl;
    }
}