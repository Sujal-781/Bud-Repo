package com.sujal.bud_repo;

import java.util.List;

public class CodeChunk {
    String content;
    List<Double> embedding;
    String filename;

    public CodeChunk(String content, List<Double> embedding, String filename) {
        this.content = content;
        this.embedding = embedding;
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public List<Double> getEmbedding() {
        return embedding;
    }

    public String getFilename() {
        return filename;
    }
}
