package com.jayasurya.enterprise_knowledge_assistant.service;

import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ai.embedding.EmbeddingModel;

import java.util.List;

@Service
public class DocumentService {


    private final TokenTextSplitter textSplitter;

    private final EmbeddingModel embeddingModel;

    public DocumentService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
        this.textSplitter = new TokenTextSplitter();
    }

    public List<Document> extractDocuments(MultipartFile file) {

        Resource resource = file.getResource();

        PagePdfDocumentReader reader =
                new PagePdfDocumentReader(resource);

        return textSplitter.apply(reader.get());

    }

}
