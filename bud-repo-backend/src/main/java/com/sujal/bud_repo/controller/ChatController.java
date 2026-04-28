package com.sujal.bud_repo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class ChatController {

    @PostMapping("/ingest")
    public String ingest(@RequestParam String repoUrl) {

        return "Ingestion started for: " + repoUrl;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String question) {

        return "Question received: " + question;
    }
}
