package com.sujal.bud_repo;

import java.util.ArrayList;
import java.util.List;

public class StoreEmbeddings {
    public static void main(String[] args) throws Exception {
        List<String[]> chunks = ChunkFiles.getChunks("/Users/sujalchoudhary/Desktop/Expensio");

        List<CodeChunk> store = new ArrayList<>();

        for(String[] chunk : chunks){
            String filename = chunk[0];
            String content = chunk[1];

            List<Double> vector = EmbeddingExample.getEmbedding(content);
            store.add(new CodeChunk(content, vector, filename));
        }
        System.out.println("Total chunks stored: " + store.size());
        System.out.println("First chunk file: " + store.get(0).filename);
        System.out.println("Vector length: " + store.get(0).embedding.size());
    }
}
