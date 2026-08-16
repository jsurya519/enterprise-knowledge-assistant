package com.jayasurya.enterprise_knowledge_assistant.controller;

import com.jayasurya.enterprise_knowledge_assistant.service.*;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ClientChatService clientChatService;

    public ChatController(ClientChatService clientChatService) {
        this.clientChatService = clientChatService;
    }

    @PostMapping
    public String chat(@RequestParam String question) {
        return clientChatService.ask(question);
    }
}
