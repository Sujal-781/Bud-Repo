package com.sujal.bud_repo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

class ReadFiles{
    public static void main(String[] args) {
        Path repoPath = Paths.get("/Users/sujalchoudhary/Desktop/Expensio");

        try(Stream<Path> paths = Files.walk(repoPath)){
            paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .filter(path -> !isIgnore(path))
                    .forEach(System.out::println);
        }
        catch (IOException e) {
            System.err.println("IO Error while walking directory: " + repoPath);
            e.printStackTrace();
        }
    }
    public static boolean isIgnore(Path path){
        String p = path.toString();
        return p.contains("target") ||
                p.contains("build") ||
                p.contains("node_modules") ||
                p.contains(".git")||
                p.startsWith(".");

    }
}