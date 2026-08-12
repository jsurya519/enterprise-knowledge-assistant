package com.jayasurya.enterprise_knowledge_assistant.controller;

import com.jayasurya.enterprise_knowledge_assistant.service.DocumentSearchService;
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
    private final DocumentSearchService documentSearchService;


    public DocumentController(DocumentService documentService, VectorStoreService vectorStoreService, DocumentSearchService documentSearchService) {
        this.documentService = documentService;
        this.vectorStoreService = vectorStoreService;
        this.documentSearchService = documentSearchService;
    }

    @PostMapping
    public String uploadDocument(@RequestParam("file") MultipartFile file) {

        documentService.ingestDocument(file);

        return "PDF processed successfully";
    }

    @PostMapping("/search")
    public List<Document> search(
            @RequestParam("question") String question) {

        return documentSearchService.search(question);
    }
}
