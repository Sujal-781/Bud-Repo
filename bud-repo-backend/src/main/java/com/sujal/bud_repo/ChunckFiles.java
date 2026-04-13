package com.sujal.bud_repo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ChunckFiles {
    public static void main(String[] args) {
        Path repoPath = Paths.get("/Users/sujalchoudhary/Desktop/Expensio");
        List<String> allChunks = new ArrayList<>();
        try(Stream<Path> paths = Files.walk(repoPath)){
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

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Print some output
        System.out.println("Total chunks: " + allChunks.size());
        allChunks.stream().limit(5).forEach(chunk -> {
            System.out.println("----- CHUNK -----");
            System.out.println(chunk);
        });
    }

    private static List<String> chunkFile(Path path) throws IOException {
        return new ArrayList<>();
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
