package com.jayasurya.enterprise_knowledge_assistant.service;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentTextCleaner {

    public Document clean(Document document) {

        String text = document.getText();

        // Replace tabs with spaces
        text = text.replace("\t", " ");

        // Replace multiple spaces with a single space
        text = text.replaceAll(" {2,}", " ");

        // Remove excessive blank lines
        text = text.replaceAll("\\n\\s*\\n+", "\n\n");

        // Remove leading/trailing whitespace from each line
        text = text.replaceAll("(?m)^\\s+", "");
        text = text.replaceAll("(?m)\\s+$", "");

        // Trim the entire document
        text = text.trim();

        return new Document(text, document.getMetadata());
    }
}
