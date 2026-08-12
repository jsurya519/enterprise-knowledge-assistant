package com.jayasurya.enterprise_knowledge_assistant.service;

import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DocumentService {


    private final TokenTextSplitter textSplitter;
    private final DocumentTextCleaner documentTextCleaner;



    public DocumentService(DocumentTextCleaner documentTextCleaner) {
        this.documentTextCleaner = documentTextCleaner;
        this.textSplitter = TokenTextSplitter.builder()
                .build();
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

        for (Document document : cleanedDocuments) {
            System.out.println(document.getText());
        }

        return textSplitter.apply(cleanedDocuments);

    }

}
