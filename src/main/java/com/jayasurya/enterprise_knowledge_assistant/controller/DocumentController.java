package com.jayasurya.enterprise_knowledge_assistant.controller;

import com.jayasurya.enterprise_knowledge_assistant.service.DocumentService;
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

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public String uploadDocument(@RequestParam("file") MultipartFile file) {

        List<Document> chunks =
                documentService.extractDocuments(file);

//        for (Document document : chunks) {
//            System.out.println("========== DOCUMENT ==========");
//            System.out.println(document.getText());
//            System.out.println("Metadata: " + document.getMetadata());
//        }

        return "PDF processed successfully";
    }
}
