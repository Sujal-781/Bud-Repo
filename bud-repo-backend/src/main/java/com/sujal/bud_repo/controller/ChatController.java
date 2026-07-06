package com.sujal.bud_repo.controller;

import com.sujal.bud_repo.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChatController {

    @Autowired
    private RepoService repoService;

    @PostMapping("/ingest")
    public String ingest(@RequestParam String repoUrl) throws Exception {
        return repoService.ingest(repoUrl);
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String question) throws Exception {
        return repoService.chat(question);
    }
}