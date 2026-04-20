package com.sujal.bud_repo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SimilaritySearch {

    public static List<CodeChunk> findTopK(List<Double> questionEmbedding,
                                           List<CodeChunk> chunks, int k) {
        return chunks.stream()
                .sorted(Comparator.comparingDouble(chunk ->
                        -cosineSimilarity(questionEmbedding, chunk.getEmbedding())))
                .limit(k)
                .collect(Collectors.toList());
    }

    private static double cosineSimilarity(List<Double> a, List<Double> b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.size(); i++) {
            dot   += a.get(i) * b.get(i);
            normA += a.get(i) * a.get(i);
            normB += b.get(i) * b.get(i);
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}