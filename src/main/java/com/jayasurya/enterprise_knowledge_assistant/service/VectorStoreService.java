package com.jayasurya.enterprise_knowledge_assistant.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VectorStoreService {

    private final VectorStore vectorStore;

    public VectorStoreService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void saveDocuments(List<Document> documents) {
        vectorStore.add(documents);
    }
}
