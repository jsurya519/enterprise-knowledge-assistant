package com.jayasurya.enterprise_knowledge_assistant.controller;

import com.jayasurya.enterprise_knowledge_assistant.service.DocumentService;
import com.jayasurya.enterprise_knowledge_assistant.service.EmbeddingService;
import com.jayasurya.enterprise_knowledge_assistant.service.VectorStoreService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ai.document.Document;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final VectorStoreService vectorStoreService;

    public DocumentController(DocumentService documentService, VectorStoreService vectorStoreService) {
        this.documentService = documentService;
        this.vectorStoreService = vectorStoreService;
    }

    @PostMapping
    public String uploadDocument(@RequestParam("file") MultipartFile file) {

        List<Document> chunks =
                documentService.extractDocuments(file);

        vectorStoreService.saveDocuments(chunks);

        return "PDF processed successfully";
    }
}
