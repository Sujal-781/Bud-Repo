package com.sujal.bud_repo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;

public class CloneRepo {
    public static void main(String[] args) {

        String repoUrl = "https://github.com/Sujal-781/Expensio";
        String location = "/Users/sujalchoudhary/Desktop/Expensio";
        File dir = new File(location);

        if (dir.exists()) {
            System.out.println("Directory already exists!");
            return;
        }

        try{
            Git git = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(dir)
                    .call();

            git.close();

            System.out.println("Repository cloned successfully");
        }
        catch (GitAPIException e){
            System.out.println("Error clong repo: " + e.getMessage());
        }
    }
}
