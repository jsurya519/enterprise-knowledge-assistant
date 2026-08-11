package com.jayasurya.enterprise_knowledge_assistant.controller;

import com.jayasurya.enterprise_knowledge_assistant.service.*;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ai.document.Document;



@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ClientChatService clientChatService;

    public ChatController(ClientChatService clientChatService) {
        this.clientChatService = clientChatService;
    }

    @PostMapping
    public String chat(@RequestBody String question) {
        return clientChatService.ask(question);
    }
}
