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
}
