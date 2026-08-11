package com.jayasurya.enterprise_knowledge_assistant.service;

import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;

import java.util.List;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public List<float[]> generateEmbeddings(List<Document> documents) {

        List<String> texts = documents.stream()
                .map(Document::getText)
                .toList();

        return embeddingModel.embed(texts);
    }
}