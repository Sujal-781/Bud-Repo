package com.sujal.bud_repo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class ChatController {

    @PostMapping("/ingest")
    public String ingest(@RequestParam String repoUrl) {
        // TODO: clone, chunk, embed
        return "Ingestion started for: " + repoUrl;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String question) {
        // TODO: similarity search + answer generation
        return "Question received: " + question;
    }
}
