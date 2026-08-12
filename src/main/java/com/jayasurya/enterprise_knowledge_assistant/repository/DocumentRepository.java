package com.jayasurya.enterprise_knowledge_assistant.repository;

import com.jayasurya.enterprise_knowledge_assistant.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository
        extends JpaRepository<DocumentEntity, Long> {

    boolean existsByDocumentHash(String documentHash);
}