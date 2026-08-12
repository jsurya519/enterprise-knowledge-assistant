package com.jayasurya.enterprise_knowledge_assistant.service;

import com.jayasurya.enterprise_knowledge_assistant.entity.DocumentEntity;
import com.jayasurya.enterprise_knowledge_assistant.repository.DocumentRepository;
import com.jayasurya.enterprise_knowledge_assistant.util.DocumentHashUtil;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {


    private final TokenTextSplitter textSplitter;
    private final DocumentTextCleaner documentTextCleaner;
    private final DocumentHashUtil documentHashUtil;
    private final DocumentRepository documentRepository;
    private final VectorStoreService vectorStoreService;



    public DocumentService(DocumentTextCleaner documentTextCleaner,
                           DocumentHashUtil documentHashUtil,
                           DocumentRepository documentRepository,
                           VectorStoreService vectorStoreService) {
        this.documentTextCleaner = documentTextCleaner;
        this.documentHashUtil = documentHashUtil;
        this.documentRepository = documentRepository;
        this.vectorStoreService = vectorStoreService;
        this.textSplitter = TokenTextSplitter.builder()
                .build();
    }

    public void ingestDocument(MultipartFile file) {

        String documentHash =
                documentHashUtil.calculateSha256(file);

        if (documentRepository.existsByDocumentHash(documentHash)) {
            throw new IllegalArgumentException(
                    "Document already exists");
        }

        List<Document> chunks =
                extractDocuments(file);

        System.out.println("Chunks extracted");

        vectorStoreService.saveDocuments(chunks);


        System.out.println("saved to vector");

        DocumentEntity documentEntity = new DocumentEntity();

        documentEntity.setDocumentHash(documentHash);
        documentEntity.setFileName(file.getOriginalFilename());
        documentEntity.setFileSize(file.getSize());
        documentEntity.setUploadedAt(LocalDateTime.now());

        documentRepository.save(documentEntity);
    }

    public List<Document> extractDocuments(MultipartFile file) {

        Resource resource = file.getResource();

        PagePdfDocumentReader reader =
                new PagePdfDocumentReader(resource);

        List<Document> documents = reader.get();

        List<Document> cleanedDocuments =
                documents.stream()
                        .map(documentTextCleaner::clean)
                        .toList();

        return textSplitter.apply(cleanedDocuments);

    }
}
