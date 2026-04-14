package com.sujal.bud_repo;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class ChunkFiles {

    public static void main(String[] args) {
        Path repoPath = Paths.get("/Users/sujalchoudhary/Desktop/Expensio");

        List<String> allChunks = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(repoPath)) {

            paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .filter(path -> !isIgnored(path))
                    .forEach(path -> {
                        try {
                            List<String> chunks = chunkFile(path);
                            allChunks.addAll(chunks);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print some output
        System.out.println("Total chunks: " + allChunks.size());
        allChunks.stream().limit(5).forEach(chunk -> {
            System.out.println("----- CHUNK -----");
            System.out.println(chunk);
        });
    }

    // 🔹 Chunking logic
    private static List<String> chunkFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        List<String> chunks = new ArrayList<>();

        int chunkSize = 200;

        for (int i = 0; i < lines.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, lines.size());

            List<String> subList = lines.subList(i, end);

            String chunk = String.join("\n", subList);

            chunks.add(chunk);
        }

        return chunks;
    }

    // 🔹 Ignore junk folders/files
    private static boolean isIgnored(Path path) {
        String p = path.toString();

        return p.contains("node_modules") ||
                p.contains(".git") ||
                p.contains("target") ||
                p.contains("build") ||
                path.getFileName().toString().startsWith(".");
    }
}