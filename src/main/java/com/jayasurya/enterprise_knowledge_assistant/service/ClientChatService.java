package com.jayasurya.enterprise_knowledge_assistant.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ClientChatService {

    private final ChatClient chatClient;
    private final DocumentSearchService documentSearchService;

    public ClientChatService(
            ChatClient.Builder chatClientBuilder,
            DocumentSearchService documentSearchService) {
        this.chatClient = chatClientBuilder.build();
        this.documentSearchService = documentSearchService;
    }

    public String ask(String question) {

        // 1. Retrieve relevant chunks
        List<Document> documents =
                documentSearchService.search(question);

        System.out.println("doc search "+ documents.size());


        if (documents.isEmpty()) {
            return "I couldn't find relevant information in the uploaded documents.";
        }

        // 2. Build context from retrieved documents
        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));

        // 3. Build prompt
        String prompt = """
                Answer the question using ONLY the provided context.

                If the answer cannot be found in the context,
                say that the information is not available in
                the provided documents.

                Context:
                %s

                Question:
                %s
                """.formatted(context, question);

        // 4. Send prompt to LLM
        String answer = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return answer;

        //return answer + buildSources(documents);
    }

    private String buildContext(List<Document> documents) {

        return IntStream.range(0, documents.size())
                .mapToObj(i -> {

                    Document document = documents.get(i);

                    String fileName =
                            String.valueOf(
                                    document.getMetadata().get("file_name"));

                    String pageNumber =
                            String.valueOf(
                                    document.getMetadata().get("page_number"));

                    return """
                            [Source %d]
                            File: %s
                            Page: %s

                            %s
                            """.formatted(
                            i + 1,
                            fileName,
                            pageNumber,
                            document.getText());
                })
                .collect(Collectors.joining("\n--------------------\n"));
    }

    private String buildSources(List<Document> documents) {

        String sources = IntStream.range(0, documents.size())
                .mapToObj(i -> {

                    Document document = documents.get(i);

                    String fileName =
                            String.valueOf(
                                    document.getMetadata().get("file_name"));

                    String pageNumber =
                            String.valueOf(
                                    document.getMetadata().get("page_number"));

                    return "- %s — Page %s"
                            .formatted(fileName, pageNumber);
                })
                .distinct()
                .collect(Collectors.joining("\n"));

        return "\n\nSources:\n" + sources;
    }
}
